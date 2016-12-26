package com.android.dron.remindapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.dron.remindapp.R;
import com.android.dron.remindapp.adapters.FragmentEventRecViewAdapter;
import com.android.dron.remindapp.content.ContentEventActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<String> list;
    private FragmentEventRecViewAdapter adapter;
    private Intent intent;
    private static int FRAGMENT_EVENT = 6;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        intent = getActivity().getIntent();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_event);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_fragment_event);
        list = new ArrayList<>();
        list.add("1");
        adapter = new FragmentEventRecViewAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(new FragmentEventRecViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, View view) {
                switch (view.getId()) {
                    case R.id.btn_close:
                        Toast.makeText(getContext(), "btn_close_event", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContentEventActivity.class);
                startActivityForResult(intent, FRAGMENT_EVENT);
            }
        });
        setList();
        return view;
    }

    public void setList() {
        String text = intent.getStringExtra("text");
        if (text != null) {
            list.add(text);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String text = data.getStringExtra("text");
            if (text != null) {
                list.add(text);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
