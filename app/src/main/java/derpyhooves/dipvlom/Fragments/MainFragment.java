package derpyhooves.dipvlom.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import derpyhooves.dipvlom.Activities.DepartmentActivity;
import derpyhooves.dipvlom.Activities.GroupActivity;
import derpyhooves.dipvlom.Activities.ScheduleActivity;
import derpyhooves.dipvlom.Adapters.CardAdapter;
import derpyhooves.dipvlom.Adapters.SectionedRecyclerViewAdapter;
import derpyhooves.dipvlom.R;


public class MainFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> keysOfGroups = new ArrayList<>();
    private ArrayList<String> typesOfGroups = new ArrayList<>();
    private ArrayList<String> viewGroup = new ArrayList<>();

    SectionedRecyclerViewAdapter mSectionedAdapter;
    private OnFragmentInteractionListener mListener;

    Activity parentActivity;
    final int REQUEST_SAVE_NEW_SCHEDULE = 2;

    public MainFragment() {
        // Required empty public constructor
    }



    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Збережені групи");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("YourApp", Context.MODE_PRIVATE);

        Boolean isMyGroupSaved = prefs.getBoolean("isMyGroupSaved", false);
        updateGroup(isMyGroupSaved);
        return v;
    }

    public void updateGroup(boolean isMyGroupSaved)
    {
        keysOfGroups= GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(),"keysOfGroup");
        typesOfGroups=GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(),"typesOfGroup");
        viewGroup=toNormalGroup(keysOfGroups,typesOfGroups);


        mAdapter = new CardAdapter(getContext(), new CardAdapter.MyClickListener(){
            @Override
            public void onItemClick(int position){

                position=mSectionedAdapter.sectionedPositionToPosition(position);
                Intent intent = new Intent(getContext(), ScheduleActivity.class);

                intent.putExtra("position", position);
                intent.putExtra("mode", 2);
                getActivity().startActivityForResult(intent, REQUEST_SAVE_NEW_SCHEDULE);
            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }


        }, viewGroup);


        List<SectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SectionedRecyclerViewAdapter.Section>();

        if(isMyGroupSaved)
        {
            sections.add(new SectionedRecyclerViewAdapter.Section(0,"Основна група"));
            sections.add(new SectionedRecyclerViewAdapter.Section(1,"Інші групи"));
        }
        else{
            sections.add(new SectionedRecyclerViewAdapter.Section(0,"Основна група"));
            sections.add(new SectionedRecyclerViewAdapter.Section(0,"Інші групи"));
        }

        //Add your adapter to the sectionAdapter
        SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SectionedRecyclerViewAdapter(getContext(), R.layout.section, R.id.section_text, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);
    }


    public String getSrtingDepartment(String department)
    {
        String currentDepartment = new String();
        switch (department)
        {
            case "АП":
                currentDepartment = "Факультет автоматики та приладобудування";
                break;

            case "БФ":
                currentDepartment = "Факультет бізнесу та фінансів";
                break;

            case "ЕК":
                currentDepartment = "Економічний факультет";
                break;

            case "ЕІМ":
                currentDepartment = "Факультет економічної інформатики та менеджменту";
                break;

            case "Е":
                currentDepartment = "Електроенергетичний факультет";
                break;

            case "ЕМБ":
                currentDepartment = "Електромашинобудівний факультет";
                break;

            case "ЕМ":
                currentDepartment = "Енергомашинобудівний факультет";
                break;

            case "І":
                currentDepartment = "Інженерно-фізичний факультет";
                break;

            case "ІПФ":
                currentDepartment = "Факультет інтегральної підготовки";
                break;

            case "ІТ":
                currentDepartment = "Факультет інтегрованих технологій і хімічної техніки";
                break;

            case "ІФ":
                currentDepartment = "Факультет інформатики та управління";
                break;

            case "КІТ":
                currentDepartment = "Факультет комп'ютерних та інформаційних технологій";
                break;

            case "МШ":
                currentDepartment = "Машинобудівний факультет";
                break;

            case "МТ":
                currentDepartment = "Механіко-технологічний факультет";
                break;

            case "НТ":
                currentDepartment = "Німецький технічний факультет";
                break;

            case "Н":
                currentDepartment = "Факультет технологій неорганічних речовин";
                break;

            case "О":
                currentDepartment = "Факультет технологій органічних речовин";
                break;

            case "ТМ":
                currentDepartment = "Факультет транспортного машинобудування";
                break;

            case "ФТ":
                currentDepartment = "Фізико-технічний факультет";
                break;
        }
        return currentDepartment;
    }

    public String getStringCourse(int course)
    {
        String currentCourse = new String();
        switch (course)
        {
            case 1:
                currentCourse="1-ий курс";
                break;

            case 2:
                currentCourse="2-ий курс";
                break;

            case 3:
                currentCourse="3-ій курс";
                break;

            case 4:
                currentCourse="4-ий курс";
                break;

            case 5:
                currentCourse="5-ий курс";
                break;

            case 6:
                currentCourse="6-ий курс";
                break;
        }

        return currentCourse;
    }

    public String getStringType(String type)
    {
        String currentType= new String();
        switch (type)
        {
            case "осінньому":
                currentType="Осінній семестр";
                break;

            case "весняному":
                currentType="Весняний семестр";
                break;
        }

        return currentType;
    }

    public ArrayList<String> toNormalGroup(ArrayList<String> data, ArrayList<String> type)
    {
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        currentYear = currentYear % 10;
        int currentCourse;

        ArrayList<String>  normalViewGroup = new ArrayList<>();
        String buf;

        for (int i=0;i<data.size();i++)
        {
            buf=data.get(i).toString();
            String[] s = buf.split("-");
            // название факультета
            String department=getSrtingDepartment(s[0]);
            // курс
            buf= String.valueOf(s[1].charAt(1));
            int yearAdmision = Integer.parseInt(buf);
            if (currentYear < yearAdmision) currentYear+=10;
            currentCourse=currentYear-yearAdmision;
            if (currentMonth>8) currentCourse++;
            String course=getStringCourse(currentCourse);
            // тип
            buf=type.get(i).toString();
            buf=getStringType(buf);

            normalViewGroup.add(data.get(i));
            normalViewGroup.add(department);
            normalViewGroup.add(course);
            normalViewGroup.add(buf);
        }

        return normalViewGroup;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_new_item:
                Intent intent = new Intent(getContext(), DepartmentActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_SAVE_NEW_SCHEDULE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void backButtonWasPressed() {
        parentActivity.finish();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            parentActivity=(Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
