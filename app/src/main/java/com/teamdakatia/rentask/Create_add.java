package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Create_add extends AppCompatActivity {
    private Button btnPublish,pick_location;
    private ScrollView step1, step2,step3;
    private ImageView img1,img2,img3;
    private AutoCompleteTextView home_type,price,number_room,number_bath,division,district,areaName,rent_start;
    private EditText short_address,post_phoneNumber;
    private CheckBox checkLift, checkWifi, checkParking, checkCctv, checkGas, checkFire;

    private String img_url1="",img_url2="",img_url3="", lati, lon;
    private Uri file_path1, file_path2,file_path3;
    boolean i1 = false,i2 = false,i3 = false,get_location = false;
    private String checkValue = "Facility: ";
    ArrayList<String> checkList = new ArrayList<>();

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private static  final int REQUEST_LOCATION=1;
    private LocationManager locationManager;

    private final int PICK_IMAGE_REQUEST1 = 71;
    private final int PICK_IMAGE_REQUEST2 = 72;
    private final int PICK_IMAGE_REQUEST3 = 73;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_add);



        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        home_type = findViewById(R.id.homeType);
        price = findViewById(R.id.price);
        number_room = findViewById(R.id.numberRoom);
        number_bath = findViewById(R.id.numberBath);
        division = findViewById(R.id.division);
        district = findViewById(R.id.district);
        areaName = findViewById(R.id.areaName);
        short_address = findViewById(R.id.shortAddress);
        pick_location = findViewById(R.id.pickLocation);
        post_phoneNumber = findViewById(R.id.post_phoneNumber);
        rent_start = findViewById(R.id.rentStart);
        checkLift = findViewById(R.id.checkLift);
        checkWifi = findViewById(R.id.checkWifi);
        checkCctv = findViewById(R.id.checkCctv);
        checkParking = findViewById(R.id.checkParking);
        checkGas = findViewById(R.id.checkGas);
        checkFire = findViewById(R.id.checkFire);
        btnPublish = findViewById(R.id.btnPublish);

        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");

        final String idExit = getIntent().getExtras().getString("uniqueId");
        step1.setVisibility(View.VISIBLE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");

        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        imageSelection();
        checkboxItem();

        pick_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //Check gps is enable or not
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Write Function To enable gps
                    OnGPS();
                }else {
                    //GPS is already On then
                    getLocation();
                }
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mHome_type = home_type.getText().toString().trim();
                String mPrice = price.getText().toString().trim();
                String mNumberRoom = number_room.getText().toString().trim();
                String mNumberBath = number_bath.getText().toString().trim();
                String mDivision = division.getText().toString().trim();
                String mDistrict = district.getText().toString().trim();
                String mAreaName = areaName.getText().toString().trim();
                String mShortAddress = short_address.getText().toString();
                String mPost_phoneNumber = post_phoneNumber.getText().toString().trim();
                String mRentStart = rent_start.getText().toString().trim();
                checkValue = checkList.get(0)+ checkList.get(1)+ checkList.get(2)+
                        checkList.get(3)+ checkList.get(4)+ checkList.get(5);
                if (!TextUtils.isEmpty(mPost_phoneNumber) && mPost_phoneNumber.length() == 11){
                    if (!TextUtils.isEmpty(mRentStart)){
                        AddData setData = new AddData(img_url1,img_url2,img_url3,mHome_type,mPrice,
                                mNumberRoom,mNumberBath,mDivision,mDistrict,mAreaName,mShortAddress,lati,lon,
                                mPost_phoneNumber,mRentStart,checkValue);
                        String currentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        databaseReference.child(idExit).child(currentDateTime).setValue(setData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), OwnerDashboard.class);
                                    intent.putExtra("UniqueId", idExit);
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create_add.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        rent_start.setError("Select the month when rent start!");
                    }
                }else {
                    post_phoneNumber.setError("Input 11 digit number");
                }
            }
        });


    }

    private void checkboxItem() {
        checkLift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(0, "Lift");
                }else {
                    checkList.set(0,"");
                }
            }
        });
        checkWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(1,"Wifi");
                }else {
                    checkList.set(1,"");
                }
            }
        });
        checkParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(2,"Car Parking");
                }else {
                    checkList.set(2,"");
                }
            }
        });
        checkCctv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(3,"CCTV");
                }else {
                    checkList.set(3,"");
                }
            }
        });
        checkGas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(4,"Gas");
                }else {
                    checkList.set(4,"");
                }
            }
        });
        checkFire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(5,"Fire");
                }else {
                    checkList.set(5,"");
                }
            }
        });
    }

    private void imageSelection() {
        img1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST3);
            }
        });
    }

    private void fieldFocus() {
        home_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_type.setFocusable(true);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price.setFocusable(true);
            }
        });
        number_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_room.setFocusable(true);
            }
        });
        number_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_bath.setFocusable(true);
            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                division.setFocusable(true);
            }
        });
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                district.setFocusable(true);
            }
        });
        areaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaName.setFocusable(true);
            }
        });
        short_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short_address.setFocusable(true);
            }
        });
        rent_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rent_start.setFocusable(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK
         && data != null && data.getData() != null){
            file_path1 = data.getData();
            i1 = true;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path1);
                img1.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            i2 = true;
            file_path2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path2);
                img2.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            i3 = true;
            file_path3 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path3);
                img3.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void goNextStep2(View view) {
        upload();
    }

    private void upload() {
        if (i1 == true && i2 == true && i3 == true){
            String mHome_type = home_type.getText().toString().trim();
            String mPrice = price.getText().toString().trim();
            String mNumberRoom = number_room.getText().toString().trim();
            String mNumberBath = number_bath.getText().toString().trim();
            if (!TextUtils.isEmpty(mHome_type)){
                if (!TextUtils.isEmpty(mPrice)){
                    if (!TextUtils.isEmpty(mNumberRoom)){
                        if (!TextUtils.isEmpty(mNumberBath)){
                            step1.setVisibility(View.GONE);
                            step2.setVisibility(View.VISIBLE);
                            step3.setVisibility(View.GONE);
                            StorageReference ref1 = storageReference.child("images/" + "monir/" + "1");
                            StorageReference ref2 = storageReference.child("images/" + "monir/" + "2");
                            StorageReference ref3 = storageReference.child("images/" + "monir/" + "3");
                            ref1.putFile(file_path1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downlaodUri = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!downlaodUri.isComplete()) ;
                                    Uri uri = downlaodUri.getResult();
                                    img_url1 = uri.toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                }
                            });
                            ref2.putFile(file_path2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downlaodUri = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!downlaodUri.isComplete()) ;
                                    Uri uri = downlaodUri.getResult();
                                    img_url2 = uri.toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                }
                            });
                            ref3.putFile(file_path3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downlaodUri = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!downlaodUri.isComplete()) ;
                                    Uri uri = downlaodUri.getResult();
                                    img_url3 = uri.toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            number_bath.setError("Number of Bath can't be Empty");
                        }
                    }else {
                        number_room.setError("Number of Room can't be Empty");
                    }
                }else {
                    price.setError("Price can't be Empty");
                }
            }else {
                home_type.setError("Home Type can't be Empty");
            }
        }else {
            Toast.makeText(this, "You must select three image!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goNextStep3(View view) {
        String mDivision = division.getText().toString().trim();
        String mDistrict = district.getText().toString().trim();
        String mAreaName = areaName.getText().toString().trim();
        String mShortAddress = short_address.getText().toString();
        if (!TextUtils.isEmpty(mDivision)){
            if (!TextUtils.isEmpty(mDistrict)){
                if (!TextUtils.isEmpty(mAreaName)){
                    if (!TextUtils.isEmpty(mShortAddress)){
                        if (get_location == true){
                            step1.setVisibility(View.GONE);
                            step3.setVisibility(View.VISIBLE);
                            step2.setVisibility(View.GONE);
                        }else {
                            pick_location.setError("Pick your home location");
                        }
                    }else {
                        short_address.setError("Give a short address to your home");
                    }
                }else {
                    areaName.setError("Select Area Name!");
                }
            }else {
                district.setError("Select District!");
            }
        }else {
            division.setError("Select Division!");
        }
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

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(Create_add.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Create_add.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                get_location = true;
                lati=String.valueOf(lat);
                lon=String.valueOf(longi);
                pick_location.setText("Done!");
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();
                get_location = true;
                lati=String.valueOf(lat);
                lon=String.valueOf(longi);
                pick_location.setText("Done!");
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();
                get_location = true;
                lati=String.valueOf(lat);
                lon=String.valueOf(longi);
                pick_location.setText("Done!");
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void OnGPS() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Enable Location").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
