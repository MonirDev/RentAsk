package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Create_add extends AppCompatActivity{
    private Button btnPublish,pick_location;
    private ScrollView step1, step2,step3;
    private ImageView img1,img2,img3;
    private AutoCompleteTextView home_type,number_room,number_bath,floorN,division,district,areaName,rent_start;
    private EditText price,short_address,post_phoneNumber;
    private CheckBox checkLift, checkWifi, checkParking, checkCctv, checkGas, checkFire;

    private String img_url1="",img_url2="",img_url3="", lati, lon,cArea;
    private Uri file_path1, file_path2,file_path3;
    boolean i1 = false,i2 = false,i3 = false,get_location = false, c1 = false,c2 = false,c3 = false;
    private String checkValue = "Facility: ";
    ArrayList<String> checkList = new ArrayList<>();
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    ProgressDialog progressDialog;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference,databaseReferenceall;

    private static  final int REQUEST_LOCATION=1;
    private LocationManager locationManager;

    private final int PICK_IMAGE_REQUEST1 = 71;
    private final int PICK_IMAGE_REQUEST2 = 72;
    private final int PICK_IMAGE_REQUEST3 = 73;
    byte[] imageInByte1,imageInByte2,imageInByte3;
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
        floorN = findViewById(R.id.floorN);
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Images...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");
        checkList.add(" ");

        final String idExit = getIntent().getExtras().getString("uniqueId");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        databaseReferenceall = FirebaseDatabase.getInstance().getReference("allPost");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

                ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        dropDown();
        imageSelection();
        checkboxItem();


        pick_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder= new AlertDialog.Builder(Create_add.this);
                builder.setMessage("Are you in your exact TO-LET location, where your TO-LET home situated??").setCancelable(false).setPositiveButton("YES, get my location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        //Check gps is enable or not
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            //Write Function To enable gps
                            OnGPS();
                        }else {
                            //GPS is already On then
                            getLocation();
                            pick_location.setText("Done!");

                        }
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Create_add.this, "You have to be your TO-LET location to create your TO-LET ", Toast.LENGTH_SHORT).show();

                        dialog.cancel();
                    }
                });
                final AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });

        short_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (get_location==true) {
                    LatLng latLng = new LatLng(Double.parseDouble(lati), Double.parseDouble(lon));
                    getFullAddress(latLng);
                }else {
                    pick_location.setError("Pick your Location First!");
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
                String mFloorN = floorN.getText().toString().trim();
                String mDivision = division.getText().toString().trim();
                String mDistrict = district.getText().toString().trim();
                String mAreaName = areaName.getText().toString().trim();
                String mShortAddress = short_address.getText().toString();
                String mPost_phoneNumber = post_phoneNumber.getText().toString().trim();
                String mRentStart = rent_start.getText().toString().trim();
                String postId= UUID.randomUUID().toString();
                checkValue = checkList.get(0)+ checkList.get(1)+ checkList.get(2)+
                        checkList.get(3)+ checkList.get(4)+ checkList.get(5);
                if (!TextUtils.isEmpty(mPost_phoneNumber) && mPost_phoneNumber.length() == 11){
                    if (!TextUtils.isEmpty(mRentStart)){
                        AddData setData = new AddData(img_url1,img_url2,img_url3,mHome_type,mPrice,
                                mNumberRoom,mNumberBath,mDivision,mDistrict,mAreaName,mShortAddress,lati,lon,
                                mPost_phoneNumber,mRentStart,checkValue,postId,mFloorN,cArea);
                        String currentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        databaseReferenceall.child(currentDateTime).setValue(setData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create_add.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
                            }
                        });
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




    private void getFullAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(Create_add.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            String t1 = addresses.get(0).getFeatureName();
            String t2 = addresses.get(0).getThoroughfare();
            String t3 = addresses.get(0).getSubLocality();
            String t4 = addresses.get(0).getLocality();
            cArea = t3;
            String fullAddress = t1 + ", " + t2 + ", " + t3 + ", " + t4;
            short_address.setText(fullAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dropDown() {
        home_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        Create_add.this,R.array.home_type_array,android.R.layout.simple_dropdown_item_1line
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                home_type.setAdapter(adapter);
                home_type.showDropDown();
            }
        });
        number_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        Create_add.this,R.array.numberRoom_array,android.R.layout.simple_dropdown_item_1line
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                number_room.setAdapter(adapter);
                number_room.showDropDown();
            }
        });
        number_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        Create_add.this,R.array.numberBath_array,android.R.layout.simple_dropdown_item_1line
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                number_bath.setAdapter(adapter);
                number_bath.showDropDown();
            }
        });
        floorN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        Create_add.this,R.array.floorN,android.R.layout.simple_dropdown_item_1line
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                floorN.setAdapter(adapter);
                floorN.showDropDown();
            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        Create_add.this,R.array.division_array,android.R.layout.simple_dropdown_item_1line
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                division.setAdapter(adapter);
                division.showDropDown();
            }
        });
        rent_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        Create_add.this,R.array.rent_start,android.R.layout.simple_dropdown_item_1line
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rent_start.setAdapter(adapter);
                rent_start.showDropDown();
            }
        });
        final String district_array[] = {"Barguna","Barisal","Bhola","Jhalokati","Patuakhali","Pirojpur","Bandarban","Brahmanbaria",
                "Chandpur","Chittagong", "Comilla","Cox's Bazar","Feni","Khagrachhari","Lakshmipur","Noakhali","Rangamati","Dhaka",
                "Faridpur","Gazipur","Gopalganj","Kishoreganj", "Madaripur","Manikganj","Munshiganj","Narayanganj","Narsingdi",
                "Rajbari","Shariatpur","Tangail","Bagerhat","Chuadanga","Jessore","Jhenaidah", "Khulna","Kushtia","Magura","Meherpur",
                "Narail","Satkhira","Jamalpur","Mymensingh","Netrokona","Sherpur","Bogra","Joypurhat","Naogaon", "Natore",
                "Chapai Nawabganj","Pabna","Rajshahi","Sirajganj","Dinajpur","Gaibandha","Kurigram","Lalmonirhat","Nilphamari","Panchagarh",
                "Rangpur","Thakurgaon","Habiganj","Moulvibazar","Sunamganj","Sylhet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, district_array);
        district.setAdapter(adapter);
        final String area_name[] = {"Mirpur","Uttara","Gulshan","Muhammadpur","Dhanmondi","Bashundhara","Baridhara","Elephant Road",
                "Jatrabari","Badda","Motijheel","Savar","Rampura","Malibag","Farmgate","Khilgaon","Purbachal","Mogbazar","Banani","Tejgaon",
                "Banglamoror","Paltan","Rama","Keraniganj","Lalbag","Tongi","Basabo","Cantonment","Khilkhet","Mohakhali","Wari","Sutrapur",
                "Demra","Bangshal","Chaukbazar","New Market","Kamrangirchar","Hazaribagh","Kafrul","Dhamrai","Kotwali","Nawabganj","Dohar",
                "Adabor","Airport","Dakshinkhan","Darus salam","Bhasan tek","Bhatara","Gendaria","Kolabagan","Sobujbagh","Shahjahanpur"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, area_name);
        areaName.setAdapter(adapter1);
    }

    private void checkboxItem() {
        checkLift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(0, "Lift ");
                }else {
                    checkList.set(0,"");
                }
            }
        });
        checkWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(1,"Wifi ");
                }else {
                    checkList.set(1,"");
                }
            }
        });
        checkParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(2,"Car Parking ");
                }else {
                    checkList.set(2,"");
                }
            }
        });
        checkCctv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(3,"CCTV ");
                }else {
                    checkList.set(3,"");
                }
            }
        });
        checkGas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkList.set(4,"Gas ");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK
         && data != null && data.getData() != null){
            file_path1 = data.getData();
            i1 = true;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path1);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;
                if (lengthbmp >= 4000000){
                    Toast.makeText(this, "Image size must be less than 2 MB or 2 MB", Toast.LENGTH_SHORT).show();
                }else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    imageInByte1 = stream.toByteArray();
                    img1.setImageBitmap(bitmap);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path1);
                img1.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }*/
        }
        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            i2 = true;
            file_path2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path2);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;
                if (lengthbmp >= 4000000){
                    Toast.makeText(this, "Image size must be less than 2 MB", Toast.LENGTH_SHORT).show();
                }else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    imageInByte2 = stream.toByteArray();
                    img2.setImageBitmap(bitmap);
                }
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
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;
                if (lengthbmp >= 4000000){
                    Toast.makeText(this, "Image size must be less than 2 MB", Toast.LENGTH_SHORT).show();
                }else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    imageInByte3 = stream.toByteArray();
                    img3.setImageBitmap(bitmap);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
           /* try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file_path3);
                img3.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }*/
        }
    }

    public void goNextStep2(View view) {
        if (i1 == true && i2 == true && i3 == true){
            String mHome_type = home_type.getText().toString().trim();
            String mPrice = price.getText().toString().trim();
            String mNumberRoom = number_room.getText().toString().trim();
            String mNumberBath = number_bath.getText().toString().trim();
            String mFloorN = floorN.getText().toString().trim();
            if (!TextUtils.isEmpty(mHome_type)){
                if (!TextUtils.isEmpty(mPrice)){
                    if (!TextUtils.isEmpty(mNumberRoom)){
                        if (!TextUtils.isEmpty(mNumberBath)){
                            if (!TextUtils.isEmpty(mFloorN)){
                                step1.setVisibility(View.GONE);
                                step2.setVisibility(View.VISIBLE);
                                step3.setVisibility(View.GONE);
                                String currentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                String idExit = getIntent().getExtras().getString("uniqueId");
                                String imageId = idExit+"/";
                                final StorageReference ref1 = storageReference.child("images/" + imageId).child(currentDateTime + "/"+"1");
                                final StorageReference ref2 = storageReference.child("images/" + imageId).child(currentDateTime + "/"+"2");
                                final StorageReference ref3 = storageReference.child("images/" + imageId).child(currentDateTime + "/"+"3");
                                /*UploadTask uploadTask1=ref1.putBytes(imageInByte1);

                                Task<Uri> uriTask1=uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(task.isSuccessful())
                                        {
                                            throw task.getException();
                                        }
                                        return ref1.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            img_url1=task.getResult().toString();
                                            c1 = true;
                                        }
                                        else {
                                            Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                UploadTask uploadTask2=ref2.putBytes(imageInByte2);

                                Task<Uri> uriTask2=uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(task.isSuccessful())
                                        {
                                            throw task.getException();
                                        }
                                        return ref2.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            img_url2=task.getResult().toString();
                                            c2 = true;
                                        }
                                        else {
                                            Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                UploadTask uploadTask3=ref3.putBytes(imageInByte3);

                                Task<Uri> uriTask3=uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(task.isSuccessful())
                                        {
                                            throw task.getException();
                                        }
                                        return ref3.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            img_url3=task.getResult().toString();
                                            c3 = true;
                                        }
                                        else {
                                            Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });*/
                                ref1.putBytes(imageInByte1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> downlaodUri = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!downlaodUri.isComplete()) ;
                                        Uri uri = downlaodUri.getResult();
                                        img_url1 = uri.toString();
                                        c1 = true;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                ref2.putBytes(imageInByte2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> downlaodUri = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!downlaodUri.isComplete()) ;
                                        Uri uri = downlaodUri.getResult();
                                        img_url2 = uri.toString();
                                        c2 = true;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                ref3.putBytes(imageInByte3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> downlaodUri = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!downlaodUri.isComplete()) ;
                                        Uri uri = downlaodUri.getResult();
                                        img_url3 = uri.toString();
                                        c3 = true;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Create_add.this, "Failed! Check Internet Conection", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else {
                                floorN.setError("Floor No. can't be Empty");
                            }
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
        if (c1 == true && c2 == true && c3 == true) {
            String mDivision = division.getText().toString().trim();
            String mDistrict = district.getText().toString().trim();
            String mAreaName = areaName.getText().toString();
            String mShortAddress = short_address.getText().toString();
            if (!TextUtils.isEmpty(mDivision)) {
                if (!TextUtils.isEmpty(mDistrict)) {
                    if (!TextUtils.isEmpty(mAreaName)) {
                        if (!TextUtils.isEmpty(mShortAddress)) {
                            if (get_location == true) {
                                step1.setVisibility(View.GONE);
                                step3.setVisibility(View.VISIBLE);
                                step2.setVisibility(View.GONE);

                            } else {
                                pick_location.setError("Pick your home location");
                            }
                        } else {
                            short_address.setError("Give a full address to your home");
                        }
                    } else {
                        areaName.setError("Select Area Name!");
                    }
                } else {
                    district.setError("Select District!");
                }
            } else {
                division.setError("Select Division!");
            }
        }else {
            Toast.makeText(this, "Uploading Images. Wait for few seceond", Toast.LENGTH_SHORT).show();
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
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        double lat = currentLocation.getLatitude();
                        double longi = currentLocation.getLongitude();


                        lati=String.valueOf(lat);
                        lon=String.valueOf(longi);
                        get_location = true;

                    }
                }
            });
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
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      switch (requestCode) {
          case REQUEST_CODE:
              if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  getLocation();
              }
              break;
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
