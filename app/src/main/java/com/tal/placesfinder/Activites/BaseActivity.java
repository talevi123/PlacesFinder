package com.tal.placesfinder.Activites;

import android.support.v7.app.AppCompatActivity;

import com.tal.placesfinder.PlacesApplication;

/**
 * Created by tal on 22/12/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        PlacesApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        PlacesApplication.getInstance().setCurrentActivity(null);
        super.onPause();
    }
}
