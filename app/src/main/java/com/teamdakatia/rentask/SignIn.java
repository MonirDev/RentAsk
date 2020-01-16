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
import android.widget.ProgressBar;
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

import java.text.DateFormat;
import java.util.Calendar;

public class SignIn extends AppCompatActivity {

    TextView new_acccount, forgetPass;
    Button sign_in;
    private EditText signIn_number, signIn_password;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        databaseReference = FirebaseDatabase.getInstance().getReference("Owner");

        new_acccount = findViewById(R.id.new_account);
        sign_in = findViewById(R.id.sign_in);
        signIn_password = findViewById(R.id.signIn_password);
        signIn_number = findViewById(R.id.signIn_number);
        forgetPass = findViewById(R.id.forgetpass);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oPhoneNumber = signIn_number.getText().toString().trim();
                final String oPassword = signIn_password.getText().toString().trim();
                databaseReference.orderByChild("phone_number").equalTo(oPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                AddData mPass = ds.getValue(AddData.class);
                                String mPassword = mPass.getPassword().toString();
                                if (oPassword.equals(mPassword)) {
                                    Intent intent = new Intent(getApplicationContext(), OwnerDashboard.class);
                                    intent.putExtra("UniqueId", oPhoneNumber);
                                    startActivity(intent);
                                } else {
                                    signIn_password.setError("Password or Number Don't Match!!");
                                }
                            }
                        } else {
                            signIn_number.setError("Input Number Correctly");
                            Toast.makeText(SignIn.this, "You didn't create any account using this number!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        new_acccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }
}