package derpyhooves.dipvlom.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import derpyhooves.dipvlom.Activities.HouseActivity;
import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.Adapters.SectionedRecyclerViewAdapter;
import derpyhooves.dipvlom.R;


@SuppressLint("ValidFragment")
public class ShowHouseInfoFragment extends Fragment {

    ArrayList<Spannable> mInfo = new ArrayList<>();

    public ShowHouseInfoFragment(ArrayList<Spannable> info) {
        mInfo=info;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_show_house_info, container, false);


        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter mAdapter = new RecyclerAdapter(getContext(), new RecyclerAdapter.MyClickListenerHA() {
            @Override
            public void onItemClick(int position) {

            }
        }, mInfo);

        mRecyclerView.setAdapter(mAdapter);
        return v;
    }
}
