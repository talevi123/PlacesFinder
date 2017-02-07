package com.tal.placesfinder.Receivers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.tal.placesfinder.PlacesApplication;

/**
 * Created by tal on 14/12/16.
 */
public class ConnectionReceiver extends BroadcastReceiver {

    public ConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean noConnection = intent.getBooleanExtra("noConnectivity", false);
        if (noConnection) {
            showSettingsAlert();
        }
    }

    public static void showSettingsAlert() {

        final Activity currentActivity = PlacesApplication.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(currentActivity);
            alertDialog.setTitle("Network settings");
            alertDialog.setMessage("Network is not enabled. Do you want to go to settings menu?");

            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    currentActivity.startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }
}
