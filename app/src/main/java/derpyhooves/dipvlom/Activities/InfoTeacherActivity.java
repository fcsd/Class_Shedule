package derpyhooves.dipvlom.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.StrictMode;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.Adapters.jsoupAdapter;
import derpyhooves.dipvlom.R;

public class InfoTeacherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, jsoupAdapter.AsyncResponse {

    private String TeacherName;
    private String TeacherLink;
    private ArrayList<String> TeacherInfo;
    ArrayList<Spannable> spTeacherInfo = new ArrayList<>();
    Bitmap bitmap;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_teacher);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TeacherName = getIntent().getStringExtra("TeacherName");
        TeacherLink = getIntent().getStringExtra("TeacherLink");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(TeacherName);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer10);
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

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);

        if (prefs.contains(TeacherName + "_size"))
        {
            bitmap=decodeBase64(prefs.getString(TeacherName + " + img",null));
            TeacherInfo=GroupActivity.restoreArrayListFromSP(this,TeacherName);
            showRecyclerView();
        }
        else showTeachers();

    }

    private void showRecyclerView()
    {
        ImageView mImg;
        mImg = (ImageView) findViewById(R.id.imageView1);
        mImg.setImageBitmap(bitmap);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);


        for (int i=0;i<TeacherInfo.size();i++) getSpannableString(TeacherInfo.get(i));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapter = new RecyclerAdapter(this, new RecyclerAdapter.MyClickListenerHA() {
            @Override
            public void onItemClick(int position) {

            }
        }, spTeacherInfo);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void getSpannableString(String str)
    {
        Spannable sb = new SpannableString(str);
        if (str.startsWith("Посада:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Наукове звання:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Назви дисциплін:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Інформація")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Контакти:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Вчений ступінь:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Тема дисертації:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Наукові інтереси:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spTeacherInfo.add(sb);
    }

    public void showTeachers() {
        if (GroupActivity.hasConnection(getApplicationContext())) {

            jsoupAdapter mt = new jsoupAdapter(TeacherLink, 4, this, this);
            mt.execute();

        }
        else Toast.makeText(this, "Немає з'єднання з інернетом!", Toast.LENGTH_LONG).show();
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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
            TeacherInfo = map.get(1);
            ArrayList<String> teacherPhoto = map.get(2);

            bitmap=getBitmapFromURL(teacherPhoto.get(0));

            SharedPreferences prefs = getApplicationContext().getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(TeacherName + " + img", encodeTobase64(bitmap));
            editor.apply();

            GroupActivity.saveArrayListToSP(getApplicationContext(),TeacherInfo,TeacherName);
            showRecyclerView();
        }
        else Toast.makeText(this, "Під час завантаження зникло з'єднання з інтернетом!", Toast.LENGTH_LONG).show();
    }
}
