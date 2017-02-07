package com.tal.placesfinder;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by tal on 22/12/16.
 */
public class PlacesApplication extends Application {

    private static PlacesApplication instance;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
    }

    public static PlacesApplication getInstance() {
        return instance;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
