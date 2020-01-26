package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdListview extends AppCompatActivity {
    private ListView mListView;
    private TextView noItem;
    ProgressDialog progressDialog;
    ArrayList<AddData> adList;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listview);

        mListView = findViewById(R.id.ad_list);
        noItem = findViewById(R.id.noItem);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        adList = new ArrayList<>();

        final String stext = getIntent().getExtras().getString("sText");
        databaseReference = FirebaseDatabase.getInstance().getReference("allPost");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        databaseReference.orderByChild("area_name").equalTo(stext).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    adList.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        AddData value = ds.getValue(AddData.class);
                        adList.add(value);
                    }
                    ListAdapter adapter = new ListAdapter(getApplicationContext(),adList);
                    mListView.setAdapter(adapter);
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AdDetailsCustomer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("img1",adList.get(i).getImgUrl1());
                intent.putExtra("img2",adList.get(i).getImgUrl2());
                intent.putExtra("img3",adList.get(i).getImgUrl3());
                intent.putExtra("home_type",adList.get(i).getHome_type());
                intent.putExtra("fullAddress",adList.get(i).getShort_address());
                intent.putExtra("price",adList.get(i).getPrice()+" TK");
                intent.putExtra("room",adList.get(i).getnRooms());
                intent.putExtra("bath",adList.get(i).getnBath());
                intent.putExtra("contact",adList.get(i).getPost_phone_number());
                intent.putExtra("floor",adList.get(i).getFloorN() + " Floor");
                intent.putExtra("rent",adList.get(i).getRent_start());
                intent.putExtra("checkvalue",adList.get(i).getCheck_value());
                intent.putExtra("division",adList.get(i).getDivision());
                intent.putExtra("district",adList.get(i).getDistrict());
                intent.putExtra("area",adList.get(i).getArea_name());
                startActivity(intent);

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
