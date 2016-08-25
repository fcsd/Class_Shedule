package derpyhooves.dipvlom.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import derpyhooves.dipvlom.Activities.InfoTeacherActivity;
import derpyhooves.dipvlom.R;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] mDataset;
    private ArrayList<String> groupNames;
    private ArrayList<Spannable> ShmiNames;

    private MyClickListenerDA mListenerDA;
    private MyClickListenerGA mListenerGA;
    private MyClickListenerHA mListenerHA;

    private int mode;
    GestureDetector mGestureDetector;


    public RecyclerAdapter(Context context, MyClickListenerDA listener, String[] dataset) {
        mListenerDA = listener;
        mDataset = dataset;
        mode = 1;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public RecyclerAdapter(Context context, MyClickListenerGA listener, ArrayList<String> groupNames) {
        mListenerGA = listener;
        this.groupNames = groupNames;
        mode=2;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public RecyclerAdapter(Context context, MyClickListenerHA listener, ArrayList<Spannable> groupNames) {
        mListenerHA = listener;
        this.ShmiNames = groupNames;
        mode=3;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }



    public interface MyClickListenerDA {
        public void onItemClick(int position);
    }

    public interface MyClickListenerGA {
        public void onItemClick(int position);
    }

    public interface MyClickListenerHA {
        public void onItemClick(int position);
    }


    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // наш пункт состоит только из одного TextView
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);

        }
    }


    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_department, parent, false);
        if (mode==2) v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_group, parent, false);
        if (mode==3) v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_house, parent, false);


        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode==1) mListenerDA.onItemClick(vh.getAdapterPosition());
                if (mode==2) mListenerGA.onItemClick(vh.getAdapterPosition());
                if (mode==3) mListenerHA.onItemClick(vh.getAdapterPosition());
            }
        });

        return vh;
    }


    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mode==1) holder.mTextView.setText(mDataset[position]);
        if (mode==2) holder.mTextView.setText(groupNames.get(position));
        if (mode==3) holder.mTextView.setText(ShmiNames.get(position));

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {

        if (mode==1) return mDataset.length;
        if (mode==2) return groupNames.size();
        if (mode==3) return ShmiNames.size();
        return 0;
    }
}
