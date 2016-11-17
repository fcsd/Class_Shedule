package derpyhooves.dipvlom.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import derpyhooves.dipvlom.Activities.DepartmentActivity;
import derpyhooves.dipvlom.Activities.GroupActivity;
import derpyhooves.dipvlom.Activities.MainActivity;
import derpyhooves.dipvlom.Activities.ScheduleActivity;
import derpyhooves.dipvlom.Adapters.CardAdapter;
import derpyhooves.dipvlom.Adapters.SectionedRecyclerViewAdapter;
import derpyhooves.dipvlom.R;


public class MainFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private TextView textView;
    SectionedRecyclerViewAdapter mSectionedAdapter;

    final int REQUEST_SAVE_NEW_SCHEDULE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(MainActivity.mySharedPreferences, Context.MODE_PRIVATE);
        Boolean isMyGroupSaved = prefs.getBoolean("isMyGroupSaved", false);

        textView = (TextView) v.findViewById(R.id.time_text);
        ScrollView view = (ScrollView) v.findViewById(R.id.scrollViewMF);
        view.requestFocus();

        checkDeleteTasks();

        updateGroup(isMyGroupSaved);
        return v;
    }

    private void checkDeleteTasks()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        long listValue = Long.parseLong(prefs.getString("deleteTasks", "0"));
        if (listValue!=0)
        {
            ArrayList<String> tasks= GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(),"listOfTasks");

            Calendar c1 = Calendar.getInstance();
            long currentTimeInMillis = c1.getTimeInMillis();

            for (int i=0;i<tasks.size(); i+=5)
            {
                if (currentTimeInMillis>=Long.parseLong(tasks.get(i+2))+listValue)
                {
                    for (int j=0; j<5; j++) tasks.remove(i*5);
                    i-=5;
                }
            }
            GroupActivity.saveArrayListToSP(getContext(),tasks,"listOfTasks");
        }
    }

    public void getText()
    {
        Calendar c1 = Calendar.getInstance();
        int currentTime = c1.get(Calendar.HOUR_OF_DAY) * 60 + c1.get(Calendar.MINUTE);
        int[] constTimeInMillis = getResources().getIntArray(R.array.constant_time);

        boolean isCouple=false;
        boolean isBreak=false;

        int minutesCoupleEnded = 0;
        int couple = 0;

        int dayOfWeek = c1.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek!=1 && dayOfWeek!=7)
        {
            for (int i=0;i<constTimeInMillis.length;i+=2)
            {
                if  (currentTime >= constTimeInMillis[i] && currentTime < constTimeInMillis[i+1])
                {
                    minutesCoupleEnded = constTimeInMillis[i+1] - currentTime;
                    couple = i/2+1;
                    isCouple=true;
                    break;
                }

                if (i!=10)
                {
                    if  (currentTime >= constTimeInMillis[i+1] && currentTime < constTimeInMillis[i+2])
                    {
                        minutesCoupleEnded = constTimeInMillis[i+2] - currentTime;
                        isBreak=true;
                        break;
                    }
                }
            }
        }

        String concateString;
        if (isCouple)
        {
            if (minutesCoupleEnded>60)
            {
                minutesCoupleEnded-=60;
                concateString = "Триває " + couple + " пара. До завершення 1 год. " + minutesCoupleEnded + " хв.";
            }
            else concateString = "Триває " + couple + " пара. До завершення " + minutesCoupleEnded + " хв.";
            textView.setText(concateString);
        }
        else if (isBreak)
        {
            concateString = "Триває перерва. До завершення " + minutesCoupleEnded + " хв.";
            textView.setText(concateString);
        }
        else textView.setText("Поточної пари немає.");

    }

    public void updateGroup(boolean isMyGroupSaved)
    {
        ArrayList<String> keysOfGroups = GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(), "keysOfGroup");
        ArrayList<String> typesOfGroups = GroupActivity.restoreArrayListFromSP(getActivity().getApplicationContext(), "typesOfGroup");
        ArrayList<String> viewGroup = toNormalGroup(keysOfGroups, typesOfGroups);

        getText();

        RecyclerView.Adapter mAdapter = new CardAdapter(getContext(), new CardAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position) {

                position = mSectionedAdapter.sectionedPositionToPosition(position);
                Intent intent = new Intent(getContext(), ScheduleActivity.class);

                intent.putExtra("position", position);
                intent.putExtra("mode", 2);
                getActivity().startActivityForResult(intent, REQUEST_SAVE_NEW_SCHEDULE);
            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }


        }, viewGroup, false, false, false);


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
        String currentDepartment = "";
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
        String currentCourse = "";
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
        String currentType= "";
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
            buf= data.get(i);
            int reverseOffset=0;
            String[] s = buf.split("-");
            // название факультета
            String department=getSrtingDepartment(s[0]);
            // курс

            Pattern p = Pattern.compile("[\\p{L}]", Pattern.UNICODE_CASE);
            Matcher m = p.matcher(s[1]);
            while (m.find()) reverseOffset++;

            buf= String.valueOf(s[1].charAt(s[1].length()-reverseOffset-1));
            int yearAdmision = Integer.parseInt(buf);
            if (currentYear < yearAdmision) currentYear+=10;
            currentCourse=currentYear-yearAdmision;
            if (currentMonth>=7) currentCourse++;
            String course=getStringCourse(currentCourse);

            // тип
            buf=type.get(i);
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
}
