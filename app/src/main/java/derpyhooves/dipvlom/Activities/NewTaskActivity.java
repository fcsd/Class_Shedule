package derpyhooves.dipvlom.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Handler;

import derpyhooves.dipvlom.Adapters.AlarmAdapter;
import derpyhooves.dipvlom.Fragments.TasksFragment;
import derpyhooves.dipvlom.R;

public class NewTaskActivity extends AppCompatActivity {

    private ArrayList<String> ListOfSubject = new ArrayList<>();
    private String group;
    private String currentSubject;
    private String subject;
    private String title=new String();
    private String displayDate;
    private EditText editText;

    private ArrayList<String> currentTask;

    private int mode;
    private boolean isFirstOpenDatePicker = true;
    private long previousDayInMillis;
    private long targetDayInMillis;
    private long currentNotificationId;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Додати завдання");

        mode = getIntent().getIntExtra("mode", 0);

        if (mode==1)
        {
            currentSubject = getIntent().getStringExtra("subject");
            group = getIntent().getStringExtra("group");
        }
        if (mode==2)
        {
            currentTask = getIntent().getStringArrayListExtra("tasks");
            group=currentTask.get(0);
        }
        getGroup();
        getSubject();
        getDate();
        getText();
    }


    public void getGroup()
    {

        final ArrayList<String> keysOfSavedSchedule=GroupActivity.restoreArrayListFromSP(this,"keysOfGroup");

        TextView textView = (TextView) findViewById(R.id.section_text4);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //SPINNER FOR GROUP
        ArrayAdapter<String> adapterForGroup = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, keysOfSavedSchedule);
        adapterForGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner forGroup = (Spinner) findViewById(R.id.spinner2);
        forGroup.setAdapter(adapterForGroup);
        // выделяем элемент
        forGroup.setSelection(keysOfSavedSchedule.indexOf(group));
        // устанавливаем обработчик нажатия
        forGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                group = keysOfSavedSchedule.get(position);
                getSubject();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void getSubject()
    {
        TextView textView = (TextView) findViewById(R.id.section_text1);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ListOfSubject = GroupActivity.restoreArrayListFromSP(this, group);
        ListOfSubject = removeRepeatSubject();

        //SPINNER FOR SUBJECT

        ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ListOfSubject);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forSubject = (Spinner) findViewById(R.id.spinner1);

        forSubject.setAdapter(adapterForSubject);
        // выделяем элемент
        if (mode==1) forSubject.setSelection(ListOfSubject.indexOf(currentSubject));
        if (mode==2) forSubject.setSelection(ListOfSubject.indexOf(currentTask.get(1)));
        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                subject = ListOfSubject.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }


    public void getDate()
    {
        TextView textView = (TextView) findViewById(R.id.section_text2);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //CALENDAR
        final Calendar c1 = Calendar.getInstance();
        final int[] year = {c1.get(Calendar.YEAR)};
        final int[] month = {c1.get(Calendar.MONTH)};
        final int[] day = {c1.get(Calendar.DAY_OF_MONTH)};


        if (mode==1)
        {
            Date jud = null;
            try {
                jud = new SimpleDateFormat("yyyy MM dd").parse(Integer.toString(year[0]) + " " + Integer.toString(month[0] + 1) + " " + Integer.toString(day[0]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            displayDate = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("uk", "UA")).format(jud);
            targetDayInMillis = c1.getTimeInMillis();
        }

        if (mode==2)
        {
            Date date = new Date();
            date.setTime(Long.parseLong(currentTask.get(2)));
            new SimpleDateFormat("yyyy MM dd").format(date);
            displayDate = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("uk", "UA")).format(date);
            targetDayInMillis = (Long.parseLong(currentTask.get(2)));
        }


        final TextView datePicker = (TextView) findViewById(R.id.clickable_calendar);
        assert datePicker != null;
        datePicker.setText("Виконати до " + displayDate);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                        if (arg0.isShown()) {

                            Date jud = null;
                            try {
                                jud = new SimpleDateFormat("yyyy MM dd").parse(Integer.toString(arg1) + " " + Integer.toString(arg2 + 1) + " " + Integer.toString(arg3));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            c1.set(arg1,arg2,arg3, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                    Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND));
                            targetDayInMillis = c1.getTimeInMillis();
                            previousDayInMillis = targetDayInMillis - 1000 * 60 * 60 * 24;

                            displayDate = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("uk", "UA")).format(jud);
                            datePicker.setText("Виконати до " + displayDate);

                            year[0] = arg1;
                            month[0]=arg2;
                            day[0]=arg3;
                        }
                    }
                };

                if (mode==2 && isFirstOpenDatePicker)
                {
                    c1.setTimeInMillis(Long.parseLong(currentTask.get(2)));
                    year[0] = c1.get(Calendar.YEAR);
                    month[0]=c1.get(Calendar.MONTH);
                    day[0]=c1.get(Calendar.DAY_OF_MONTH);
                    isFirstOpenDatePicker = false;
                }

                DatePickerDialog dp = new DatePickerDialog(NewTaskActivity.this, myDateListener, year[0], month[0], day[0]);
                dp.show();
            }
        });
    }

    public void getText()
    {
        //TEXT
        TextView textView = (TextView) findViewById(R.id.section_text3);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        editText = (EditText) findViewById(R.id.etWidth1);
        if (mode==2) editText.setText(currentTask.get(3));
        assert editText != null;

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                editText.setCursorVisible(true);
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public ArrayList<String> removeRepeatSubject()
    {
        ArrayList<String> tmp = new ArrayList<>();

        for (int i=0;i<ListOfSubject.size();i+=16)
        {
            ListOfSubject.remove(i);
            i--;
        }

        for (int i=0;i<ListOfSubject.size();i+=3) tmp.add(ListOfSubject.get(i));

        for (int i=0;i<tmp.size()-1;i++)
        {
            for (int j=i+1; j<tmp.size();j++)
            {
                if (tmp.get(i).contains(tmp.get(j)))
                {
                    tmp.remove(j);
                    j--;
                }
            }
        }
        tmp.remove("");
        return tmp;
    }

    public void saveTask()
    {
        title=editText.getText().toString();
        if (title.isEmpty()) Toast.makeText(this, "Відсутній опис завдання!", Toast.LENGTH_LONG).show();
        else{
            scheduleNotification(getNotification());

            ArrayList<String> task=GroupActivity.restoreArrayListFromSP(this,"listOfTasks");
            task.add(group);
            task.add(subject);
            task.add(String.valueOf(targetDayInMillis));
            task.add(title);
            task.add(String.valueOf(currentNotificationId));
            GroupActivity.saveArrayListToSP(this,task,"listOfTasks");
            Toast.makeText(this, "Завдання будо успішно збережено!", Toast.LENGTH_LONG).show();

            Intent intent=new Intent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    public void editTask()
    {
        title=editText.getText().toString();
        if (title.isEmpty()) Toast.makeText(this, "Відсутній опис завдання!", Toast.LENGTH_LONG).show();
        else{
            scheduleNotification(getNotification());

            int position = getIntent().getIntExtra("position", 0);
            ArrayList<String> task=GroupActivity.restoreArrayListFromSP(this,"listOfTasks");
            task.set(position*5,group);
            task.set(position*5+1,subject);
            task.set(position*5+2,String.valueOf(targetDayInMillis));
            task.set(position*5+3,title);
            task.set(position*5+4, String.valueOf(currentNotificationId));
            GroupActivity.saveArrayListToSP(this,task,"listOfTasks");

            Intent intent=new Intent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    private void scheduleNotification(Notification notification) {

        if (mode==1)
        {
            SharedPreferences prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
            currentNotificationId = prefs.getLong("maxNotificationId", 0);
            currentNotificationId++;
            prefs.edit().putLong("maxNotificationId", currentNotificationId).commit();
        }

        if (mode==2) currentNotificationId = Long.parseLong(currentTask.get(4));

        Intent notificationIntent = new Intent(this, AlarmAdapter.class);
        notificationIntent.putExtra(AlarmAdapter.NOTIFICATION_ID, currentNotificationId);
        notificationIntent.putExtra(AlarmAdapter.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = previousDayInMillis + 60000;

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Завдання" + " до " + displayDate);
        builder.setContentText(title);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            new Notification.BigTextStyle(builder).bigText(title);
            return builder.build();
        }
        else return builder.getNotification();

    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            finish();
        }
        else { Toast.makeText(getBaseContext(), "Натисніть ще раз для виходу без збереження змін!", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_new_task, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.apply:
                if(mode==1) saveTask();
                if(mode==2) editTask();
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
