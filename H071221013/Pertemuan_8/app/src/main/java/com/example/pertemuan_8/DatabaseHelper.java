package com.example.pertemuan_8;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_ID = "id";
    public static final String TABLE_NAME = "note";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_CREATED_TIMESTAMP = "created_timestamp";
    public static final String COLUMN_UPDATED_TIMESTAMP = "updated_timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESC + "TEXT, " +
                COLUMN_CREATED_TIMESTAMP + " INTEGER, " +
                COLUMN_UPDATED_TIMESTAMP + " INTEGER" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void addTask(String title, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DESC, desc);
        long timestamp = System.currentTimeMillis();
        cv.put(COLUMN_CREATED_TIMESTAMP, timestamp);
        cv.put(COLUMN_UPDATED_TIMESTAMP, timestamp);
        db.insert(TABLE_NAME, null, cv);
    }

    public Cursor readAllData() {
        SQLiteDatabase db = this.getReadableDatabase(); //instance
        return db.query(TABLE_NAME, null, null, null, null, null, COLUMN_CREATED_TIMESTAMP + " ASC");
    }

    //metode untuk update
    public void updateTask(String id, String title, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESC, desc);
        contentValues.put(COLUMN_UPDATED_TIMESTAMP, System.currentTimeMillis());
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{id});
    }

    public void deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
    }

    //pencarian berdasrkan judul
    public ArrayList<Note> searchNotes(String query) {
        ArrayList<Note> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TITLE + " NOT LIKE '" + query + "%' AND " + COLUMN_TITLE + " LIKE '%" + query + "%'";
        Cursor cursor = db.rawQuery(searchQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
                @SuppressLint("Range") long createdTimestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_TIMESTAMP));
                @SuppressLint("Range") long updatedTimestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_UPDATED_TIMESTAMP));
                dataList.add(new Note(id, title, desc, createdTimestamp, updatedTimestamp));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }
}

