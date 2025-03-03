package com.example.everafter;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EverAfterDB.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE events (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "user_id INTEGER, " +
                "event_name TEXT, " +
                "event_date TEXT, " + "subject_lists_id INTEGER)";

        db.execSQL(sqlCreate);

        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT, " +
                "password TEXT)";
        db.execSQL(createUsersTable);

        String createSubjectListsTable = "CREATE TABLE subject_lists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "list_name TEXT, " +
                "description TEXT)";
        db.execSQL(createSubjectListsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS subject_lists");
        onCreate(db);
    }
}
