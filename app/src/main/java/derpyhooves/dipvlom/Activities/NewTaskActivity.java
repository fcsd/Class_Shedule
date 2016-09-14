package derpyhooves.dipvlom.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import java.util.Locale;
import java.util.logging.Handler;

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


        Date jud = null;
        try {
            jud = new SimpleDateFormat("yyyy MM dd").parse(Integer.toString(year[0]) + " " + Integer.toString(month[0] + 1) + " " + Integer.toString(day[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        displayDate = DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("uk", "UA")).format(jud);


        final TextView datePicker = (TextView) findViewById(R.id.clickable_calendar);
        assert datePicker != null;
        if(mode==1) datePicker.setText("Виконати до " + displayDate);
        if(mode==2) datePicker.setText(currentTask.get(2));

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
                    String [] replaceDate = currentTask.get(2).split(" ");
                    day[0] = Integer.parseInt(replaceDate[2]);
                    month[0] = getSavedMonth(replaceDate[3]);
                    year[0] = Integer.parseInt(replaceDate[4]);
                    isFirstOpenDatePicker = false;
                }

                DatePickerDialog dp = new DatePickerDialog(NewTaskActivity.this, myDateListener, year[0], month[0], day[0]);
                dp.show();
            }
        });
    }

    public int getSavedMonth(String month)
    {
        int Int_month = -1;
        switch (month)
        {
            case "січня":
                Int_month=0;
                break;

            case "лютого":
                Int_month=1;
                break;

            case "березня":
                Int_month=2;
                break;

            case "квітня":
                Int_month=3;
                break;

            case "травня":
                Int_month=4;
                break;

            case "червня":
                Int_month=5;
                break;

            case "липня":
                Int_month=6;
                break;

            case "серпня":
                Int_month=7;
                break;

            case "вересня":
                Int_month=8;
                break;

            case "жовтня":
                Int_month=9;
                break;

            case "листопада":
                Int_month=10;
                break;

            case "грудня":
                Int_month=11;
                break;
        }
        return Int_month;
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
            ArrayList<String> task=GroupActivity.restoreArrayListFromSP(this,"listOfTasks");
            task.add(group);
            task.add(subject);
            task.add("Виконати до " + displayDate);
            task.add(title);
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
            int position = getIntent().getIntExtra("position", 0);
            ArrayList<String> task=GroupActivity.restoreArrayListFromSP(this,"listOfTasks");
            task.set(position*4,group);
            task.set(position*4+1,subject);
            task.set(position*4+2,"Виконати до " + displayDate);
            task.set(position*4+3,title);
            GroupActivity.saveArrayListToSP(this,task,"listOfTasks");

            Intent intent=new Intent();
            setResult(RESULT_OK, intent);

            finish();
        }
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
