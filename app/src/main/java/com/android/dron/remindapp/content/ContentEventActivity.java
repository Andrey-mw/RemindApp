package com.android.dron.remindapp.content;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.dron.remindapp.R;
import com.android.dron.remindapp.alarmanager.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ContentEventActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etMsg;
    private Button btnTimePicker, btnDatePicker, btnSetNotification;
    private Toolbar toolbar;
    private GregorianCalendar calendar;
    private List<PendingIntent> pi;
    private PendingIntent pendingIntent;
    static int count = 0;
    private Intent intent;
    private Intent intentList;
    private AlarmManager am;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_event_layout);
        pi = new ArrayList<>();
        initToolbar();
        initView();
    }

    private void initView() {
        calendar = (GregorianCalendar) Calendar.getInstance();
        etMsg = (EditText) findViewById(R.id.et_content_event);
        btnTimePicker = (Button) findViewById(R.id.btn_time_picker);
        btnDatePicker = (Button) findViewById(R.id.btn_date_picker);
        btnSetNotification = (Button) findViewById(R.id.btn_set_notification);
        btnTimePicker.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnSetNotification.setOnClickListener(this);
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate() {
        new DatePickerDialog(ContentEventActivity.this, d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime() {
        new TimePickerDialog(ContentEventActivity.this, t,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true)
                .show();
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 1);
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_date_picker:
                setDate();
                break;

            case R.id.btn_time_picker:
                setTime();
                break;

            case R.id.btn_set_notification:

                intentList = createIntent(etMsg.getText().toString());
                // Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                // alarmIntent.putExtra("msg", etMsg.getText().toString());
                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                pendingIntent = PendingIntent.getBroadcast(this, count, intentList, 0);
                pi.add(pendingIntent);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                count++;
                Toast.makeText(this, "Напоминание создано!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public Intent createIntent(String extra) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg", extra);
        return intent;
    }


    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_content_event);
        toolbar.setTitle("Напоминание");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            intent = new Intent();
        if (intentList == null) {
            Toast.makeText(getApplicationContext(), "intent = null", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            intent.putExtra("text", etMsg.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cancelAlarm(int position) {
        am.cancel(pi.get(position));
    }
}
