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

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_ITEM = "NOTE_ITEM";
    private static final String COLUMN_TIME_DATE = "TIME_DATE";

    private static final String DB_CREATE_NOTE =
            "create table " + DB_TABLE_NOTE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NOTE_ITEM + " text, " +
                    COLUMN_TIME_DATE + " text" +
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

    // добавить запись в DB_TABLE
    public void addRecNote(String note, String time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE_ITEM, note);
        cv.put(COLUMN_TIME_DATE, time);
        database.insert(DB_TABLE_NOTE, null, cv);
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

    // удалить запись из DB_TABLE
    public void delRecNote(String id) {
        database.delete(DB_TABLE_NOTE, COLUMN_TIME_DATE + " = ?", new String[]{id});
    }

    // поиск
    public Cursor getSearchList(String search) {
        database = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT rowid as" +
                COLUMN_ID + "," +
                COLUMN_NOTE_ITEM + "," +
                COLUMN_TIME_DATE +
                " FROM " + DB_TABLE_NOTE +
                " WHERE " + COLUMN_NOTE_ITEM + " LIKE '%" + search + "%' ";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    //Сортировка
    public Cursor getSort() {
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DB_TABLE_NOTE, null, null, null, null, null, COLUMN_NOTE_ITEM + " ASC");
        return cursor;
    }

    private class RemindDataBaseHelper extends SQLiteOpenHelper {
        RemindDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
