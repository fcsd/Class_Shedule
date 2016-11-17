package derpyhooves.dipvlom.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import derpyhooves.dipvlom.Adapters.AlarmAdapter;
import derpyhooves.dipvlom.Adapters.CardAdapter;
import derpyhooves.dipvlom.Adapters.SectionedRecyclerViewAdapter;
import derpyhooves.dipvlom.R;

public class SubjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SectionedRecyclerViewAdapter mSectionedAdapter;

    ArrayList<String> currentSubject = new ArrayList();
    ArrayList<String> getSubject;
    ArrayList<String> tasks;
    String group;
    final int REQUEST_SAVE_NEW_TASK = 1;
    final int REQUEST_EDIT_TASK = 2;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer5);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        setTitle("Предмет");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

       // currentSubject = getIntent().getStringArrayListExtra("subject");
        group = getIntent().getStringExtra("group");
        getSubject = getIntent().getStringArrayListExtra("subject");

        prepareTasks();
        showTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_SAVE_NEW_TASK) {
                updateTasks(true);
                showTasks();
            }

            if (requestCode == REQUEST_EDIT_TASK) {
                prepareTasks();
                showTasks();
            }
        }
    }

    public void prepareTasks()
    {
        tasks=GroupActivity.restoreArrayListFromSP(this,"listOfTasks");
        currentSubject.clear();
        currentSubject.addAll(getSubject);

        for(int i=1;i<tasks.size();i+=5)
        {
            if(tasks.get(i).contains(currentSubject.get(1))&& tasks.get(i-1).contains(group))
            {
                currentSubject.add(tasks.get(i-1));
                currentSubject.add(tasks.get(i));
                currentSubject.add(tasks.get(i+1));
                currentSubject.add(tasks.get(i+2));
                currentSubject.add(tasks.get(i+3));
            }
        }
    }

    public void updateTasks(boolean isSavedNewTask)
    {
        tasks=GroupActivity.restoreArrayListFromSP(this,"listOfTasks");
        if (tasks.get(tasks.size()-5).contains(group)&&tasks.get(tasks.size()-4).contains(currentSubject.get(1))&&isSavedNewTask)
        {
            currentSubject.add(tasks.get(tasks.size()-5));
            currentSubject.add(tasks.get(tasks.size()-4));
            currentSubject.add(tasks.get(tasks.size()-3));
            currentSubject.add(tasks.get(tasks.size()-2));
            currentSubject.add(tasks.get(tasks.size()-1));
        }
    }

    public void showTasks()
    {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new CardAdapter(this, new CardAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position) {
                position = mSectionedAdapter.sectionedPositionToPosition(position);
                if (position != 0) {
                    Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                    ArrayList<String> selectedTask = new ArrayList<>();
                    selectedTask.addAll(currentSubject.subList(position * 5, position * 5 + 5));

                    intent.putExtra("tasks", selectedTask);
                    intent.putExtra("position", position - 1);
                    intent.putExtra("mode", 2);
                    startActivityForResult(intent, REQUEST_EDIT_TASK);
                }
            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }


        }, currentSubject, false, true, true);

        List<SectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SectionedRecyclerViewAdapter.Section>();
        sections.add(new SectionedRecyclerViewAdapter.Section(0,"Предмет"));
        sections.add(new SectionedRecyclerViewAdapter.Section(1,"Завдання"));
        SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        mRecyclerView.setAdapter(mSectionedAdapter);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                if (viewHolder.getAdapterPosition()==0 || viewHolder.getAdapterPosition()==1 || viewHolder.getAdapterPosition()==2)
                    return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    Bitmap bitmap;

                    if (dX < 0) { // swiping left
                        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.delete);
                        float height = (itemView.getHeight() / 2) - (bitmap.getHeight() / 2);

                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);
                        c.drawBitmap(bitmap, (float) itemView.getRight() - 100, (float) itemView.getTop() + height, null);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position=viewHolder.getAdapterPosition();
                position=mSectionedAdapter.sectionedPositionToPosition(position);

                if (position!=0)
                {
                    tasks=GroupActivity.restoreArrayListFromSP(getApplicationContext(),"listOfTasks");

                    for (int j=0; j<tasks.size(); j+=5)
                    {
                        if (tasks.get(j).equals(currentSubject.get(position*5))&&tasks.get(j+1).equals(currentSubject.get(position*5+1))
                                && tasks.get(j+2).equals(currentSubject.get(position*5+2)) && tasks.get(j+3).equals(currentSubject.get(position*5+3)))
                        {
                            for (int i=0; i<5; i++)
                            {
                                currentSubject.remove(position*5);
                                if (i==4) NewTaskActivity.deleteNotification(getApplicationContext(),Integer.parseInt(tasks.get(j)));
                                tasks.remove(j);
                            }
                            break;
                        }
                    }

                    GroupActivity.saveArrayListToSP(getApplicationContext(),tasks,"listOfTasks");
                    showTasks();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void getCurrentHouse()
    {
        int position=-1;
        String buf=getSubject.get(2);
        String getHouse=buf.substring(buf.lastIndexOf(" ")+1,buf.length());
        switch(getHouse)
        {
            case "АК":
                position=0;
                break;

            case "ВК":
                position=1;
                break;

            case "ГАК":
                position=2;
                break;

            case "ІК":
                position=3;
                break;

            case "КГО":
                position=4;
                break;

            case "МК":
                position=5;
                break;

            case "ПС":
                position=6;
                break;

            case "РК":
                position=7;
                break;

            case "ГК":
                position=8;
                break;

            case "СК":
                position=9;
                break;

            case "ТК":
                position=10;
                break;

            case "КУМ":
                position=11;
                break;

            case "У1":
                position=12;
                break;

            case "У2":
                position=13;
                break;

            case "У3":
                position=14;
                break;

            case "У4":
                position=15;
                break;

            case "У5":
                position=16;
                break;

            case "ФК":
                position=17;
                break;

            case "ХК":
                position=18;
                break;

            case "ЕК":
                position=19;
                break;
        }

        if (position==-1) Toast.makeText(this, "Невірний корпус!", Toast.LENGTH_LONG).show();
        else {
            Intent intent = new Intent(getApplicationContext(), HouseActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("isSubjectActivity",true);
            startActivity(intent);
        }
    }

    public void getCurrentTeacher()
    {
        ArrayList<String> listOfSavedKathedras;
        boolean isTeacherContains=false;
        listOfSavedKathedras = GroupActivity.restoreArrayListFromSP(getApplicationContext(), "savedKathedras");
        for (int i=0;i<listOfSavedKathedras.size();i++)
        {
            ArrayList<String> shortTeachersNames;
            shortTeachersNames = GroupActivity.restoreArrayListFromSP(getApplicationContext(),listOfSavedKathedras.get(i)+"short");
            {
                if (shortTeachersNames.contains(currentSubject.get(4)))
                {
                    isTeacherContains=true;
                    ArrayList<String> TeacherNames;
                    ArrayList<String> TeacherLinks;
                    int position = shortTeachersNames.indexOf(currentSubject.get(4));
                    TeacherNames = GroupActivity.restoreArrayListFromSP(getApplicationContext(),listOfSavedKathedras.get(i));
                    TeacherLinks = GroupActivity.restoreArrayListFromSP(getApplicationContext(),listOfSavedKathedras.get(i)+"links");

                    Intent intent = new Intent(getApplicationContext(), InfoTeacherActivity.class);
                    intent.putExtra("TeacherName", TeacherNames.get(position));
                    intent.putExtra("TeacherLink", TeacherLinks.get(position));
                    startActivity(intent);
                }
            }
        }

        if (!isTeacherContains) Toast.makeText(this, "Неможливо подивитись інформацію про поточного викладача!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_subject, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.subject_map:
               // MAP
                getCurrentHouse();
                return true;

            case R.id.teacher:
                // TEACHER
                getCurrentTeacher();
                return true;

            case R.id.subject_task:

                SharedPreferences prefs = this.getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
                if(prefs.contains(group+"_size"))
                {
                    Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                    intent.putExtra("group", group);
                    intent.putExtra("subject", currentSubject.get(1));
                    intent.putExtra("mode", 1);
                    startActivityForResult(intent, REQUEST_SAVE_NEW_TASK);
                }
                else Toast.makeText(this, "Ви можете додати завдання тільки для збереженних груп!", Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
