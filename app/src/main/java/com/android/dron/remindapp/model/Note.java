package com.android.dron.remindapp.model;

public class Note {
    private String note;
    private String date_time;

    public Note(String note, String date_time) {
        this.note = note;
        this.date_time = date_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {

        this.date_time = date_time;
    }
}
