package derpyhooves.dipvlom.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import derpyhooves.dipvlom.Fragments.TasksFragment;
import derpyhooves.dipvlom.R;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    private MyClickListener myClickListener;
    GestureDetector mGestureDetector;
    private boolean mIsTasksFragment;
    private boolean mIsSubjectActivity;
    private boolean mIsScheduleActivity;

    public CardAdapter(Context context, MyClickListener listener, ArrayList<String> myDataset, boolean isTasksFragment,
                       boolean isSubjectActivity, boolean isScheduleActivity) {
        mDataset = myDataset;
        myClickListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        mIsTasksFragment = isTasksFragment;
        mIsSubjectActivity = isSubjectActivity;
        mIsScheduleActivity = isScheduleActivity;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (!mIsTasksFragment && !mIsSubjectActivity && !mIsScheduleActivity) {
            position=position*4;
            holder.location.setText(mDataset.get(position+2));
        }

        if (!mIsTasksFragment && mIsSubjectActivity && mIsScheduleActivity) {
            position = position * 5;
            if (position==0)
            {
                holder.location.setText(mDataset.get(position+2));
                holder.teacher.setText(mDataset.get(position+4));
            }
            else
            {
                holder.location.setText(getStringDate(mDataset.get(position+2)));
                holder.teacher.setText("");
            }
        }

        if (mIsTasksFragment && !mIsSubjectActivity && !mIsScheduleActivity) {
            position=position*5;
            holder.location.setText(getStringDate(mDataset.get(position+2)));
        }

        if (!mIsSubjectActivity && mIsScheduleActivity) {
            position = position * 5;
            holder.location.setText(mDataset.get(position+2));
            holder.teacher.setText(mDataset.get(position+4));
        }

        holder.time.setText(mDataset.get(position));
        holder.subject.setText(mDataset.get(position+1));
        holder.type.setText(mDataset.get(position+3));
    }

    @Override
    public int getItemCount() {
        if (!mIsTasksFragment && !mIsSubjectActivity && !mIsScheduleActivity) return mDataset.size()/4;
        else return mDataset.size()/5;
    }

    public interface MyClickListener {
        void onItemClick(int position);

        boolean onLongClick(View v);
    }

    public String getStringDate (String targetDayInMillis)
    {
        Date date = new Date();
        date.setTime(Long.parseLong(targetDayInMillis));
        String formattedDate;
        new SimpleDateFormat("yyyy MM dd").format(date);
        formattedDate = "Виконати до " + DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("uk", "UA")).format(date);
        return formattedDate;
    }

     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

         TextView time;
         TextView location;
         TextView subject;
         TextView type;
         TextView teacher;
         CardView card_view;


        public ViewHolder(View itemView) {

            super(itemView);
            card_view = (CardView)itemView.findViewById(R.id.my_card_view);
            if (mIsScheduleActivity) teacher = (TextView) itemView.findViewById(R.id.teacher);
            time = (TextView) itemView.findViewById(R.id.time);
            location = (TextView) itemView.findViewById(R.id.location);
            subject = (TextView) itemView.findViewById(R.id.subject);
            type = (TextView) itemView.findViewById(R.id.type);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition());
        }

         @Override
         public boolean onLongClick(View v) {
             myClickListener.onItemClick(getAdapterPosition());
             return false;
         }
     }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View v;
        if (!mIsScheduleActivity) v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout, parent, false);
        else v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout_schedule, parent, false);

        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(vh.getAdapterPosition());
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myClickListener.onItemClick(vh.getAdapterPosition());
                return false;
            }
        });

        return vh;
    }

}
