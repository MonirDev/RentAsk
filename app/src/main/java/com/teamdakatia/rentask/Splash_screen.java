package com.teamdakatia.rentask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Splash_screen extends AppCompatActivity {
    ProgressBar progressBar;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBarID);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });
        thread.start();
    }
    //for progressBar progressing
    private void doWork() {
        for(progress=20; progress<=100; progress = progress+40){
            try{
                Thread.sleep(3000);
                progressBar.setProgress((progress));
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    //Where to go after splash screen
    private void startApp() {
        Intent intent = new Intent(Splash_screen.this, Landing_screen.class);
        startActivity(intent);
        finish();
    }
}
