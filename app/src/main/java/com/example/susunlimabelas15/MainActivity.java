package com.example.susunlimabelas15;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    private Button buttonStartGame;
    private TextView textLastStep;
    private TextView textBestStep;
    private TextView textLastTime;
    private TextView textBestTime;
    private MyBase myBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartGame = findViewById(R.id.button_start_game);
        textLastStep = findViewById(R.id.text_last_step);
        textBestStep = findViewById(R.id.text_best_step);
        textLastTime = findViewById(R.id.text_last_time);
        textBestTime = findViewById(R.id.text_best_time);

        myBase = new MyBase(this);

        loadData();

        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,GameActivity.class),REQUEST_CODE);
            }
        });
    }

    private void loadData(){
        textLastStep.setText(String.valueOf(myBase.getLastStep()));
        textBestStep.setText(String.valueOf(myBase.getBestStep()));

        int lastTime = myBase.getLastTime();
        int lastSecond = lastTime %60;
        int lastHour = lastTime/3600;
        int lastMinute = (lastTime-lastHour*3600)/60;
        textLastTime.setText(String.format("%02d:%02d:%02d",lastHour,lastMinute,lastSecond));

        int bestTime = myBase.getBestTime();
        int bestSecond = bestTime %60;
        int bestHour = bestTime/3600;
        int bestMinute = (bestTime-bestHour*3600)/60;
        textBestTime.setText(String.format("%02d:%02d:%02d",bestHour,bestMinute,bestSecond));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            textLastStep.setText(String.valueOf(myBase.getLastStep()));
            textBestStep.setText(String.valueOf(myBase.getBestStep()));

            int lastTime = myBase.getLastTime();
            int lastSecond = lastTime %60;
            int lastHour = lastTime/3600;
            int lastMinute = (lastTime-lastHour*3600)/60;
            textLastTime.setText(String.format("%02d:%02d:%02d",lastHour,lastMinute,lastSecond));

            int bestTime = myBase.getBestTime();
            int bestSecond = bestTime %60;
            int bestHour = bestTime/3600;
            int bestMinute = (bestTime-bestHour*3600)/60;
            textBestTime.setText(String.format("%02d:%02d:%02d",bestHour,bestMinute,bestSecond));
        }
    }
}