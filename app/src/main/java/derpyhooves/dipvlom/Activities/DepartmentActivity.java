package derpyhooves.dipvlom.Activities;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.MenuItem;

import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.R;

public class DepartmentActivity extends AppCompatActivity implements RecyclerAdapter.MyClickListenerDA,
        NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Факультети");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer2);
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


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(getApplicationContext(),this, getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }


    private String[] getDataSet() {
        String[] mDataSet = getResources().getStringArray(R.array.listOfFaq);
        return mDataSet;
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

