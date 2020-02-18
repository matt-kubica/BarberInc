package com.official.barberinc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    public static final String TABLE_NAME = "visits";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TAG = "tag";

    public DatabaseHelper(Context context) { super(context, TABLE_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATETIME + " TEXT NOT NULL, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_TAG + " INTEGER NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    public boolean addData(Visit item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATETIME, item.getDatetime());
        contentValues.put(COLUMN_NAME, item.getName());
        contentValues.put(COLUMN_TAG, item.getTag());

        long result = db.insert(TABLE_NAME, null, contentValues);

        return (result == -1) ? false : true;
    }

    public ArrayList <Visit> getDataFromCertainDay(String dateString) {
        ArrayList <Visit> results = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE date(" + COLUMN_DATETIME + ") = '" +
                dateString +"' ORDER BY " + COLUMN_DATETIME + " ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            results.add(new Visit(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)));
        }
        return results;
    }
}
