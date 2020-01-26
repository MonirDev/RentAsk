package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AdDetails extends AppCompatActivity {

    DatabaseReference databaseReference, databaseReferenceall;
    private ImageView img1, img2, img3;
    private Button btnDelete;
    private TextView hometype, full_address,price, nRoom, nBath, contactN, floorN, rent, checkbox_value;
    String key,id;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        hometype = findViewById(R.id.home_type);
        full_address = findViewById(R.id.fullAddress);
        price = findViewById(R.id.mPrice);
        nRoom = findViewById(R.id.nRoom);
        nBath = findViewById(R.id.nBath);
        contactN = findViewById(R.id.contactN);
        floorN = findViewById(R.id.nFloor);
        rent = findViewById(R.id.rent);
        checkbox_value = findViewById(R.id.checkboxValue);
        btnDelete = findViewById(R.id.btnDelete);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String imgurl1 = bundle.getString("img1");
            String imgurl2 = bundle.getString("img2");
            String imgurl3 = bundle.getString("img3");
            Picasso.with(this).load(imgurl1).into(img1);
            Picasso.with(this).load(imgurl2).into(img2);
            Picasso.with(this).load(imgurl3).into(img3);
            hometype.setText(bundle.getString("home_type"));
            full_address.setText(bundle.getString("fullAddress"));
            price.setText(bundle.getString("price"));
            nRoom.setText(bundle.getString("room"));
            nBath.setText(bundle.getString("bath"));
            contactN.setText(bundle.getString("contact"));
            floorN.setText(bundle.getString("floor"));
            rent.setText(bundle.getString("rent"));
            checkbox_value.setText(bundle.getString("checkvalue"));
            key = bundle.getString("key");
            id = bundle.getString("id");
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Post").child(id);
        databaseReferenceall = FirebaseDatabase.getInstance().getReference("allPost");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceall.orderByChild("postId").equalTo(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                databaseReference.orderByChild("postId").equalTo(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(AdDetails.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),OwnerDashboard.class);
                        intent.putExtra("UniqueId",id);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
