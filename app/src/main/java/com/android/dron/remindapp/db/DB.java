package com.android.dron.remindapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by User on 21.11.2016.
 */

public class DB {

    private final Context mCtx;
    private RemindDataBaseHelper dbHelper;
    private SQLiteDatabase database;

    private static final String DB_NAME = "REMIND";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_NOTE = "NOTE";
    private static final String DB_TABLE_BUY = "BUY";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_ITEM = "NOTE_ITEM";
    private static final String COLUMN_TIME_DATE = "TIME_DATE";
    private static final String COLUMN_TITLE_BUY = "TITLE_BUY";
    private static final String COLUMN_ITEM_BUY = "ITEM_BUY";

    private static final String DB_CREATE_NOTE =
            "create table " + DB_TABLE_NOTE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NOTE_ITEM + " text, " +
                    COLUMN_TIME_DATE + " text" +
                    ");";

    private static final String DB_CREATE_BUY =
            "create table " + DB_TABLE_BUY + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TITLE_BUY + " text, " +
                    COLUMN_ITEM_BUY + " text" +
                    ");";


    public DB(Context mCtx) {
        this.mCtx = mCtx;
    }

    // открыть подключение
    public void open() {
        try {
            dbHelper = new RemindDataBaseHelper(mCtx, DB_NAME, null, DB_VERSION);
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Toast.makeText(mCtx, "База данных недоступна!", Toast.LENGTH_LONG).show();
        }
    }

    // закрыть подключение
    public void close() {
        if (dbHelper != null) dbHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllDataNote() {
        return database.query(DB_TABLE_NOTE, null, null, null, null, null, null);
    }

    public Cursor getAllDataBuy() {
        return database.query(DB_TABLE_BUY, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRecNote(String note, String time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_ITEM, note);
        cv.put(COLUMN_TIME_DATE, time);
        database.insert(DB_TABLE_NOTE, null, cv);
        cv.clear();
    }

    public void addRecBuy(String title, String listItem) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE_BUY, title);
        cv.put(COLUMN_ITEM_BUY, listItem);
        database.insert(DB_TABLE_BUY, null, cv);
        cv.clear();
    }

    // обновить запись из DB_TABLE
    public void upRecNote(String note, String time, String id) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_ITEM, note);
        cv.put(COLUMN_TIME_DATE, time);
        database.update(DB_TABLE_NOTE, cv, COLUMN_TIME_DATE + " = ?", new String[]{id});
        cv.clear();
    }

    public void upRecBuy(String note, String time, String id) {
    }

    // удалить запись из DB_TABLE
    public void delRecNote(String id) {
        database.delete(DB_TABLE_NOTE, COLUMN_TIME_DATE + " = ?", new String[]{id});
    }

    public void delRecBuy(String id) {
    }

    private class RemindDataBaseHelper extends SQLiteOpenHelper {
        public RemindDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_NOTE);
            db.execSQL(DB_CREATE_BUY);
//            db.execSQL("CREATE TABLE NOTE ("
//                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "NOTE_ITEM TEXT, "
//                    + "id INTEGER AUTOINCREMENT, "
//                    + "TIME_DATE TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
