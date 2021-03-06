package derpyhooves.dipvlom.Activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import derpyhooves.dipvlom.Fragments.HousingFragment;
import derpyhooves.dipvlom.Fragments.InfoFragment;
import derpyhooves.dipvlom.Fragments.MainFragment;
import derpyhooves.dipvlom.Fragments.PreferencesFragment;
import derpyhooves.dipvlom.Fragments.TasksFragment;
import derpyhooves.dipvlom.Fragments.KathedralFragment;
import derpyhooves.dipvlom.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private GoogleApiClient client;
    public DrawerLayout drawer;

    MainFragment mf = new MainFragment();
    TasksFragment tf = new TasksFragment();
    HousingFragment hf = new HousingFragment();
    KathedralFragment kf = new KathedralFragment();
    PreferencesFragment khf = new PreferencesFragment();
    InfoFragment inf = new InfoFragment();

    final int REQUEST_SAVE_NEW_TASK = 1;
    final int REQUEST_SAVE_NEW_SCHEDULE = 2;
    final int REQUEST_LAUNCH_KATHEDRAL_FRAGMENT = 11;
    final int REQUEST_LAUNCH_TASKS_FRAGMENT = 12;
    final int REQUEST_LAUNCH_HOUSING_FRAGMENT = 13;
    final int REQUEST_LAUNCH_KHPI_FRAGMENT = 14;
    final int REQUEST_LAUNCH_INFO_FRAGMENT = 15;

    boolean[] fragmentLaunched = new boolean[6];

    int requestCode = -1;

    public static String mySharedPreferences = "YourApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setTitle("Збережені групи");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        fragmentLaunched[0]=true;
        for (int i=1; i<fragmentLaunched.length; i++) fragmentLaunched[i]=false;

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mf).addToBackStack(null).commit();

        requestCode=getIntent().getIntExtra("request",-1);
        if (requestCode!=-1)
        {
            if (requestCode == REQUEST_LAUNCH_KATHEDRAL_FRAGMENT) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.kathedral_fragment, kf).addToBackStack(null).commit();
                getFlags(1);
            }
            if (requestCode == REQUEST_LAUNCH_TASKS_FRAGMENT) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.tasks_fragment, tf).addToBackStack(null).commit();
                getFlags(2);
            }
            if (requestCode == REQUEST_LAUNCH_HOUSING_FRAGMENT) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.housing_fragment, hf).addToBackStack(null).commit();
                getFlags(3);
            }
            if (requestCode == REQUEST_LAUNCH_KHPI_FRAGMENT) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.settings_fragment, khf).addToBackStack(null).commit();
                getFlags(4);
            }
            if (requestCode == REQUEST_LAUNCH_INFO_FRAGMENT) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.info_fragment, inf).addToBackStack(null).commit();
                getFlags(5);
            }

            requestCode=-1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_SAVE_NEW_TASK) {
                tf.updateTask();
            }

            if (requestCode == REQUEST_SAVE_NEW_SCHEDULE) {
                SharedPreferences prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
                Boolean isMyGroupSaved = prefs.getBoolean("isMyGroupSaved", false);
                mf.updateGroup(isMyGroupSaved);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            if (!fragmentLaunched[0])
            {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mf).addToBackStack(null).commit();
                getFlags(0);
            }

        } else if (id == R.id.nav_gallery) {

            if (!fragmentLaunched[1])
            {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.kathedral_fragment, kf).addToBackStack(null).commit();
                getFlags(1);
            }


        } else if (id == R.id.nav_slideshow) {

            if (!fragmentLaunched[2])
            {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.tasks_fragment, tf).addToBackStack(null).commit();
                getFlags(2);
            }

        } else if (id == R.id.nav_manage) {

            if (!fragmentLaunched[3])
            {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.housing_fragment, hf).addToBackStack(null).commit();
                getFlags(3);
            }

        } else if (id == R.id.nav_share) {

            if (!fragmentLaunched[4])
            {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.settings_fragment, khf).addToBackStack(null).commit();
                getFlags(4);
            }

        } else if (id == R.id.nav_send) {

            if (!fragmentLaunched[5])
            {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.info_fragment, inf).addToBackStack(null).commit();
                getFlags(5);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getFlags(int position)
    {
        for (int i=0; i<fragmentLaunched.length; i++) fragmentLaunched[i] = i == position;
    }
}
