package com.tal.placesfinder.Receivers;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.widget.Toast;

/**
 * Created by tal on 14/12/16.
 */
public class ChargingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "Phone is connected to power", Toast.LENGTH_SHORT).show();
        }
        else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, "Phone is disconnected from power", Toast.LENGTH_SHORT).show();
        }
    }
}
