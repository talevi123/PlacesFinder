package com.tal.placesfinder.Activites;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tal.placesfinder.R;

import java.util.Random;

public class QueueActivity extends AppCompatActivity {

    TextView service;
    TextView pepole;
    TextView waitTime;
    ImageView imageView;
    Button mButton;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        Random r = new Random();
        final int wait = r.nextInt(41 - 25) + 25;
        int costumers = 3;

        init();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        int name = b.getInt("Queue");
        int image = b.getInt("image");

        set(image, name, wait, costumers);

//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void set(int image, int name, int wait, int costumers) {
        service.setText(name);

        pepole.setText(String.valueOf(costumers));
        waitTime.setText(String.valueOf(wait));
        Picasso.with(this).load(image).fit().into(imageView);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addNotification();
                Toast.makeText(getApplication().getApplicationContext(),"You the first in line :)",Toast.LENGTH_LONG).show();
            }
        }, wait * 1000);
    }

    private void init() {
//        mButton = (Button) findViewById(R.id.refresh);
        service = (TextView) findViewById(R.id.service);
        pepole = (TextView) findViewById(R.id.pepole);
        waitTime = (TextView) findViewById(R.id.wait);
        imageView = (ImageView) findViewById(R.id.image);
    }


    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.posti)
                        .setContentTitle("SMP")
                        .setContentText("התור שלך הגיע");

        Intent notificationIntent = new Intent(this, Queue2.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
