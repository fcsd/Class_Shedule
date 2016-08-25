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

import java.util.ArrayList;

import derpyhooves.dipvlom.R;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    private MyClickListener myClickListener;
    GestureDetector mGestureDetector;

    public CardAdapter(Context context, MyClickListener listener, ArrayList<String> myDataset) {
        mDataset = myDataset;
        myClickListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            position=position*4;
            holder.time.setText(mDataset.get(position));
            holder.subject.setText(mDataset.get(position+1));
            holder.location.setText(mDataset.get(position+2));
            holder.type.setText(mDataset.get(position+3));
    }

    @Override
    public int getItemCount() {
        return mDataset.size()/4;
    }

    public interface MyClickListener {
        void onItemClick(int position);

        boolean onLongClick(View v);
    }



     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView time;
        TextView location;
        TextView subject;
        TextView type;
        CardView card_view;



        public ViewHolder(View itemView) {

            super(itemView);

            card_view = (CardView)itemView.findViewById(R.id.my_card_view);
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout, parent, false);

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
