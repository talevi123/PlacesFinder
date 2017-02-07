package com.tal.placesfinder.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tal on 11/12/16.
 */
public class PlacesSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "placesFinder.db";
    private static final int DATABASE_VERSION = 1;

    public PlacesSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public static final String TABLE_FAVORITE = "favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADRESS = "adress";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_DISTANCE = "distance";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_FAVORITE +
            " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_ADRESS + " TEXT, " +
            COLUMN_PHOTO + " TEXT, " +
            COLUMN_DISTANCE + " TEXT" +
            " )";
}
