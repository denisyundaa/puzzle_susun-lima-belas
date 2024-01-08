package com.example.susunlimabelas15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private int emptyX = 3; //menyimpan koordinat tile kosong
    private int emptyY = 3; //menyimpan koordinat tile kosong
    private RelativeLayout group; //menampung semua tile
    private Button[][] buttons;
    private int[] tiles; //array yang menyimpan nilai tile
    private TextView textViewSteps;
    private int stepCount=0;
    private TextView textViewTime;
    private Timer timer;
    private int timeCount=0;
    private Button buttonShuffle;
    private Button buttonStop;
    private boolean isTimeRunning;
    private MyBase myBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadViews();
        loadNumbers();
        generateNumbers();
        loadDatatoViews();
    }

    //memperbarui tampilan dengan nilai terbaru dari data
    private void loadDatatoViews() {
        //mengatur koordinat tile kosong ke (3, 3)
        emptyX = 3;
        emptyY = 3;
        for (int i = 0; i < group.getChildCount() - 1; i++) {
            buttons[i / 4][i % 4].setText(String.valueOf(tiles[i]));
            buttons[i / 4][i % 4].setBackgroundResource(android.R.drawable.btn_default);
        }
        buttons[emptyX][emptyY].setText("");
        buttons[emptyX][emptyY].setBackgroundColor(ContextCompat.getColor(this, R.color.colorFreeButton));
    }

    //menghasilkan urutan angka acak untuk tile-tile
    private void generateNumbers() {
        int n = 15;
        Random random = new Random();
        while (n > 1) {
            int randomNum = random.nextInt(n--);
            int temp = tiles[randomNum];
            tiles[randomNum] = tiles[n];
            tiles[n] = temp;
        }
        if (!isSolvable())
            generateNumbers();
    }

    private boolean isSolvable() {
        int countInversions = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }
        return countInversions % 2 == 0;
    }

    private void loadNumbers() {
        //alokasi memori utk array tiles
        tiles = new int[16];
        //masukkan nilai dari 1 hingga 15 ke dalam array tiles
        for (int i = 0; i < group.getChildCount() - 1; i++) {
            tiles[i] = i + 1;
        }
    }

    private void loadTimer(){
        //atur flag isTimeRunning menjadi true
        isTimeRunning=true;
        timer=new Timer();
        //jadwalkan task timer untuk dijalankan setiap 1 detik
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeCount++;
                setTime(timeCount);
            }
        },1000,1000);
    }

    private void setTime(int timeCount){
        //menghitung jam, menit, dan detik dari timeCount
        int second=timeCount%60;
        int hour=timeCount/3600;
        int minute=(timeCount-hour*3600)/60;

        //format dan tampilkan waktu ke textViewTime
        textViewTime.setText(String.format("Waktu: %02d:%02d:%02d",hour,minute,second));
    }

    private void loadViews() {
        group = findViewById(R.id.group);
        textViewSteps=findViewById(R.id.text_view_steps);
        textViewTime=findViewById(R.id.text_view_time);
        buttonShuffle=findViewById(R.id.button_shuffle);
        buttonStop=findViewById(R.id.button_stop);

        loadTimer();
        buttons = new Button[4][4];

        for (int i = 0; i < group.getChildCount(); i++) {
            buttons[i / 4][i % 4] = (Button) group.getChildAt(i);
        }
        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNumbers();
                loadDatatoViews();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimeRunning) {
                    timer.cancel();
                    buttonStop.setText("Lanjutkan");
                    isTimeRunning = false;
                    for (int i = 0; i < group.getChildCount(); i++) {
                        buttons[i / 4][i % 4].setClickable(false);
                    }
                }else{
                    loadTimer();
                    buttonStop.setText("Berhenti");
                    for (int i = 0; i < group.getChildCount(); i++) {
                        buttons[i / 4][i % 4].setClickable(true);
                    }
                }
            }
        });
    }

    public void buttonClick(View view) {
        Button button = (Button) view;
        //mendapatkan koordinat x dan y dari tombol yang diklik
        int x = button.getTag().toString().charAt(0) - '0'; // mengambil karakter pertama dari tag tombol (karakter x)
        int y = button.getTag().toString().charAt(1) - '0'; // mengambil karakter kedua dari tag tombol (karakter y)

        //memeriksa apakah tile yang diklik bersebelahan dengan tile kosong
        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            buttons[emptyX][emptyY].setText(button.getText().toString());
            buttons[emptyX][emptyY].setBackgroundResource(android.R.drawable.btn_default);
            button.setText("");
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorFreeButton));
            emptyX = x;
            emptyY = y;
            stepCount++;
            textViewSteps.setText("Langkah: "+stepCount);
            checkWin();
        }
    }

    //mengecek kemenangan
    private void checkWin() {
        boolean isWin = false;
        if (emptyX == 3 && emptyY == 3) {
            for (int i = 0; i < group.getChildCount() - 1; i++) {
                if (buttons[i / 4][i % 4].getText().toString().equals(String.valueOf(i + 1))) {
                    isWin = true;
                } else {
                    isWin = false;
                    break;
                }
            }
        }
        //jika puzzle sudah terpecahkan, maka akan menampilkan
        if(isWin){
            Toast.makeText(this,"BERHASIL MENANG!\nLangkah: "+stepCount, Toast.LENGTH_SHORT).show();
            for(int i=0; i<group.getChildCount(); i++){
               buttons[i/4][i%4].setClickable(false);
            }
            timer.cancel();
            buttonShuffle.setClickable(false);
            buttonStop.setClickable(false);
            saveData();
        }
    }
    private void saveData(){
        myBase = new MyBase(GameActivity.this);
        myBase.saveLastStep(stepCount);
        myBase.saveLastTime(timeCount);
        if(myBase.getBestStep()!=0){
            if(myBase.getBestStep()>stepCount)
                myBase.saveBestStep(stepCount);
        } else
            myBase.saveBestStep(stepCount);

        if(myBase.getBestTime()!=0){
            if(myBase.getBestTime()>timeCount)
                myBase.saveBestTime(timeCount);
        } else
            myBase.saveBestTime(timeCount);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(MainActivity.REQUEST_CODE);
    }
}