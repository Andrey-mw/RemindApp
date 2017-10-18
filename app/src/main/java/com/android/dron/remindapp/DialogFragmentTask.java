package com.android.dron.remindapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.dron.remindapp.util.RequestCode;

public class DialogFragmentTask extends DialogFragment implements View.OnClickListener {

    private EditText editText;
    public static final String TAG_TEXT_NOTES = "text_task";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Введите текст: ");
        View view = inflater.inflate(R.layout.dialog_item, null);
        editText = (EditText) view.findViewById(R.id.et_dialog_fragment_task);
        editText.setBackgroundColor(Color.TRANSPARENT);
        setting();

        // вызов клавиатуры
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        view.findViewById(R.id.btn_dialog_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_dialog_ok:
                intent = new Intent();
                String getTextForEditText = editText.getText().toString();
                intent.putExtra(TAG_TEXT_NOTES, getTextForEditText);
                getTargetFragment().onActivityResult(RequestCode.Task.REQUEST_CODE_FAB_OK, Activity.RESULT_OK, intent);
                dismiss();
                break;
            case R.id.btn_dialog_cancel:
                intent = new Intent();
                getTargetFragment().onActivityResult(RequestCode.Task.REQUEST_CODE_FAB_CANCEL, Activity.RESULT_OK, intent);
                dismiss();
                break;
        }
    }

    public void setting() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        float sizeText = Float.parseFloat(preferences.getString("size_key", "14"));
        editText.setTextSize(sizeText);
    }
}

