package com.awarmisland.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.awarmisland.customview.waveView.WaveView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WaveView view_wave = findViewById(R.id.view_wave);
        view_wave.startAnimation();
    }
}
