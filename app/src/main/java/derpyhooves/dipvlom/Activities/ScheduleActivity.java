package derpyhooves.dipvlom.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import derpyhooves.dipvlom.Adapters.PagerAdapter;
import derpyhooves.dipvlom.Adapters.jsoupAdapter;
import derpyhooves.dipvlom.Fragments.ScheduleFragment;
import derpyhooves.dipvlom.R;


public class ScheduleActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, jsoupAdapter.AsyncResponse {

    private String group;
    private String link;
    private ArrayList<String> scheduleForFirstWeek = new ArrayList<>();
    private ArrayList<String> scheduleForSecondWeek = new ArrayList<>();
    private ArrayList<String> type = new ArrayList<>();
    private ArrayList<String> allSchedule = new ArrayList<>();

    private ArrayList<String> monday = new ArrayList<>();
    private ArrayList<String> tuesday = new ArrayList<>();
    private ArrayList<String> wednesday = new ArrayList<>();
    private ArrayList<String> thursday = new ArrayList<>();
    private ArrayList<String> friday = new ArrayList<>();

    private boolean isCurrentGroupSaved = false;
    private boolean isUpdateSchedule = false;
    private boolean isFirstCallViewPager = true;
    private boolean isLoadingSucess = true;

    private MenuItem mSaveSchedule;
    private MenuItem mEditSchedule;
    private MenuItem mUpdateSchedule;

    private int indexOfUpdateSchedule;
    private int tabPosition;
    private Spinner mSpinner;

    final int REQUEST_SAVE_NEW_TASK = 1;
    ViewPager viewPager;
    DrawerLayout drawer;

    @Override
    public void processFinish(Map<Integer, ArrayList<String>> map) {
        if (!map.isEmpty())
        {

            scheduleForFirstWeek = map.get(1);
            scheduleForSecondWeek = map.get(2);
            type=map.get(3);

            if (isUpdateSchedule)
            {
                allSchedule.clear();
                allSchedule.addAll(scheduleForFirstWeek);
                allSchedule.addAll(scheduleForSecondWeek);
                GroupActivity.saveArrayListToSP(getApplicationContext(),allSchedule,group);

                ArrayList<String> keysOfSavedScheduleAutumSpring=GroupActivity.restoreArrayListFromSP(this,"typesOfGroup");
                keysOfSavedScheduleAutumSpring.set(indexOfUpdateSchedule, type.get(0));
                GroupActivity.saveArrayListToSP(this,keysOfSavedScheduleAutumSpring,"typesOfGroup");

                isUpdateSchedule = false;
            }

            Calendar c1 = Calendar.getInstance();
            int currentDayOfYear = c1.get(Calendar.DAY_OF_YEAR);
            SharedPreferences prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
            prefs.edit().putInt(group + "ref", currentDayOfYear).apply();
            isLoadingSucess = true;
            invalidateOptionsMenu();

            showSchedule(scheduleForFirstWeek);
            showSchedule(scheduleForSecondWeek);
            setupViewPager();

        }
        else
        {
            Toast.makeText(this, "Під час завантаження зникло з'єднання з інтернетом!", Toast.LENGTH_LONG).show();
            isLoadingSucess = false;
            invalidateOptionsMenu();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        int mode = getIntent().getIntExtra("mode", 0);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        mSpinner = new Spinner(getSupportActionBar().getThemedContext());
        String[] frags = getResources().getStringArray(R.array.weeks);

        ArrayAdapter mAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, R.id.text1, frags);

        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setAdapter(mAdapter);
        toolbar.addView(mSpinner);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isEverydayUpdateEnabled = prefs.getBoolean("ref", false);

        Calendar c1 = Calendar.getInstance();
        int currentDayOfYear = c1.get(Calendar.DAY_OF_YEAR);

        if (mode==1)
        {
            link = getIntent().getStringExtra("link");
            group = getIntent().getStringExtra("group");

            prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
            int lastDayUpdateSchedule = prefs.getInt(group + "ref", 0);

            setTitle(group);
            if(prefs.contains(group+"_size"))
            {
                isCurrentGroupSaved=true;
                if (isEverydayUpdateEnabled && lastDayUpdateSchedule!=currentDayOfYear && GroupActivity.hasConnection(this))
                {
                    isUpdateSchedule = true;
                    everyDayUpdateSchedule();
                }

                else
                {
                    allSchedule=GroupActivity.restoreArrayListFromSP(this,group);
                    for (int i=0; i<allSchedule.size()/2; i++) scheduleForFirstWeek.add(allSchedule.get(i));
                    for (int i=allSchedule.size()/2; i<allSchedule.size(); i++) scheduleForSecondWeek.add(allSchedule.get(i));
                }

            } else updateSchedule();
        }

        if (mode==2)
        {
            isCurrentGroupSaved = true;
            int position = getIntent().getIntExtra("position", 0);
            ArrayList<String> keysOfSavedSchedule = GroupActivity.restoreArrayListFromSP(this, "keysOfGroup");
            group = keysOfSavedSchedule.get(position);
            setTitle(group);

            prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
            int lastDayUpdateSchedule = prefs.getInt(group + "ref", 0);

            if (isEverydayUpdateEnabled && lastDayUpdateSchedule!=currentDayOfYear && GroupActivity.hasConnection(this))
            {
                ArrayList<String> linksOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"linksOfGroup");
                indexOfUpdateSchedule=keysOfSavedSchedule.indexOf(group);
                link= linksOfSavedSchedule.get(indexOfUpdateSchedule);
                isUpdateSchedule = true;
                everyDayUpdateSchedule();
            }
            else
            {
                allSchedule = GroupActivity.restoreArrayListFromSP(this, group);
                scheduleForFirstWeek.addAll((allSchedule.subList(0, allSchedule.size() / 2)));
                scheduleForSecondWeek.addAll((allSchedule.subList(allSchedule.size() / 2, allSchedule.size())));
            }
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                tabPosition = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                tabPosition = arg0;
            }

            @Override
            public void onPageScrollStateChanged(int num) {

            }
        });
    }

    public void updateSchedule()
    {

        if(GroupActivity.hasConnection(this))
        {
            jsoupAdapter mt = new jsoupAdapter(link, 2, this, this, false);
            mt.execute();
        }
        else
        {
            Toast.makeText(this, "Нет соединения с интернетом!", Toast.LENGTH_LONG).show();
            isLoadingSucess = false;
        }
    }


    public void everyDayUpdateSchedule()
    {
        Intent intent = new Intent(getApplicationContext(), jsoupAdapter.class);
        intent.putExtra("everyDayUpdate", true);
        jsoupAdapter mt = new jsoupAdapter(link, 2, this, this, true);
        mt.execute();
    }

    public void showPopup1(){
        View menuItemView = findViewById(R.id.schedule_save);
        PopupMenu popup = new PopupMenu(this, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.activity_schedule_popup1, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.my_schedule_save:
                         saveMySchedule();
                         return true;

                    case R.id.other_schedule_save:
                        saveOtherSchedule();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void showPopup2(){
        View menuItemView = findViewById(R.id.schedule_edit);
        PopupMenu popup = new PopupMenu(this, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.activity_schedule_popup2, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.schedule_delete:
                        deleteSchedule();
                        return true;

                    case R.id.schedule_edit:
                        editSchedule();
                        return true;

                    case R.id.schedule_update:

                        ArrayList<String> keysOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"keysOfGroup");
                        ArrayList<String> linksOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"linksOfGroup");
                        indexOfUpdateSchedule=keysOfSavedSchedule.indexOf(group);
                        link= linksOfSavedSchedule.get(indexOfUpdateSchedule);

                        isUpdateSchedule=true;
                        updateSchedule();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        if (!isLoadingSucess) inflater.inflate(R.menu.activity_group, menu);
        else if (isCurrentGroupSaved) inflater.inflate(R.menu.activity_schedule_edit, menu);
        else inflater.inflate(R.menu.activity_schedule_save, menu);

        mSaveSchedule = menu.findItem(R.id.schedule_save);
        mEditSchedule = menu.findItem(R.id.schedule_edit);
        mUpdateSchedule = menu.findItem(R.id.update);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        if (!isLoadingSucess) mUpdateSchedule.setVisible(true);
        else if (isCurrentGroupSaved) mEditSchedule.setVisible(true);
        else mSaveSchedule.setVisible(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void saveMySchedule()
    {
        ArrayList<String> keysOfSavedSchedule;
        ArrayList<String> keysOfSavedScheduleAutumSpring;
        ArrayList<String> linksOfSavedSchedule;

        allSchedule.clear();
        allSchedule.addAll(scheduleForFirstWeek);
        allSchedule.addAll(scheduleForSecondWeek);
        GroupActivity.saveArrayListToSP(this,allSchedule,group);

        keysOfSavedSchedule=GroupActivity.restoreArrayListFromSP(this,"keysOfGroup");
        keysOfSavedSchedule.add(0,group);
        GroupActivity.saveArrayListToSP(this,keysOfSavedSchedule,"keysOfGroup");

        keysOfSavedScheduleAutumSpring=GroupActivity.restoreArrayListFromSP(this,"typesOfGroup");
        keysOfSavedScheduleAutumSpring.addAll(0,type);
        GroupActivity.saveArrayListToSP(this,keysOfSavedScheduleAutumSpring,"typesOfGroup");

        linksOfSavedSchedule=GroupActivity.restoreArrayListFromSP(this,"linksOfGroup");
        linksOfSavedSchedule.add(0,link);
        GroupActivity.saveArrayListToSP(this,linksOfSavedSchedule,"linksOfGroup");

        SharedPreferences prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isMyGroupSaved", true).apply();
        prefs.edit().putString("myGroup", group).apply();

        isCurrentGroupSaved = true;
        Toast.makeText(this, "Розклад групи "+group+" був збережен як розклад основної групи", Toast.LENGTH_LONG).show();

        Intent intent=new Intent();
        setResult(RESULT_OK, intent);

        invalidateOptionsMenu();
    }

    public void saveOtherSchedule()
    {
        ArrayList<String> linksOfSavedSchedule;
        ArrayList<String> keysOfSavedSchedule;
        ArrayList<String> keysOfSavedScheduleAutumSpring;
        allSchedule.clear();
        allSchedule.addAll(scheduleForFirstWeek);
        allSchedule.addAll(scheduleForSecondWeek);
        GroupActivity.saveArrayListToSP(this,allSchedule,group);

        keysOfSavedSchedule=GroupActivity.restoreArrayListFromSP(this,"keysOfGroup");
        keysOfSavedSchedule.add(group);
        GroupActivity.saveArrayListToSP(this,keysOfSavedSchedule,"keysOfGroup");

        keysOfSavedScheduleAutumSpring=GroupActivity.restoreArrayListFromSP(this,"typesOfGroup");
        keysOfSavedScheduleAutumSpring.addAll(type);
        GroupActivity.saveArrayListToSP(this,keysOfSavedScheduleAutumSpring,"typesOfGroup");

        linksOfSavedSchedule=GroupActivity.restoreArrayListFromSP(this,"linksOfGroup");
        linksOfSavedSchedule.add(link);
        GroupActivity.saveArrayListToSP(this,linksOfSavedSchedule,"linksOfGroup");

        isCurrentGroupSaved = true;
        Toast.makeText(this, "Розклад групи "+group+" був збережен як розклад додаткової групи", Toast.LENGTH_LONG).show();

        Intent intent=new Intent();
        setResult(RESULT_OK, intent);

        invalidateOptionsMenu();

    }


    public void deleteSchedule()
    {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        ArrayList<String> keysOfSavedSchedule;
                        ArrayList<String> keysOfSavedScheduleAutumSpring;
                        ArrayList<String> linksOfSavedSchedule;
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
                        prefs.edit().remove(group+"_size").apply();
                        prefs.edit().remove(group+"ref").apply();

                        keysOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"keysOfGroup");
                        int removeIndex=keysOfSavedSchedule.indexOf(group);
                        keysOfSavedSchedule.remove(group);
                        GroupActivity.saveArrayListToSP(getApplicationContext(),keysOfSavedSchedule,"keysOfGroup");

                        keysOfSavedScheduleAutumSpring=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"typesOfGroup");
                        keysOfSavedScheduleAutumSpring.remove(removeIndex);
                        GroupActivity.saveArrayListToSP(getApplicationContext(),keysOfSavedScheduleAutumSpring,"typesOfGroup");

                        linksOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"linksOfGroup");
                        linksOfSavedSchedule.remove(removeIndex);
                        GroupActivity.saveArrayListToSP(getApplicationContext(),linksOfSavedSchedule,"linksOfGroup");

                        if(group.contains(prefs.getString("myGroup", "defaultStringIfNothingFound"))) prefs.edit().remove("isMyGroupSaved").apply();

                        isCurrentGroupSaved=false;

                        ArrayList<String> tasks = GroupActivity.restoreArrayListFromSP(getApplicationContext(), "listOfTasks");
                        for (int i = 0; i< tasks.size(); i+=5)
                        {
                            if (tasks.get(i).equals(group))
                            {
                                for (int j=0;j<5;j++)
                                {
                                    if (j==4) NewTaskActivity.deleteNotification(getApplicationContext(),Integer.parseInt(tasks.get(i*5)));
                                    tasks.remove(i*5);
                                }
                                i-=5;
                            }
                        }
                        GroupActivity.saveArrayListToSP(getApplicationContext(), tasks,"listOfTasks");
                        Toast.makeText(getApplicationContext(), "Розклад був успішно видален!", Toast.LENGTH_LONG).show();

                        Intent intent=new Intent();
                        setResult(RESULT_OK, intent);

                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ви впевнені, що бажаєте видалити розклад?").setPositiveButton("Так", dialogClickListener)
                .setNegativeButton("Ні", dialogClickListener).setCancelable(false).show();
    }

    private void editSchedule()
    {
        Intent intent = new Intent(getApplicationContext(), NewScheduleActivity.class);
        intent.putExtra("allSchedule", allSchedule);
        intent.putExtra("group",group);
        startActivityForResult(intent,REQUEST_SAVE_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_SAVE_NEW_TASK) {
                scheduleForFirstWeek.clear();
                scheduleForSecondWeek.clear();
                allSchedule=GroupActivity.restoreArrayListFromSP(this,group);
                for (int i=0; i<allSchedule.size()/2; i++) scheduleForFirstWeek.add(allSchedule.get(i));
                for (int i=allSchedule.size()/2; i<allSchedule.size(); i++) scheduleForSecondWeek.add(allSchedule.get(i));
                showSchedule(scheduleForFirstWeek);
                showSchedule(scheduleForSecondWeek);
                setupViewPager();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

             switch (item.getItemId()) {

                 case R.id.schedule_save:
                     showPopup1();
                     return true;

                 case R.id.schedule_edit:
                     showPopup2();
                     return true;

                 case R.id.update:

                     if (isCurrentGroupSaved)
                     {
                         ArrayList<String> keysOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"keysOfGroup");
                         ArrayList<String> linksOfSavedSchedule=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"linksOfGroup");
                         indexOfUpdateSchedule=keysOfSavedSchedule.indexOf(group);
                         link= linksOfSavedSchedule.get(indexOfUpdateSchedule);
                     }

                     isUpdateSchedule=true;
                     updateSchedule();
                     return true;

                 default:
                     return super.onOptionsItemSelected(item);
         }
    }


    private void setupViewPager() {

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Fragment scheduleMonday = new ScheduleFragment(monday,group);
        Fragment scheduleTuesday = new ScheduleFragment(tuesday,group);
        Fragment scheduleWednesday = new ScheduleFragment(wednesday,group);
        Fragment scheduleThursday = new ScheduleFragment(thursday,group);
        Fragment scheduleFriday = new ScheduleFragment(friday,group);

        adapter.addFragment(scheduleMonday, "Понеділок");
        adapter.addFragment(scheduleTuesday, "Вівторок");
        adapter.addFragment(scheduleWednesday, "Середа");
        adapter.addFragment(scheduleThursday, "Четвер");
        adapter.addFragment(scheduleFriday, "П'ятниця");
        viewPager.setAdapter(adapter);

        if (isFirstCallViewPager)
        {
            tabPosition = getCurrentDay();
            isFirstCallViewPager = false;
        }
        viewPager.setCurrentItem(tabPosition);

        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

    }

    public int getCurrentDay()
    {
        Calendar c1 = Calendar.getInstance();
        int dayOfWeek = c1.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek==1 || dayOfWeek==7)
        {
            dayOfWeek=0;
            mSpinner.setSelection((getCurrentWeek()+1)%2);
        }
        else
        {
            dayOfWeek-=2;
            mSpinner.setSelection(getCurrentWeek());
        }

        return dayOfWeek;
    }

    public int getCurrentWeek()
    {
        Calendar c1 = Calendar.getInstance();
        int currentWeek = c1.get(Calendar.WEEK_OF_YEAR);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean value = prefs.getBoolean("inv", false);

        if(value)currentWeek++;
        currentWeek=currentWeek%2;
        return currentWeek;
    }


    public void toNormalForm(ArrayList<String> data) {

        monday.clear();
        tuesday.clear();
        wednesday.clear();
        thursday.clear();
        friday.clear();

        for (int i = 0; i < data.size(); i += 21) {
            monday.add(data.get(i));
            tuesday.add(data.get(i));
            wednesday.add(data.get(i));
            thursday.add(data.get(i));
            friday.add(data.get(i));

            for (int j = i + 1; j < i + 5; j++) {
                monday.add(data.get(j));
                tuesday.add(data.get(j + 4));
                wednesday.add(data.get(j + 8));
                thursday.add(data.get(j + 12));
                friday.add(data.get(j + 16));
            }
        }
    }

    public void showSchedule(ArrayList<String> scheduleWeek) {
        toNormalForm(scheduleWeek);
        removeEmptyStudy(monday);
        removeEmptyStudy(tuesday);
        removeEmptyStudy(wednesday);
        removeEmptyStudy(thursday);
        removeEmptyStudy(friday);

    }

    public void removeEmptyStudy(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i += 5) {
            if (data.get(i + 1) == "") {
                for (int j = 0; j < 5; j++) {
                    data.remove(i);
                }
                i -= 5;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                showSchedule(scheduleForFirstWeek);
                setupViewPager();
                break;

            case 1:
                showSchedule(scheduleForSecondWeek);
                setupViewPager();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_OK, intent);
        int request=-1;

        if (id == R.id.nav_gallery) request=11;
        else if (id == R.id.nav_slideshow) request=12;
        else if (id == R.id.nav_manage) request=13;
        else if (id == R.id.nav_share) request=14;
        else if (id == R.id.nav_send) request=15;

        drawer.closeDrawer(GravityCompat.START);
        intent.putExtra("request", request);
        startActivity(intent);
        return true;
    }
}


