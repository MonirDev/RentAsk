package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerDashboard extends AppCompatActivity {
    private LinearLayout logout;
    private Button createPost;
    private TextView userName,noItem;
    private RecyclerView mRecyclerView;
    ProgressDialog progressDialog;
    List<AddData> postList;

    DatabaseReference databaseReference,postData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        createPost = findViewById(R.id.createPost);
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);
        noItem = findViewById(R.id.noItem);
        mRecyclerView = findViewById(R.id.recyclerView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        postList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Owner");
        postData = FirebaseDatabase.getInstance().getReference("Post");
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getApplicationContext(),1 );
        mRecyclerView.setLayoutManager( gridLayoutManager );

        final String id = getIntent().getExtras().getString("UniqueId");
        databaseReference.orderByChild("phone_number").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    AddData value = ds.getValue(AddData.class);
                    userName.setText(value.getName().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postData.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    postList.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        AddData value = ds.getValue(AddData.class);
                        postList.add(value);
                    }
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(),postList,id);
                    mRecyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }else {
                    noItem.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Create_add.class);
                intent.putExtra("uniqueId", id);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Landing_screen.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to Exit?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), Landing_screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
