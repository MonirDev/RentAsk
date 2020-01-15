package com.teamdakatia.rentask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OwnerDashboard extends AppCompatActivity {
    private Button createPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        createPost = findViewById(R.id.createPost);



        final String id = getIntent().getExtras().getString("UniqueId");
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Create_add.class);
                intent.putExtra("uniqueId", id);
                startActivity(intent);
            }
        });

    }
}
