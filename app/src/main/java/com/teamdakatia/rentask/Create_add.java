package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.UUID;

public class Create_add extends AppCompatActivity {
    private Button btnPublish,pick_location;
    private ScrollView step1, step2,step3;
    private ImageView img1,img2,img3;
    private AutoCompleteTextView home_type,price,number_room,number_bath,division,district,areaName,rent_start;
    private EditText short_address,post_phoneNumber;
    private Uri file_path1, file_path2,file_path3;
    boolean i1 = false,i2 = false,i3 = false;

    String img_url1="",img_url2="",img_url3="",location_long,location_lat;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

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
        btnPublish = findViewById(R.id.btnPublish);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Owner");


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

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData setData = new AddData(img_url1,img_url2,img_url3);
                databaseReference.child("phone_number").setValue(setData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Create_add.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
        String mShortAddress = short_address.getText().toString().trim();
        if (!TextUtils.isEmpty(mDivision)){
            if (!TextUtils.isEmpty(mDistrict)){
                if (!TextUtils.isEmpty(mAreaName)){
                    if (!TextUtils.isEmpty(mShortAddress)){

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
