package com.example.needlevision;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Filter extends AppCompatActivity {
    // 0 to 100 km
    private final static int MAX = 100;
    private int distance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SeekBar bar = findViewById(R.id.distance_slider);
        Intent i = getIntent();

        distance = i.getIntExtra("distance", 0);

        bar.setMax(MAX);
        bar.setProgress(distance);
        ((TextView) findViewById(R.id.distance_val)).setText(distance + " km");

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                ((TextView) findViewById(R.id.distance_val)).setText(String.valueOf(progress) + " km");
                distance = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.filter_return_btn).setOnClickListener(new View.OnClickListener() {
            // Load the loadPagerPage function
            @Override
            public void onClick(View v) {
                Intent filterDetail = new Intent();
                filterDetail.putExtra("distance", distance);
                setResult(Activity.RESULT_OK, filterDetail);
                finish();
            }
        });


    }


}