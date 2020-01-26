package com.teamdakatia.rentask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Landing_screen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
    }

    public void rent(View view) {
        Intent intent = new Intent(Landing_screen.this, SignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);

    }

    public void search(View view) {
        Intent intent = new Intent(Landing_screen.this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
