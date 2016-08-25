package derpyhooves.dipvlom.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import derpyhooves.dipvlom.Adapters.RecyclerAdapter;
import derpyhooves.dipvlom.R;


public class InfoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Spannable> spInfo = new ArrayList<>();


    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Про додаток");


        ImageView imageView = (ImageView) v.findViewById(R.id.imageView1);
        imageView.setImageResource(R.drawable.derp);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);


        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        spInfo.clear();
        String [] shmi = getDataSet();

        for (int i=0; i<shmi.length; i++) getSpannableString(shmi[i]);

        mAdapter = new RecyclerAdapter(getContext(), new RecyclerAdapter.MyClickListenerHA(){
            @Override
            public void onItemClick(int position){

            }}, spInfo);


        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private void getSpannableString(String str)
    {
        Spannable sb = new SpannableString(str);
        if (str.startsWith("Developer:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("E-mail:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Version:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (str.startsWith("Last update:")) sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spInfo.add(sb);
    }

    private String[] getDataSet() {
        String[] mDataSet = getResources().getStringArray(R.array.info);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
