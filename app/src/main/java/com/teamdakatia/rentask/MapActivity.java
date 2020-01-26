package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private ImageView search;
    private AutoCompleteTextView search_bar;
    private String near = "";
    private RecyclerView mREcyclerView;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List<AddData> mList;
    TextView notfound;

    /*GoogleMap mMap;*/
    final String area_name[] = {"Mirpur","Uttara","Gulshan","Muhammadpur","Dhanmondi","Bashundhara","Baridhara","Elephant Road",
            "Jatrabari","Badda","Motijheel","Savar","Rampura","Malibag","Farmgate","Khilgaon","Purbachal","Mogbazar","Banani","Tejgaon",
            "Banglamoror","Paltan","Rama","Keraniganj","Lalbag","Tongi","Basabo","Cantonment","Khilkhet","Mohakhali","Wari","Sutrapur",
            "Demra","Bangshal","Chaukbazar","New Market","Kamrangirchar","Hazaribagh","Kafrul","Dhamrai","Kotwali","Nawabganj","Dohar",
            "Adabor","Airport","Dakshinkhan","Darus salam","Bhasan tek","Bhatara","Gendaria","Kolabagan","Sobujbagh","Shahjahanpur"};
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        notfound = findViewById(R.id.notfound);
        search_bar = findViewById(R.id.search_bar);
        search = findViewById(R.id.search);
        mREcyclerView = findViewById(R.id.recyclerView);


        databaseReference = FirebaseDatabase.getInstance().getReference("allPost");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        mList = new ArrayList<>();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, area_name);
        search_bar.setAdapter(adapter1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MapActivity.this,LinearLayoutManager.HORIZONTAL,false);
        mREcyclerView.setLayoutManager(layoutManager);
        mREcyclerView.setItemAnimator(new DefaultItemAnimator());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = search_bar.getText().toString();
                Intent intent = new Intent(getApplicationContext(), AdListview.class);
                intent.putExtra("sText" , searchText);
                startActivity(intent);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        String mCity = getCityName(latLng);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        googleMap.addMarker(markerOptions);
        googleMap.setMyLocationEnabled(true);
        progressDialog.dismiss();


    }

    private String getCityName(LatLng latLng) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        try {
            List<Address> addresses =geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            near = myCity;
            databaseReference.orderByChild("district").equalTo(near).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        mList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            AddData value = ds.getValue(AddData.class);
                            mList.add(value);
                        }
                        AdapterNearest adapterNearest = new AdapterNearest(getApplicationContext(),mList);
                        mREcyclerView.setAdapter(adapterNearest);
                        progressDialog.dismiss();

                    }else {
                        notfound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

}
