package derpyhooves.dipvlom.Activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import derpyhooves.dipvlom.R;

public class NewScheduleActivity extends AppCompatActivity  {

    private ArrayList<String> allSchedule = new ArrayList<>();
    private ArrayList<String> ListOfSubject = new ArrayList<>();
    private int WeekPosition;
    private int TimePosition;
    private int DayPosition;
    private String Subject;
    private String group;
    private String House = "";
    private String type;
    private String teacher;
    private ArrayList<String> textTimeFor1 = new ArrayList<>();
    private ArrayList<String> listOfSavedKathedras = new ArrayList<>();

    private boolean isNewSubjectAdd = false;
    private boolean isFirstCallGetSubject = true;
    private boolean isFirstCallGetHouse = true;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private int countSave = 0;
    private int countDelete = 0;
    private int offset = 0;

    private ScrollView EditShedule;
    private ScrollView DeleteShedule;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        EditShedule = (ScrollView) findViewById(R.id.new_schedule);
        DeleteShedule = (ScrollView) findViewById(R.id.delete_schedule);
        DeleteShedule.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner mSpinner = new Spinner(getSupportActionBar().getThemedContext());
        String[] frags = getResources().getStringArray(R.array.modeForEditShedule);

        ArrayAdapter mAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_dropdown_item, R.id.text1, frags);

        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        toolbar.addView(mSpinner);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        allSchedule = getIntent().getStringArrayListExtra("allSchedule");
        group = getIntent().getStringExtra("group");

        ListOfSubject = GroupActivity.restoreArrayListFromSP(this, group);
        ListOfSubject = removeRepeatSubject();
        ListOfSubject.add(0, "Додати...");

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:

                        EditShedule.setVisibility(View.VISIBLE);
                        DeleteShedule.setVisibility(View.GONE);
                        ifEditSchedule();
                        break;

                    case 1:
                        DeleteShedule.setVisibility(View.VISIBLE);
                        EditShedule.setVisibility(View.GONE);
                        ifDeleteSchedule();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void ifEditSchedule() {
        getWeek(0);
        getTime(0);
        getDay(0);
        getSubject();
        getTeacher();
        getHouse();
        getType();
        getText();

        final Button button = (Button) findViewById(R.id.temp_save);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                temp_result();
            }
        });

    }

    private void ifDeleteSchedule() {
        getWeek(1);
        getDay(1);
        isFirstCallGetHouse=true;

        final Button button = (Button) findViewById(R.id.temp_delete);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete_temp();
            }
        });

    }

    private void temp_result() {

        String title=editText.getText().toString();
        String allHouse;

        if (title.isEmpty())
            Toast.makeText(this, "Не була введена аудиторія. Проміжний розклад не буде збережений!", Toast.LENGTH_LONG).show();

        else {

            allHouse = title + " " + House;
            switch (WeekPosition) {
                case 0: // обе недели
                    allSchedule.set(TimePosition * 21, toNormalTime(TimePosition)); // установка времени
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 1, Subject); // установка предмета
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 2, allHouse); // установка аудитории
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 3, type); // установка типа занятий
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 4, teacher); // установка преподавателя

                    allSchedule.set(126 + TimePosition * 21, toNormalTime(TimePosition)); // установка времени
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 1, Subject); // установка предмета
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 2, allHouse); // установка аудитории
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 3, type); // установка типа занятий
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 4, teacher); // установка преподавателя

                    break;
                case 1: // первая неделя
                    allSchedule.set(TimePosition * 21, toNormalTime(TimePosition)); // установка времени
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 1, Subject); // установка предмета
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 2, allHouse); // установка аудитории
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 3, type); // установка типа занятий
                    allSchedule.set(TimePosition * 21 + DayPosition * 4 + 4, teacher); // установка преподавателя
                    break;
                case 2: // вторая неделя
                    allSchedule.set(126 + TimePosition * 21, toNormalTime(TimePosition)); // установка времени
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 1, Subject); // установка предмета
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 2, allHouse); // установка аудитории
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 3, type); // установка типа занятий
                    allSchedule.set(126 + TimePosition * 21 + DayPosition * 4 + 4, teacher); // установка преподавателя
                    break;
            }
            countSave++;
            Toast.makeText(getBaseContext(), "Збережено " + countSave + " проміжних розкладів. Для остаточного збереження натисніть галочку у верхньом правому вікні!", Toast.LENGTH_SHORT).show();

        }
    }

    private void delete_temp()
    {
        int startIndexFirstWeek, startIndexSecondWeek;
        switch (WeekPosition)
        {
            case 0:
                startIndexFirstWeek=TimePosition*21+DayPosition*4+1;
                startIndexSecondWeek=TimePosition*21+DayPosition*4+127;
                for (int i=startIndexFirstWeek;i<startIndexFirstWeek+4;i++) allSchedule.set(i,"");
                for (int i=startIndexSecondWeek;i<startIndexSecondWeek+4;i++) allSchedule.set(i,"");
                break;

            case 1:
                startIndexFirstWeek=TimePosition*21+DayPosition*4+1;
                for (int i=startIndexFirstWeek;i<startIndexFirstWeek+4;i++) allSchedule.set(i,"");
                break;

            case 2:
                startIndexSecondWeek=TimePosition*21+DayPosition*4+127;
                for (int i=startIndexSecondWeek;i<startIndexSecondWeek+4;i++) allSchedule.set(i,"");
                break;
        }

        countDelete++;
        Toast.makeText(getBaseContext(), "Видалено " + countDelete + " проміжних розкладів. Для остаточного збереження натисніть галочку у верхньом правому вікні!", Toast.LENGTH_SHORT).show();
        getTime(1);
    }

    private String toNormalTime(int time) {
        String normalTime = "";
        switch (time) {
            case 0:
                normalTime = "8.30 — 10.05";
                break;
            case 1:
                normalTime = "10.25 — 12.00";
                break;
            case 2:
                normalTime = "12.35 — 14.10";
                break;
            case 3:
                normalTime = "14.30 — 16.05";
                break;
            case 4:
                normalTime = "16.25 — 18.00";
                break;
            case 5:
                normalTime = "18.10 — 19.45";
                break;
        }
        return normalTime;
    }

    private int positionTime (String time)
    {
        int positionTime=-1;
        switch (time)
        {
            case "Перша пара":
                positionTime=0;
                break;

            case "Друга пара":
                positionTime=1;
                break;

            case "Третя пара":
                positionTime=2;
                break;

            case "Четверта пара":
                positionTime=3;
                break;

            case "П'ята пара":
                positionTime=4;
                break;

            case "Шоста пара":
                positionTime=5;
                break;
        }
        return positionTime;
    }

    public void getWeek(final int mode) {
        TextView textView = null;
        if (mode==0) textView = (TextView) findViewById(R.id.section_text);
        if (mode==1) textView = (TextView) findViewById(R.id.section_text10);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ArrayList<String> textWeek = new ArrayList<>();
        textWeek.add("Обидва тижні");
        textWeek.add("1-ий тиждень");
        textWeek.add("2-ий тиждень");

        //SPINNER FOR WEEK
        ArrayAdapter<String> adapterForGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textWeek);
        adapterForGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner forWeek = null;
        if (mode == 0) forWeek = (Spinner) findViewById(R.id.spinner);
        if (mode == 1) forWeek = (Spinner) findViewById(R.id.spinner10);

        forWeek.setAdapter(adapterForGroup);
        // выделяем элемент
        forWeek.setSelection(textWeek.indexOf("Обидва тижні"));
        // устанавливаем обработчик нажатия
        forWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                WeekPosition = position;
                if (mode==1) getTime(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void getTime(final int mode) {

        TextView textView = null;
        if (mode==0) textView = (TextView) findViewById(R.id.section_text2);
        if (mode==1) textView = (TextView) findViewById(R.id.section_text12);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Spinner forTime = null;

        if (mode == 0) {

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

            forTime = (Spinner) findViewById(R.id.spinner2);
            forTime.setAdapter(adapterForGroup);

        }

        if (mode == 1) {

            ArrayList<String> currentTime = new ArrayList<>();

            if (DayPosition == 0) currentTime=getCurrentTime(1);
            if (DayPosition == 1) currentTime=getCurrentTime(5);
            if (DayPosition == 2) currentTime=getCurrentTime(9);
            if (DayPosition == 3) currentTime=getCurrentTime(13);
            if (DayPosition == 4) currentTime=getCurrentTime(17);
            textTimeFor1=getNormalTime(currentTime);

            //SPINNER FOR TIME
            ArrayAdapter<String> adapterForGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textTimeFor1);
            adapterForGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            forTime = (Spinner) findViewById(R.id.spinner12);
            forTime.setAdapter(adapterForGroup);

            }

        // устанавливаем обработчик нажатия
        assert forTime != null;
        forTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (mode==0) TimePosition = position;
                if (mode==1) TimePosition = positionTime(textTimeFor1.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        }

    public ArrayList<String> getCurrentTime(int startIndex) {
        ArrayList<String> currentSchedule = new ArrayList<>();
        ArrayList<String> currentTime = new ArrayList<>();
        if (WeekPosition == 0) for (int i = 0; i < 252; i++) currentSchedule.add(allSchedule.get(i));
        if (WeekPosition == 1) for (int i = 0; i < 126; i++) currentSchedule.add(allSchedule.get(i));
        if (WeekPosition == 2) for (int i = 126; i < 252; i++) currentSchedule.add(allSchedule.get(i));

        for (int i = startIndex; i < currentSchedule.size(); i += 21) {
            if (!currentSchedule.get(i).isEmpty()) currentTime.add(currentSchedule.get(i - startIndex));
        }
        if (WeekPosition == 0) for (int i = currentTime.size() / 2; i < currentTime.size(); i++) {
            currentTime.remove(i);
            i--;
        }

        return currentTime;
    }

    private ArrayList<String> getNormalTime (ArrayList<String> currentTime)
    {
        ArrayList<String> textTime = new ArrayList<>();
        for (int i=0; i<currentTime.size();i++)
        {
            switch (currentTime.get(i))
            {
                case "8.30 — 10.05":
                    textTime.add("Перша пара");
                    break;
                case "10.25 — 12.00":
                    textTime.add("Друга пара");
                    break;
                case "12.35 — 14.10":
                    textTime.add("Третя пара");
                    break;
                case "14.30 — 16.05":
                    textTime.add("Четверта пара");
                    break;
                case "16.25 — 18.00":
                    textTime.add("П'ята пара");
                    break;
                case "18.10 — 19.45":
                    textTime.add("Шоста пара");
                    break;
            }
        }

        return textTime;
    }

    public void getTeacher()
    {
        TextView textView = (TextView) findViewById(R.id.section_text15);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        listOfSavedKathedras = GroupActivity.restoreArrayListFromSP(getApplicationContext(), "savedKathedras");
        listOfSavedKathedras.add(0, "Додати...");
        listOfSavedKathedras.add(0, "-");

        final ArrayList <String> buffer = new ArrayList<>();
        buffer.addAll(listOfSavedKathedras);

        //SPINNER FOR TEACHER
        final ArrayAdapter<String> adapterForTeacher = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buffer);
        adapterForTeacher.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forTeacher = (Spinner) findViewById(R.id.spinner15);

        final String[] newTeacher = {""};

        forTeacher.setAdapter(adapterForTeacher);
        forTeacher.setSelection(0);

        // устанавливаем обработчик нажатия
        forTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position==1)
                {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(NewScheduleActivity.this).create();

                    final EditText input = new EditText(NewScheduleActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);

                    alertDialog.setView(input);
                    alertDialog.setTitle("Введіть викладача");
                    alertDialog.setMessage("Введіть прізвище викладача та його ініціали без крапок. Наприклад: Іванов О А");
                    alertDialog.setCanceledOnTouchOutside(false);
                    input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) showKeyboard(v);
                            if (!hasFocus) hideKeyboard(v);
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Додати",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    newTeacher[0] = input.getText().toString();
                                    if (newTeacher[0].isEmpty())
                                        Toast.makeText(getBaseContext(), "Нічого не було введено, викладач не буде доданий!", Toast.LENGTH_SHORT).show();

                                    else
                                    {
                                        teacher=newTeacher[0];
                                        buffer.add(offset+2,newTeacher[0]);
                                        listOfSavedKathedras.add(offset+2,newTeacher[0]);
                                        adapterForTeacher.notifyDataSetChanged();
                                        forTeacher.setSelection(offset+2);
                                        offset++;
                                    }
                                    input.setFocusable(false);
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Відмінити",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        input.setFocusable(false);
                                        dialog.cancel();
                                        forTeacher.setSelection(0);
                                    }
                                });
                    alertDialog.show();
                }

                if (position >= offset + 2)
                {
                    final ArrayList<String> shortTeachersNames;
                    final ArrayList<String> TeacherNames;
                    shortTeachersNames = GroupActivity.restoreArrayListFromSP(getApplicationContext(),listOfSavedKathedras.get(position)+"short");
                    TeacherNames = GroupActivity.restoreArrayListFromSP(getApplicationContext(),listOfSavedKathedras.get(position));
                    TeacherNames.add(0, "Назад...");

                    buffer.clear();
                    buffer.addAll(TeacherNames);
                    adapterForTeacher.notifyDataSetChanged();
                    forTeacher.setSelection(1);

                    // устанавливаем обработчик нажатия
                    forTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, final View view,
                                                   final int positionSecond, long id) {

                            if (positionSecond==0)
                            {
                                buffer.clear();
                                buffer.addAll(listOfSavedKathedras);
                                adapterForTeacher.notifyDataSetChanged();
                                forTeacher.setSelection(0);
                                getTeacher();
                            }
                            else teacher = shortTeachersNames.get(positionSecond-1);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });
                }
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
        final ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ListOfSubject);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner forSubject = (Spinner) findViewById(R.id.spinner3);

        final int[] positions = new int[2];
        final String[] newSubject = {""};

        forSubject.setAdapter(adapterForSubject);

        // выделяем элемент
        if (ListOfSubject.size()==1) forSubject.setSelection(0);
        else if (!isNewSubjectAdd) forSubject.setSelection(1);
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
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(NewScheduleActivity.this).create();

                    final EditText input = new EditText(NewScheduleActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);

                    alertDialog.setView(input);
                    alertDialog.setTitle("Введіть назву нового предмету");
                    alertDialog.setCanceledOnTouchOutside(false);

                    input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) showKeyboard(v);
                            if (!hasFocus) hideKeyboard(v);
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Додати",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    newSubject[0] = input.getText().toString();
                                    if (newSubject[0].isEmpty())
                                        Toast.makeText(getBaseContext(), "Нічого не було введено, новий предмет не буде доданий!", Toast.LENGTH_SHORT).show();

                                    else{
                                        Subject=newSubject[0];
                                        ListOfSubject.add(Subject);
                                        isNewSubjectAdd =true;
                                        adapterForSubject.notifyDataSetChanged();
                                        forSubject.setSelection(ListOfSubject.size());
                                    }
                                    input.setFocusable(false);
                                }
                            });

                    if (ListOfSubject.size()!=1)
                    {
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Відмінити",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                        input.setFocusable(false);
                                        dialog.cancel();
                                        if(!isFirstCallGetSubject) forSubject.setSelection(positions[1]);
                                        else forSubject.setSelection(1);
                                    }
                                });
                            }
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

        forSubject.setAdapter(adapterForSubject);

        House = getShortHouse(shmi.get(0));

        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view,
                                       final int position, long id) {

                if (isFirstCallGetHouse) isFirstCallGetHouse=false;

                else House = getShortHouse(shmi.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
    }

    public void getText()
    {
        editText = (EditText) findViewById(R.id.etWidth1);
        assert editText != null;

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) hideKeyboard(v);
            }
        });
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        InputMethodManager inputMgr = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
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

    private void getDay(final int mode)
    {
        TextView textView = null;
        if (mode==0) textView = (TextView) findViewById(R.id.section_text6);
        if (mode==1) textView = (TextView) findViewById(R.id.section_text11);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final ArrayList<String> shmi = new ArrayList<String>(Arrays.asList("Понеділок","Вівторок","Середа","Четвер","П'ятниця"));

        //SPINNER FOR DAY
        ArrayAdapter<String> adapterForSubject = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shmi);
        adapterForSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        Spinner forSubject = null;
        if (mode==0) forSubject  = (Spinner) findViewById(R.id.spinner6);
        if (mode==1) forSubject  = (Spinner) findViewById(R.id.spinner11);

        forSubject.setAdapter(adapterForSubject);

        // выделяем элемент
        forSubject.setSelection(0);

        // устанавливаем обработчик нажатия
        forSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                DayPosition=position;
                if (mode==1) getTime(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String getShortHouse(String data)
    {
        String shortName= "";
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

        for (int i=0;i<ListOfSubject.size();i+=21)
        {
            ListOfSubject.remove(i);
            i--;
        }

        for (int i=0;i<ListOfSubject.size();i+=4) tmp.add(ListOfSubject.get(i));

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

                GroupActivity.saveArrayListToSP(this, allSchedule, group);
                Toast.makeText(getBaseContext(), "Збереження успішне!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                this.finish();
                return true;

            case android.R.id.home:
                editText.setFocusable(false);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}