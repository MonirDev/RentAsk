package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
    private AutoCompleteTextView home_type,number_room,number_bath,floorN,division,district,areaName,rent_start;
    private EditText price,short_address,post_phoneNumber;
    private CheckBox checkLift, checkWifi, checkParking, checkCctv, checkGas, checkFire;

    private String img_url1="",img_url2="",img_url3="", lati, lon;
    private Uri file_path1, file_path2,file_path3;
    boolean i1 = false,i2 = false,i3 = false,get_location = false;
    private String checkValue = "Facility: ";
    ArrayList<String> checkList = new ArrayList<>();

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference,databaseReferenceall;

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

        dropDown();
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
                                mPost_phoneNumber,mRentStart,checkValue,postId,mFloorN);
                        String currentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        databaseReferenceall.child(currentDateTime).setValue(setData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

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
        String mDivision = division.getText().toString().trim();
        String mDistrict = district.getText().toString().trim();
        String mAreaName = areaName.getText().toString();
        String mShortAddress = short_address.getText().toString();
        if (!TextUtils.isEmpty(mDivision)){
            if (!TextUtils.isEmpty(mDistrict)){
                if (!TextUtils.isEmpty(mAreaName)){
                    if (!TextUtils.isEmpty(mShortAddress)){
                        if (get_location == true){
                            step1.setVisibility(View.GONE);
                            step3.setVisibility(View.VISIBLE);
                            step2.setVisibility(View.GONE);
                            String currentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            String idExit = getIntent().getExtras().getString("uniqueId");
                            String imageId = idExit+"/";
                            StorageReference ref1 = storageReference.child("images/" + imageId).child(currentDateTime + "/"+"1");
                            StorageReference ref2 = storageReference.child("images/" + imageId).child(currentDateTime + "/"+"2");
                            StorageReference ref3 = storageReference.child("images/" + imageId).child(currentDateTime + "/"+"3");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
