package com.android.dron.remindapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.dron.remindapp.ItemClickListener;
import com.android.dron.remindapp.R;
import com.android.dron.remindapp.fragments.FragmentTask;
import com.android.dron.remindapp.model.Note;
import com.android.dron.remindapp.util.AnimationUtil;

import java.util.List;

public class FragmentTaskRecViewAdapter extends RecyclerView.Adapter<FragmentTaskRecViewAdapter.ViewHolder> {

    public List<Note> list, filterList;
    private int previousPosition = 0;
    FragmentTask fragmentTask = new FragmentTask();


//    public static OnItemClickListener listener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }

//    public interface OnItemClickListener {
//        void onItemClick(View itemView, int position, View view);
//    }

    public FragmentTaskRecViewAdapter(List<Note> list) {
        this.list = list;
        this.filterList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.getTextView().setText(list.get(position).getNote());
        holder.getTvDate().setText(list.get(position).getDate_time());
        holder.setListener(new ItemClickListener() {
            @Override
            public void onItemClickk(View view, int position) {
                fragmentTask.initSwipe();
            }
        });

        if (position > previousPosition) { // We are scrolling DOWN
            AnimationUtil.animate(holder, true);
        } else { // We are scrolling UP
            AnimationUtil.animate(holder, false);
        }
        previousPosition = position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private TextView tvDate;
        ItemClickListener listener;

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        ViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_content_fragment_task);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            textView.setOnClickListener(this);
        }

        TextView getTvDate() {
            return tvDate;
        }

        TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View v) {
//            if (FragmentTaskRecViewAdapter.listener != null) {
//                FragmentTaskRecViewAdapter.listener.onItemClick(itemView, getLayoutPosition(), v);
//            }
            this.listener.onItemClickk(v, getLayoutPosition());
        }
    }
}
