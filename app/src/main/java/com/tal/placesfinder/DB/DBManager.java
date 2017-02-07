package com.tal.placesfinder.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tal.placesfinder.Moduls.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tal on 11/12/16.
 */
public class DBManager {

    private static DBManager instance;

    private SQLiteDatabase database;
    private PlacesSQLiteHelper dbHelper;
    private String[] allColumns = { PlacesSQLiteHelper.COLUMN_ID, PlacesSQLiteHelper.COLUMN_NAME,
                                    PlacesSQLiteHelper.COLUMN_ADRESS, PlacesSQLiteHelper.COLUMN_PHOTO,
                                    PlacesSQLiteHelper.COLUMN_DISTANCE };

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    private DBManager(Context context) {
        dbHelper = new PlacesSQLiteHelper(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean checkIfExsists(String name) {
        Cursor c = database.query(PlacesSQLiteHelper.TABLE_FAVORITE, null, PlacesSQLiteHelper.COLUMN_NAME + " =? ",
                new String[]{name}, null, null, null);
        return c.getCount() > 0;
    }

    public void addToFav(Place place) {
        ContentValues cv = new ContentValues();
        cv.put(PlacesSQLiteHelper.COLUMN_NAME, place.getName());
        cv.put(PlacesSQLiteHelper.COLUMN_ADRESS, place.getVicinity());
        cv.put(PlacesSQLiteHelper.COLUMN_PHOTO, place.getPhoto());
        cv.put(PlacesSQLiteHelper.COLUMN_DISTANCE, place.getDistance());
        database.insert(PlacesSQLiteHelper.TABLE_FAVORITE, null, cv);
    }

    public void deleteFavorites() {
        database.delete(PlacesSQLiteHelper.TABLE_FAVORITE, null, null);
    }

    public List<Place> getAllFav(){
        List<Place> places = new ArrayList<>();

        Cursor cursor = database.query(PlacesSQLiteHelper.TABLE_FAVORITE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Place place = new Place(cursor);
            places.add(place);
            cursor.moveToNext();
        }
        cursor.close();
        return places;
    }
}