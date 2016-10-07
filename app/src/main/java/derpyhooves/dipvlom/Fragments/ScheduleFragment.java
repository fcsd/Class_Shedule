package derpyhooves.dipvlom.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import derpyhooves.dipvlom.Activities.GroupActivity;
import derpyhooves.dipvlom.Activities.SubjectActivity;
import derpyhooves.dipvlom.Adapters.CardAdapter;
import derpyhooves.dipvlom.R;

public class ScheduleFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mData;
    private String mGroup;


    @SuppressLint("ValidFragment")
    public ScheduleFragment(ArrayList<String> data, String group) {

        mData=data;
        mGroup=group;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapter(getContext(), new CardAdapter.MyClickListener(){
            @Override
            public void onItemClick(int position){

                ArrayList<String> selectedSubject = new ArrayList<>();
                selectedSubject.addAll(mData.subList(position*4,position*4+4));

                Intent intent = new Intent(getActivity(), SubjectActivity.class);
                intent.putExtra("subject", selectedSubject);
                intent.putExtra("group", mGroup);
                startActivity(intent);

            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }


        }, mData, false, false);
        mRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return v;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
