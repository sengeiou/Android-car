package com.tgf.kcwc.seek;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "kcwcsearch.db";
    private static Integer version = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200))");
        db.execSQL("create table friend(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}