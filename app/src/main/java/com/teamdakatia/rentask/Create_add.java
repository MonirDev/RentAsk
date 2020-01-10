package com.teamdakatia.rentask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

public class Create_add extends AppCompatActivity {
    private ScrollView step1, step2,step3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_add);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);


    }

    public void goNextStep2(View view) {
        step1.setVisibility(View.GONE);
        step2.setVisibility(View.VISIBLE);
        step3.setVisibility(View.GONE);
    }

    public void goNextStep3(View view) {
        step1.setVisibility(View.GONE);
        step3.setVisibility(View.VISIBLE);
        step2.setVisibility(View.GONE);
    }
    public void goPrevStep1(View view) {
        step1.setVisibility(View.VISIBLE);
        step2.setVisibility(View.GONE);
        step3.setVisibility(View.GONE);
    }

    public void goPrevStep2(View view) {
        step1.setVisibility(View.GONE);
        step2.setVisibility(View.VISIBLE);
        step3.setVisibility(View.GONE);
    }
}
