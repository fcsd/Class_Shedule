package derpyhooves.dipvlom.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import derpyhooves.dipvlom.Activities.GroupActivity;
import derpyhooves.dipvlom.Activities.NewTaskActivity;
import derpyhooves.dipvlom.Adapters.CardAdapter;
import derpyhooves.dipvlom.R;


public class TasksFragment extends Fragment {

    private ArrayList<String> tasks;
    private ArrayList<String> keysOfGroups = new ArrayList<>();
    View v;

    final int REQUEST_SAVE_NEW_TASK = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_tasks, container, false);
        updateTask();
        return v;
    }

    public void updateTask()
    {
        tasks= GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(),"listOfTasks");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Завдання");

        keysOfGroups= GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(),"keysOfGroup");

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new CardAdapter(getContext(), new CardAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position) {

                ArrayList<String> selectedTask = new ArrayList<>();
                selectedTask.addAll(tasks.subList(position * 5, position * 5 + 5));
                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                intent.putExtra("tasks", selectedTask);
                intent.putExtra("position", position);
                intent.putExtra("mode", 2);
                getActivity().startActivityForResult(intent, REQUEST_SAVE_NEW_TASK);
            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }


        }, tasks, true, false, false);

        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    Bitmap bitmap;

                    if (dX < 0) { // swiping left

                        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.delete);
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

                NewTaskActivity.deleteNotification(getContext(),Integer.parseInt(tasks.get(position*5+4)));
                tasks=GroupActivity.restoreArrayListFromSP(getContext(),"listOfTasks");
                for (int i=0; i<5; i++) tasks.remove(position*5);
                GroupActivity.saveArrayListToSP(getContext(),tasks,"listOfTasks");
                updateTask();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_task, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_new_item:

                if (!keysOfGroups.isEmpty())
                {
                    Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                    intent.putExtra("mode", 1);
                    getActivity().startActivityForResult(intent, REQUEST_SAVE_NEW_TASK);
                }
                else Toast.makeText(getActivity(), "Жодна група не була зберена, додати завдання неможливо!", Toast.LENGTH_LONG).show();

                return true;

            case R.id.sorting:
                showPopup();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopup()
    {

        View menuItemView = getActivity().findViewById(R.id.sorting);

        PopupMenu popup = new PopupMenu(getActivity(), menuItemView);
        popup.getMenuInflater().inflate(R.menu.fragment_task_popup, popup.getMenu());

        //inflate.inflate(R.menu.fragment_task_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.sort_by_group:

                        finalSorting(1);
                        return true;

                    case R.id.sort_by_subject:

                        finalSorting(2);
                        return true;

                    case R.id.sort_by_date:

                        finalSorting(3);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }


    public void finalSorting(int mode)  {

        ArrayList<ArrayList<String>> allItems = new ArrayList<>();

        for (int i=0;i<tasks.size();i+=5) allItems.add(new ArrayList<>(tasks.subList(i,i+5)));

        allItems=getSorting(allItems,mode);
        tasks.clear();

        for (int i=0; i<allItems.size();i++)
        {
            for (int j=0;j<5;j++)
            {
                tasks.add(allItems.get(i).get(j));
            }
        }
        GroupActivity.saveArrayListToSP(getContext(),tasks,"listOfTasks");
        updateTask();

    }

    public ArrayList<ArrayList<String>> getSorting (ArrayList<ArrayList<String>> data, final int mode)
    {
        Collections.sort(data, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> lhs, ArrayList<String> rhs)
            {
                return lhs.get(mode-1).compareTo(rhs.get(mode-1));
            }
        });
        return data;
    }
}
