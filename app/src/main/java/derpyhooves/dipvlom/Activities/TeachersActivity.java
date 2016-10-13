package derpyhooves.dipvlom.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.Adapters.jsoupAdapter;
import derpyhooves.dipvlom.R;

public class TeachersActivity extends AppCompatActivity implements RecyclerAdapter.MyClickListenerGA,
        NavigationView.OnNavigationItemSelectedListener, jsoupAdapter.AsyncResponse{

    private RecyclerView mRecyclerView;
    private android.support.v7.widget.RecyclerView.Adapter mAdapter;

    private int position;
    private String URLS[];
    private String kathedra;
    private ArrayList<String> TeacherLinks = new ArrayList<>();
    private ArrayList<String> TeacherNames = new ArrayList<>();

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);

        position = getIntent().getIntExtra("position", 0);
        kathedra = getIntent().getStringExtra("kathedra");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(kathedra);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer8);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);

        String shmi=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(kathedra, "defaultStringIfNothingFound");
        if (shmi.equals(kathedra))
        {
            TeacherNames = GroupActivity.restoreArrayListFromSP(getApplicationContext(), String.valueOf(position + 1000));
            TeacherLinks = GroupActivity.restoreArrayListFromSP(getApplicationContext(), String.valueOf(position + 1050));

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new RecyclerAdapter(getApplicationContext(),this,TeacherNames);
            mRecyclerView.setAdapter(mAdapter);
        }

        else updateTeachers();


    }

    public void updateTeachers() {

        if (GroupActivity.hasConnection(getApplicationContext())) {

            switch (position) {
                case 0:
                    URLS = new String[]{"http://web.kpi.kharkov.ua/otp/uk/lecturers/"};
                    showTeachers();
                    break;
            }
        }
        else Toast.makeText(this, "Немає з'єднання з інернетом!", Toast.LENGTH_LONG).show();
    }

    public void showTeachers()
    {
        jsoupAdapter mt = new jsoupAdapter(URLS[0], 3, this, this);
        mt.execute();
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), InfoTeacherActivity.class);
        intent.putExtra("TeacherName", TeacherNames.get(position));
        intent.putExtra("TeacherLink", TeacherLinks.get(position));
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

        if (!map.isEmpty())
        {
            TeacherLinks.clear();
            TeacherNames.clear();

            TeacherLinks=map.get(1);
            TeacherNames=map.get(2);

            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(kathedra, kathedra).commit();
            GroupActivity.saveArrayListToSP(getApplicationContext(), TeacherNames, String.valueOf(position + 1000));
            GroupActivity.saveArrayListToSP(getApplicationContext(), TeacherLinks, String.valueOf(position + 1050));

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new RecyclerAdapter(getApplicationContext(),this,TeacherNames);
            mRecyclerView.setAdapter(mAdapter);
        }
        else Toast.makeText(this, "Під час завантаження зникло з'єднання з інтернетом!", Toast.LENGTH_LONG).show();

    }
}
