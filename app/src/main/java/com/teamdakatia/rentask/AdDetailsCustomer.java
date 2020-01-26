package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class AdDetailsCustomer extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private ImageView img1, img2, img3;
    private Button direction, contactN;
    private TextView hometype, full_address,price, nRoom, nBath, floorN, rent,address, checkbox_value;
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details_customer);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        hometype = findViewById(R.id.home_type);
        full_address = findViewById(R.id.fullAddress);
        price = findViewById(R.id.mPrice);
        nRoom = findViewById(R.id.nRoom);
        nBath = findViewById(R.id.nBath);
        floorN = findViewById(R.id.nFloor);
        rent = findViewById(R.id.rent);
        address = findViewById(R.id.address);
        checkbox_value = findViewById(R.id.checkboxValue);
        direction = findViewById(R.id.btnDelete);
        contactN = findViewById(R.id.contactN);


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
            num = bundle.getString("contact");
            floorN.setText(bundle.getString("floor"));
            rent.setText(bundle.getString("rent"));
            address.setText(bundle.getString("area") + ", " +
                            bundle.getString("district") + ", " +
                            bundle.getString("division"));
            checkbox_value.setText(bundle.getString("checkvalue"));
        }

        contactN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               makePhoneCall();

            }
        });
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(AdDetailsCustomer.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdDetailsCustomer.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + num;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
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
