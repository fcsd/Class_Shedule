package derpyhooves.dipvlom.Activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import derpyhooves.dipvlom.R;

public class NewScheduleActivity extends AppCompatActivity {

    private ArrayList<String> allSchedule = new ArrayList<>();
    private ArrayList<String> ListOfSubject = new ArrayList<>();
    private int WeekPosition;
    private int TimePosition;
    private int DayPosition;
    private String Subject;
    private String group;
    private String House;
    private String type;

    private boolean isNewSubjectAdd = false;
    private boolean isFirstCallGetSubject = true;
    private boolean isFirstCallGetHouse = true;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Редагувати розклад");
        allSchedule=getIntent().getStringArrayListExtra("allSchedule");
        group = getIntent().getStringExtra("group");

        ListOfSubject = GroupActivity.restoreArrayListFromSP(this, group);
        ListOfSubject = removeRepeatSubject();
        ListOfSubject.add(0,"Додати...");

        getWeek();
        getTime();
        getDay();
        getSubject();
        getHouse();
        getType();

        final Button button = (Button) findViewById(R.id.temp_save);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                temp_result();
            }
        });

    }

    private void temp_result()
    {
        switch (WeekPosition)
        {
            case 0: // обе недели
                allSchedule.set(TimePosition*16,toNormalTime(TimePosition)); // установка времени
                allSchedule.set(TimePosition*16+DayPosition*3+1,Subject); // установка предмета
                allSchedule.set(TimePosition*16+DayPosition*3+2,House); // установка аудитории
                allSchedule.set(TimePosition*16+DayPosition*3+3,type); // установка типа занятий
                allSchedule.set(96+TimePosition*16,toNormalTime(TimePosition)); // установка времени
                allSchedule.set(96+TimePosition*16+DayPosition*3+1,Subject); // установка предмета
                allSchedule.set(96+TimePosition*16+DayPosition*3+2,House); // установка аудитории
                allSchedule.set(96+TimePosition*16+DayPosition*3+3,type); // установка типа занятий
                break;
            case 1: // первая неделя
                allSchedule.set(TimePosition*16,toNormalTime(TimePosition)); // установка времени
                allSchedule.set(TimePosition*16+DayPosition*3+1,Subject); // установка предмета
                allSchedule.set(TimePosition*16+DayPosition*3+2,House); // установка аудитории
                allSchedule.set(TimePosition*16+DayPosition*3+3,type); // установка типа занятий
                break;
            case 2: // вторая неделя
                allSchedule.set(96+TimePosition*16,toNormalTime(TimePosition)); // установка времени
                allSchedule.set(96+TimePosition*16+DayPosition*3+1,Subject); // установка предмета
                allSchedule.set(96+TimePosition*16+DayPosition*3+2,House); // установка аудитории
                allSchedule.set(96+TimePosition*16+DayPosition*3+3,type); // установка типа занятий
                break;
        }
        count++;
        Toast.makeText(getBaseContext(), "Збережено " + count + " проміжних розкладів. Для остаточного збереження натисніть галочку у верхньом правому вікні!", Toast.LENGTH_SHORT).show();
    }

    private String toNormalTime(int time)
    {
       String normalTime=new String();
       switch(time)
       {
           case 0:
               normalTime="8.30 — 10.05";
               break;
           case 1:
               normalTime="10.25 — 12.00";
               break;
           case 2:
               normalTime="12.35 — 14.10";
               break;
           case 3:
               normalTime="14.30 — 16.05";
               break;
           case 4:
               normalTime="16.25 — 18.00";
               break;
           case 5:
               normalTime="18.10 — 19.45";
               break;
       }
        return normalTime;
    }
    public void getWeek()
    {
        TextView textView = (TextView) findViewById(R.id.section_text);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ArrayList<String> textWeek = new ArrayList<>();
        textWeek.add("Обидва тижні");
        textWeek.add("1-ий тиждень");
        textWeek.add("2-ий тиждень");

        //SPINNER FOR WEEK
        ArrayAdapter<String> adapterForGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textWeek);
        adapterForGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner forWeek = (Spinner) findViewById(R.id.spinner);
        forWeek.setAdapter(adapterForGroup);
        // выделяем элемент
        forWeek.setSelection(textWeek.indexOf("Обидва тижні"));
        // устанавливаем обработчик нажатия
        forWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                WeekPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void getTime()
    {
        TextView textView = (TextView) findViewById(R.id.section_text2);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ArrayList<String> textTime = new ArrayList<>();
        textTime.add("Перша пара");
        textTime.add("Друга пара");
        textTime.add("Третя пара");
        textTime.add("Четверта пара");
        textTime.add("П'ята пара");
        textTime.add("Шоста пара");


        //SPINNER FOR TIME
        ArrayAdapter<String> adapterForGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textTime);
        adapterForGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner forTime = (Spinner) findViewById(R.id.spinner2);
        forTime.setAdapter(adapterForGroup);
        // устанавливаем обработчик нажатия
        forTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                TimePosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void getSubject()
    {
        TextView textView = (TextView) findViewById(R.id.section_text3);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //SPINNER FOR SUBJECT
        ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ListOfSubject);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forSubject = (Spinner) findViewById(R.id.spinner3);

        final int[] positions = new int[2];
        final String[] newSubject = {new String()};

        forSubject.setAdapter(adapterForSubject);

        // выделяем элемент
        if(!isNewSubjectAdd)forSubject.setSelection(1);
        else forSubject.setSelection(ListOfSubject.indexOf(Subject));

        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if(isFirstCallGetSubject)
                {
                    isFirstCallGetSubject = false;
                    positions[0] = position;
                }
                else{
                    positions[1] = positions[0];
                    positions[0] = position;
                }


                if (position==0)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewScheduleActivity.this);

                    final EditText input = new EditText(NewScheduleActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setTitle("Введіть назву нового предмету");

                    alertDialog.setPositiveButton("Додати",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    newSubject[0] = input.getText().toString();
                                    if (newSubject[0].isEmpty())
                                        Toast.makeText(getBaseContext(), "Нічого не було введено, новий предмет не буде доданий!", Toast.LENGTH_SHORT).show();

                                    else{
                                        Subject=newSubject[0];
                                        ListOfSubject.add(Subject);
                                        isNewSubjectAdd =true;
                                        getSubject();
                                    }
                                }
                            });

                    alertDialog.setNegativeButton("Відмінити",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if(!isFirstCallGetSubject) forSubject.setSelection(positions[1]);
                                    else forSubject.setSelection(1);
                                }
                            });

                    alertDialog.show();
                }
                else Subject = ListOfSubject.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }


    public void getHouse()
    {
        TextView textView = (TextView) findViewById(R.id.section_text4);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String[] mDataSet = getResources().getStringArray(R.array.Housing);
        final ArrayList<String> shmi = new ArrayList<>();
        Collections.addAll(shmi, mDataSet);


        //SPINNER FOR HOUSE
        ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shmi);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forSubject = (Spinner) findViewById(R.id.spinner4);

        final String[] newHouse = {new String()};

        forSubject.setAdapter(adapterForSubject);

        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {

                if (isFirstCallGetHouse)
                {
                    isFirstCallGetHouse=false;
                }
                else{

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewScheduleActivity.this);

                    final EditText input = new EditText(NewScheduleActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setTitle("Введіть аудиторію");

                    alertDialog.setPositiveButton("Додати",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    newHouse[0] = input.getText().toString();
                                    if (newHouse[0].isEmpty())
                                        Toast.makeText(getBaseContext(), "Нічого не було введено, аудиторія не буде збережена!", Toast.LENGTH_SHORT).show();

                                    else{
                                        House=newHouse[0]+" "+getShortHouse(shmi.get(position));
                                        int k=100;
                                    }
                                }
                            });

                    alertDialog.setNegativeButton("Відмінити",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    forSubject.setSelection(1);
                                }
                            });

                    alertDialog.show();

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void getType()
    {
        TextView textView = (TextView) findViewById(R.id.section_text5);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final ArrayList<String> shmi = new ArrayList<String>(Arrays.asList("Лекція","Лабораторна","Семінар","Практика"));

        //SPINNER FOR TYPE
        ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shmi);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forSubject = (Spinner) findViewById(R.id.spinner5);

        assert forSubject != null;
        forSubject.setAdapter(adapterForSubject);

        // выделяем элемент
        forSubject.setSelection(0);

        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                type=shmi.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDay()
    {
        TextView textView = (TextView) findViewById(R.id.section_text6);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final ArrayList<String> shmi = new ArrayList<String>(Arrays.asList("Понеділок","Вівторок","Середа","Четвер","П'ятниця"));

        //SPINNER FOR DAY
        ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shmi);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forSubject = (Spinner) findViewById(R.id.spinner6);

        assert forSubject != null;
        forSubject.setAdapter(adapterForSubject);

        // выделяем элемент
        forSubject.setSelection(0);

        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                DayPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public String getShortHouse(String data)
    {
        String shortName=new String();
        switch(data)
        {
            case "Адміністративний корпус":
                shortName="АК";
                break;
            case "Вечірний корпус":
                shortName="ВК";
                break;
            case "Головний аудиторний корпус":
                shortName="ГАК";
                break;
            case "Інженерний корпус":
                shortName="ІК";
                break;
            case "Корпус громадських організацій":
                shortName="КГО";
                break;
            case "Математичний корпус":
                shortName="МК";
                break;
            case "Палац студентів":
                shortName="ПС";
                break;
            case "Радіокорпус":
                shortName="РК";
                break;
            case "Ректорський корпус":
                shortName="ГК";
                break;
            case "Спортивний комлекс":
                shortName="СК";
                break;
            case "Технічний корпус":
                shortName="ТК";
                break;
            case "Український корпус":
                shortName="КУМ";
                break;
            case "Учбовий корпус №1":
                shortName="У1";
                break;
            case "Учбовий корпус №2":
                shortName="У2";
                break;
            case "Учбовий корпус №3":
                shortName="У3";
                break;
            case "Учбовий корпус №4":
                shortName="У4";
                break;
            case "Учбовий корпус №5":
                shortName="У5";
                break;
            case "Фізичний корпус":
                shortName="ФК";
                break;
            case "Хімічний корпус":
                shortName="ХК";
                break;
            case "Електротехнічний корпус":
                shortName="ЕК";
                break;
        }
        return shortName;
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
                GroupActivity.saveArrayListToSP(this,allSchedule,group);
                Toast.makeText(getBaseContext(), "Збереження успішне!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                this.finish();
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}