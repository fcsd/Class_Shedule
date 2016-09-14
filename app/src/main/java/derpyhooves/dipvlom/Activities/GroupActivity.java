package derpyhooves.dipvlom.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.Adapters.SectionedRecyclerViewAdapter;
import derpyhooves.dipvlom.Adapters.jsoupAdapter;
import derpyhooves.dipvlom.R;

public class GroupActivity extends AppCompatActivity implements RecyclerAdapter.MyClickListenerGA,
        NavigationView.OnNavigationItemSelectedListener, jsoupAdapter.AsyncResponse {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String URLS[];
    private ArrayList<ArrayList<String>> GroupLinks = new ArrayList<>();
    private ArrayList<ArrayList<String>> GroupNames = new ArrayList<>();
    private Map<Integer, ArrayList<String>> map = new HashMap<>();

    private ArrayList<String> getNames = new ArrayList<>();
    private ArrayList<String> getLinks = new ArrayList<>();

    private ArrayList[] savedNames = new ArrayList[19];
    private ArrayList[] savedLinks = new ArrayList[19];

    private ArrayList<Integer> view = new ArrayList<>();

    DrawerLayout drawer;

    private ProgressDialog dialog;

    SectionedRecyclerViewAdapter mSectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Групи");
        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view2);

        // используем linear layout manager
        mRecyclerView.setItemViewCacheSize(0);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        int position = getIntent().getIntExtra("position", 0);

        SharedPreferences prefs = this.getSharedPreferences("YourApp", Context.MODE_PRIVATE);
        if (prefs.contains(String.valueOf(position) + "_size")) {

            savedNames[position] = restoreArrayListFromSP(getApplicationContext(), String.valueOf(position));
            savedLinks[position] = restoreArrayListFromSP(getApplicationContext(), String.valueOf(position + 50));
            getLinks = savedLinks[position];
            getNames = savedNames[position];

            mAdapter = new RecyclerAdapter(getApplicationContext(), this, savedNames[position]);

            //This is the code to provide a sectioned list
            List<SectionedRecyclerViewAdapter.Section> sections =
                    new ArrayList<SectionedRecyclerViewAdapter.Section>();

            ArrayList<Integer> indexOfCourses = getCourses(savedNames[position]);

            String title[] = new String[]{"Перший курс", "Другий курс", "Третій курс", "Четвертий курс", "П'ятий курс", "Шостий курс"};
            for (int i = 0; i < indexOfCourses.size(); i++)
                sections.add(new SectionedRecyclerViewAdapter.Section(indexOfCourses.get(i), title[view.get(i)]));

            //Add your adapter to the sectionAdapter
            SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
            mSectionedAdapter = new SectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            //Apply this adapter to the RecyclerView
            mRecyclerView.setAdapter(mSectionedAdapter);

        } else { updateGroup();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.update:
                updateGroup();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateGroup()
    {
        int position = getIntent().getIntExtra("position", 0);

        // вырисовка групп для всех факультетов
        if (GroupActivity.hasConnection(getApplicationContext())) {

            switch (position) {
                // Автоматики та приладобудування
                case 0:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/10/", "http://schedule.kpi.kharkov.ua/Groups/2/10/", "http://schedule.kpi.kharkov.ua/Groups/3/10/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/10/", "http://schedule.kpi.kharkov.ua/Groups/5/10/", "http://schedule.kpi.kharkov.ua/Groups/6/10/"};

                    showGroups();
                    break;

                // Бізнес та фінанси
                case 1:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/41/", "http://schedule.kpi.kharkov.ua/Groups/2/41/", "http://schedule.kpi.kharkov.ua/Groups/3/41/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/41/", "http://schedule.kpi.kharkov.ua/Groups/5/41/", "http://schedule.kpi.kharkov.ua/Groups/6/41/"};

                    showGroups();
                    break;


                // Економічний
                case 2:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/5/", "http://schedule.kpi.kharkov.ua/Groups/2/5/", "http://schedule.kpi.kharkov.ua/Groups/3/5/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/5/", "http://schedule.kpi.kharkov.ua/Groups/5/5/", "http://schedule.kpi.kharkov.ua/Groups/6/5/"};

                    showGroups();
                    break;


                // Економічної інформатики та менеджменту
                case 3:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/6/", "http://schedule.kpi.kharkov.ua/Groups/2/6/", "http://schedule.kpi.kharkov.ua/Groups/3/6/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/6/", "http://schedule.kpi.kharkov.ua/Groups/5/6/", "http://schedule.kpi.kharkov.ua/Groups/6/6/"};

                    showGroups();
                    break;


                // Електроенергетичний
                case 4:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/11/", "http://schedule.kpi.kharkov.ua/Groups/2/11/", "http://schedule.kpi.kharkov.ua/Groups/3/11/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/11/", "http://schedule.kpi.kharkov.ua/Groups/5/11/", "http://schedule.kpi.kharkov.ua/Groups/6/11/"};

                    showGroups();
                    break;


                // Електромашинобудівний
                case 5:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/9/", "http://schedule.kpi.kharkov.ua/Groups/2/9/", "http://schedule.kpi.kharkov.ua/Groups/3/9/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/9/", "http://schedule.kpi.kharkov.ua/Groups/5/9/", "http://schedule.kpi.kharkov.ua/Groups/6/9/"};

                    showGroups();
                    break;

                // Енергомашинобудівний
                case 6:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/3/", "http://schedule.kpi.kharkov.ua/Groups/2/3/", "http://schedule.kpi.kharkov.ua/Groups/3/3/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/3/", "http://schedule.kpi.kharkov.ua/Groups/5/3/", "http://schedule.kpi.kharkov.ua/Groups/6/3/"};

                    showGroups();
                    break;

                // Інженерно-фізичний
                case 7:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/7/", "http://schedule.kpi.kharkov.ua/Groups/2/7/", "http://schedule.kpi.kharkov.ua/Groups/3/7/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/7/", "http://schedule.kpi.kharkov.ua/Groups/5/7/", "http://schedule.kpi.kharkov.ua/Groups/6/7/"};

                    showGroups();
                    break;

                // Інтегральної підготовки
                case 8:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/40/", "http://schedule.kpi.kharkov.ua/Groups/2/40/", "http://schedule.kpi.kharkov.ua/Groups/3/40/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/40/", "http://schedule.kpi.kharkov.ua/Groups/5/40/", "http://schedule.kpi.kharkov.ua/Groups/6/40/"};

                    showGroups();
                    break;

                // Інтегрованих технологій і хімічної техніки
                case 9:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/14/", "http://schedule.kpi.kharkov.ua/Groups/2/14/", "http://schedule.kpi.kharkov.ua/Groups/3/14/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/14/", "http://schedule.kpi.kharkov.ua/Groups/5/14/", "http://schedule.kpi.kharkov.ua/Groups/6/14/"};

                    showGroups();
                    break;

                // Інформатики і управління
                case 10:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/18/", "http://schedule.kpi.kharkov.ua/Groups/2/18/", "http://schedule.kpi.kharkov.ua/Groups/3/18/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/18/", "http://schedule.kpi.kharkov.ua/Groups/5/18/", "http://schedule.kpi.kharkov.ua/Groups/6/18/"};

                    showGroups();
                    break;

                // Комп`ютерні та інформаційні технології
                case 11:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/42/", "http://schedule.kpi.kharkov.ua/Groups/2/42/", "http://schedule.kpi.kharkov.ua/Groups/3/42/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/42/", "http://schedule.kpi.kharkov.ua/Groups/5/42/", "http://schedule.kpi.kharkov.ua/Groups/6/42/"};

                    showGroups();
                    break;

                // Машинобудівний
                case 12:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/2/", "http://schedule.kpi.kharkov.ua/Groups/2/2/", "http://schedule.kpi.kharkov.ua/Groups/3/2/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/2/", "http://schedule.kpi.kharkov.ua/Groups/5/2/", "http://schedule.kpi.kharkov.ua/Groups/6/2/"};

                    showGroups();
                    break;

                // Механіко-технологічний
                case 13:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/1/", "http://schedule.kpi.kharkov.ua/Groups/2/1/", "http://schedule.kpi.kharkov.ua/Groups/3/1/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/1/", "http://schedule.kpi.kharkov.ua/Groups/5/1/", "http://schedule.kpi.kharkov.ua/Groups/6/1/"};

                    showGroups();
                    break;

                // Німецький технічний
                case 14:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/43/", "http://schedule.kpi.kharkov.ua/Groups/2/43/", "http://schedule.kpi.kharkov.ua/Groups/3/43/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/43/", "http://schedule.kpi.kharkov.ua/Groups/5/43/", "http://schedule.kpi.kharkov.ua/Groups/6/43/"};

                    showGroups();
                    break;

                // Технології неорганічних речовин
                case 15:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/12/", "http://schedule.kpi.kharkov.ua/Groups/2/12/", "http://schedule.kpi.kharkov.ua/Groups/3/12/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/12/", "http://schedule.kpi.kharkov.ua/Groups/5/12/", "http://schedule.kpi.kharkov.ua/Groups/6/12/"};

                    showGroups();
                    break;

                // Технології органічних речовин
                case 16:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/13/", "http://schedule.kpi.kharkov.ua/Groups/2/13/", "http://schedule.kpi.kharkov.ua/Groups/3/13/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/13/", "http://schedule.kpi.kharkov.ua/Groups/5/13/", "http://schedule.kpi.kharkov.ua/Groups/6/13/"};

                    showGroups();
                    break;

                // Транспортного машинобудування
                case 17:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/4/", "http://schedule.kpi.kharkov.ua/Groups/2/4/", "http://schedule.kpi.kharkov.ua/Groups/3/4/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/4/", "http://schedule.kpi.kharkov.ua/Groups/5/4/", "http://schedule.kpi.kharkov.ua/Groups/6/4/"};

                    showGroups();
                    break;

                // Фізико-технічний
                case 18:

                    URLS = new String[]{"http://schedule.kpi.kharkov.ua/Groups/1/8/", "http://schedule.kpi.kharkov.ua/Groups/2/8/", "http://schedule.kpi.kharkov.ua/Groups/3/8/",
                            "http://schedule.kpi.kharkov.ua/Groups/4/8/", "http://schedule.kpi.kharkov.ua/Groups/5/8/", "http://schedule.kpi.kharkov.ua/Groups/6/8/"};

                    showGroups();
                    break;
            }

        } else {
            Toast.makeText(this, "Немає з'єднання з интернетом!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // проверка интернет-соединения
    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void saveArrayListToSP(Context context, ArrayList<String> list, String prefix)
    {
        SharedPreferences prefs = context.getSharedPreferences("YourApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int size = prefs.getInt(prefix+"_size", 0);

        // clear the previous data if exists
        for(int i=0; i<size; i++)
            editor.remove(prefix+"_"+i);

        // write the current list
        for(int i=0; i<list.size(); i++)
            editor.putString(prefix+"_"+i, list.get(i));

        editor.putInt(prefix+"_size", list.size());
        editor.commit();
    }

    public static ArrayList<String> restoreArrayListFromSP (Context context, String prefix)
    {
        SharedPreferences prefs = context.getSharedPreferences("YourApp", Context.MODE_PRIVATE);

        int size = prefs.getInt(prefix+"_size", 0);

        ArrayList<String> data = new ArrayList<>(size);
        for(int i=0; i<size; i++)
            data.add(prefs.getString(prefix+"_"+i, null));

        return data;
    }


    public void showGroups () {

        GroupLinks.clear();
        GroupNames.clear();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Завантаження...");
        dialog.show();

        for (int i = 0; i < 6; i++) {
            jsoupAdapter mt = new jsoupAdapter(URLS[i], 1, this, this);
            mt.execute();
        }
    }



    public ArrayList<Integer> getCourses(ArrayList<String> data)
    {
        String buf = data.get(0).toString();
        String tmp[]=new String[2];
        String[] s = buf.split("-");
        tmp[0]= String.valueOf(s[1].charAt(1));
        ArrayList<Integer> indexOfCourses = new ArrayList<>();
        indexOfCourses.add(0);

        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        currentYear = currentYear % 10;
        int currentCourse;
        view.clear();
        view.add(0);

        for (int i=0; i<data.size(); i++)
        {
            buf=data.get(i).toString();
            s = buf.split("-");

            Pattern p = Pattern.compile("[\\p{L}]", Pattern.UNICODE_CASE);
            Matcher m = p.matcher(s[1]);

            while (m.find()) s[1]=s[1].substring(0,s[1].length()-1);

            if (s[1].length()>2) tmp[1]= String.valueOf(s[1].charAt(2));
            else tmp[1]= String.valueOf(s[1].charAt(1));

            if ((tmp[0].compareTo(tmp[1]))==0) continue;
            tmp[0]=tmp[1];
            indexOfCourses.add(i);

            int yearAdmision = Integer.parseInt(tmp[1]);
            if (currentYear < yearAdmision) currentYear+=10;
            currentCourse=currentYear-yearAdmision-1;
            if (currentMonth>=8) currentCourse++;
            view.add(currentCourse);
        }

        return indexOfCourses;
    }


    @Override
    public void onItemClick(int position) {

        position=mSectionedAdapter.sectionedPositionToPosition(position);
        String link;
        String group;
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        link=getLinks.get(position);
        group=getNames.get(position);
        intent.putExtra("link", link);
        intent.putExtra("group", group);
        intent.putExtra("mode", 1);

        startActivity(intent);

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

    @Override
    public void processFinish(Map<Integer, ArrayList<String>> map) {

        int position = getIntent().getIntExtra("position", 0);

        SharedPreferences prefs = this.getSharedPreferences("YourApp", Context.MODE_PRIVATE);
        int count_processFinish=prefs.getInt("count_processFinish",0);
        count_processFinish++;

        GroupLinks.add(map.get(1));
        GroupNames.add(map.get(2));

        getNames.clear();
        getLinks.clear();

        for (int i = 0; i < GroupNames.size(); i++) {
            for (int j = 0; j < GroupNames.get(i).size(); j++) {
                getNames.add(GroupNames.get(i).get(j));
                getLinks.add(GroupLinks.get(i).get(j));
            }
        }

        if (!getNames.isEmpty()) {
            saveArrayListToSP(getApplicationContext(), getNames, String.valueOf(position));
            saveArrayListToSP(getApplicationContext(), getLinks, String.valueOf(position + 50));

            mAdapter = new RecyclerAdapter(getApplicationContext(), this, getNames);
            List<SectionedRecyclerViewAdapter.Section> sections =
                    new ArrayList<SectionedRecyclerViewAdapter.Section>();

            ArrayList<Integer> indexOfCourses = getCourses(getNames);

            String title[] = new String[]{"Перший курс", "Другий курс", "Третій курс", "Четвертий курс", "П'ятий курс", "Шостий курс"};
            for (int i = 0; i < indexOfCourses.size(); i++)
                sections.add(new SectionedRecyclerViewAdapter.Section(indexOfCourses.get(i), title[view.get(i)]));

            //Add your adapter to the sectionAdapter
            SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
            mSectionedAdapter = new SectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            //Apply this adapter to the RecyclerView
            mRecyclerView.setAdapter(mSectionedAdapter);

            if (count_processFinish==6)
            {
                dialog.dismiss();
                prefs.edit().putInt("count_processFinish",0);
            }
            else prefs.edit().putInt("count_processFinish",count_processFinish).apply();

        }
        else
        {
            if (count_processFinish==6)
            {
                Toast.makeText(this, "Списку груп для вашого факультету немає!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                prefs.edit().putInt("count_processFinish",0).apply();
            }
            else prefs.edit().putInt("count_processFinish",count_processFinish).apply();
        }
    }
}