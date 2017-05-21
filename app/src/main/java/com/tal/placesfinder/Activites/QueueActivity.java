package com.tal.placesfinder.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        Random r = new Random();
        int wait = r.nextInt(41 - 25) + 25;
        int costumers = r.nextInt(6 - 1) + 1;

        init();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        int name = b.getInt("Queue");
        int image = b.getInt("image");

        set(image, name, wait, costumers);
    }

    private void set(int image, int name, int wait, int costumers) {
        service.setText(name);
        pepole.setText(String.valueOf(costumers));
        waitTime.setText(String.valueOf(wait));
        Picasso.with(this).load(image).fit().into(imageView);

        startTime = System.nanoTime();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QueueActivity.this, "This is a delay message", Toast.LENGTH_SHORT).show();
            }
        }, wait * 1000);
    }

    private void init() {
        service = (TextView) findViewById(R.id.service);
        pepole = (TextView) findViewById(R.id.pepole);
        waitTime = (TextView) findViewById(R.id.wait);
        imageView = (ImageView) findViewById(R.id.image);
    }




}
