package com.micro_gis.microgistracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oleg on 01.06.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "nmea text);");

        db.execSQL("create table trackdata ("
                + "id integer primary key autoincrement, name text,"
                + "track BLOB);");

        db.execSQL("create table markers ("
                + "id integer primary key autoincrement, latlng text, data BLOB);");

        db.execSQL("create table requestgroup ("
                + "id integer primary key autoincrement, groupname text, account text, keyString text, url text, requestInterval text, groups text, timeZone text);");

        db.execSQL("create table messages ("
                + "id integer primary key autoincrement, time integer, message text, isSeen integer, voyageId integer);");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
