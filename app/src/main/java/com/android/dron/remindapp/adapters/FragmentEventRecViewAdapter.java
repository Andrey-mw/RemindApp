package com.android.dron.remindapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.dron.remindapp.R;

import java.util.List;

public class FragmentEventRecViewAdapter extends RecyclerView.Adapter<FragmentEventRecViewAdapter.ViewHolder> {

    private List<String> list;

    //
    public static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, View view);
    }
    //

    public FragmentEventRecViewAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_content_buy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.getTextView().setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private Button button;

        public TextView getTextView() {
            return textView;
        }

        public Button getButton() {
            return button;
        }

        public ViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_content_fragment_task);
            button = (Button) itemView.findViewById(R.id.btn_close);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (FragmentEventRecViewAdapter.listener != null) {
                FragmentEventRecViewAdapter.listener.onItemClick(itemView, getLayoutPosition(), v);
            }
        }
    }
}
