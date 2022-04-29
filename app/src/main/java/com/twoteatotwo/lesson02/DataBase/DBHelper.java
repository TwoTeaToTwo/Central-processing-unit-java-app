package com.twoteatotwo.lesson02.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "listDb";
    public static final String TABLE_NAMES_ELBRUS = "lists";
    public static final String TABLE_ELBRUS = "description";
    public static final String TABLE_BAIKAL = "descriptionb";
    public static final String TABLE_NAMES_BAIKAL = "listsb";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "arrayname";



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAMES_ELBRUS + "(" + KEY_ID
        + " integer primary key," + KEY_NAME + " text" + ")");

        sqLiteDatabase.execSQL("create table " + TABLE_ELBRUS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text" + ")");

        sqLiteDatabase.execSQL("create table " + TABLE_NAMES_BAIKAL + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text" + ")");

        sqLiteDatabase.execSQL("create table " + TABLE_BAIKAL + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
