package com.tal.placesfinder.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tal.placesfinder.Moduls.Place;
import com.tal.placesfinder.R;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;
    TextView city;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        place = (Place) intent.getExtras().getSerializable("Place");

        initBtn();

        assert place != null;
        city.setText(place.getVicinity());
    }

    public void initBtn() {
        city = (TextView) findViewById(R.id.city);
        btn1 = (ImageButton) findViewById(R.id.btn1);
        btn2 = (ImageButton) findViewById(R.id.btn2);
        btn3 = (ImageButton) findViewById(R.id.btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    private void sendQueue(int name, int image) {
        Intent intent = new Intent(ServiceActivity.this, QueueActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("Queue", name);
//        bundle.putInt("wait", wait);
        bundle.putInt("image", image);
        bundle.putSerializable("Pla", place);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                sendQueue(R.string.asnav, R.drawable.acustomimage);
                break;
            case R.id.btn2:
                sendQueue(R.string.amara, R.drawable.amara);
                break;
            case R.id.btn3:
                sendQueue(R.string.mesluh, R.drawable.box);
        }
    }
}
