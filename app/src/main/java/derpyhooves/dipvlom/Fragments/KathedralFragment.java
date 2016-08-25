package derpyhooves.dipvlom.Fragments;

import android.app.Activity;
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
import derpyhooves.dipvlom.Activities.TeachersActivity;
import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.R;


public class KathedralFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    Activity parentActivity;
    View v;

    public KathedralFragment() {
        // Required empty public constructor
    }


    public static KathedralFragment newInstance(String param1, String param2) {
        KathedralFragment fragment = new KathedralFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_kathedral, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Кафедри");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);


        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerAdapter(getContext(), new RecyclerAdapter.MyClickListenerDA(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(getContext(), TeachersActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("kathedra", currentData(position,getDataSet()));
                startActivity(intent);

            }}, getDataSet());

        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private String[] getDataSet() {
        String[] mDataSet = getResources().getStringArray(R.array.Kathedral);
        return mDataSet;
    }

    private String currentData(int position, String[] mDataSet) {
        return mDataSet[position];
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
