package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    TextView sign_in;
    private EditText name,phone_number,password,confirm_password;
    private DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseReference = FirebaseDatabase.getInstance().getReference("Owner");

        sign_in = findViewById(R.id.sign_in);
        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignIn.class);
                startActivity(intent);
            }
        });
    }

    public void Sign_up(View view) {
        final String oName = name.getText().toString();
        final String oPhoneNumber = phone_number.getText().toString().trim();
        final String oPassword = password.getText().toString().trim();
        String oConfirmPass = confirm_password.getText().toString().trim();

        if (!TextUtils.isEmpty(oName) && !TextUtils.isEmpty(oPhoneNumber)
                && oPhoneNumber.length() == 11){
            if (!TextUtils.isEmpty(oPassword) && !TextUtils.isEmpty(oConfirmPass) && oPassword.equals(oConfirmPass)){
                progressDialog.show();
                databaseReference.orderByChild("phone_number").equalTo(oPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() !=null) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "This number already have a account!", Toast.LENGTH_LONG).show();
                            }else {
                                AddData setRegData = new AddData(oName, oPhoneNumber, oPassword);
                                databaseReference.child(oPhoneNumber).setValue(setRegData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Registration Complete!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }else {
                confirm_password.setError("Password don't match!!Please input password");
            }
        }else {
            name.setError("Name can't be empty!!");
            phone_number.setError("Phone number must be 11 digit!!");
        }
    }
}
