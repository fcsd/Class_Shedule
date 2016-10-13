package derpyhooves.dipvlom.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import derpyhooves.dipvlom.Activities.HouseActivity;
import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.R;


public class HousingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_housing, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Учбові корпуси");

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);


        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter mAdapter = new RecyclerAdapter(getContext(), new RecyclerAdapter.MyClickListenerDA() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), HouseActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        }, getDataSet());

        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private String[] getDataSet() {
        return getResources().getStringArray(R.array.Housing);
    }

}
