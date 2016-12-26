package com.android.dron.remindapp.alarmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.dron.remindapp.DialogFragmentEvent;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, DialogFragmentEvent.class);
        newIntent.putExtra("msg", intent.getStringExtra("msg"));
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}
