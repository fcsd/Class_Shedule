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

import derpyhooves.dipvlom.Activities.TeachersActivity;
import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.R;


public class KHPIFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private OnFragmentInteractionListener mListener;

    public KHPIFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static KHPIFragment newInstance() {
        KHPIFragment fragment = new KHPIFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_khpi, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Про ХПІ");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);


        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerAdapter(getContext(), new RecyclerAdapter.MyClickListenerDA(){
            @Override
            public void onItemClick(int position){


            }}, getDataSet());

        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private String[] getDataSet() {
        String[] mDataSet = getResources().getStringArray(R.array.khpi);
        return mDataSet;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
