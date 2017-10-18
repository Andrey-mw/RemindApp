package com.android.dron.remindapp.content;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.dron.remindapp.R;
import com.android.dron.remindapp.fragments.FragmentTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContentTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button btnSave;
    private Button btnCancel;
    private Toolbar toolbar;
    private Intent intent;
    private String task_content_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_task_layout);
        initView();
        initToolbar();
        setting();
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setText(task_content_text);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void initView() {
        intent = getIntent();
        task_content_text = intent.getStringExtra(FragmentTask.TAG_TASK_CONTENT_TEXT);
        editText = (EditText) findViewById(R.id.et_content_task);
        btnSave = (Button) findViewById(R.id.btn_save_item_task);
        btnCancel = (Button) findViewById(R.id.btn_cancel_item_task);
    }

    public void setting() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        float sizeText = Float.parseFloat(preferences.getString("size_key", "20"));
        editText.setTextSize(sizeText);
    }

    @Override
    public void onClick(View v) {
        String id = intent.getStringExtra(FragmentTask.TAG_TASK_ID);
        switch (v.getId()) {
            case R.id.btn_save_item_task:
                intent.putExtra("edit", editText.getText().toString());
                intent.putExtra("pos", id);
                intent.putExtra("date", setListDateFormat());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btn_cancel_item_task:
                finish();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_content);
        toolbar.setTitle(R.string.tab_name_task);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_toolbar_clear_text:
                        editText.setText(null);
                        break;
                    case R.id.menu_toolbar_back_old_text:
                        editText.setText(task_content_text);
                }
                return false;
            }
        });
        //toolbar.inflateMenu(R.menu.menu_toolbar_item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_item, menu);
        return true;
    }

    public String setListDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = timeFormat.format(new Date(System.currentTimeMillis())) + "\n" + dateFormat.format(new Date(System.currentTimeMillis()));
        return date;
    }
}
