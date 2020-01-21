package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetNewPassword extends AppCompatActivity {
    DatabaseReference databaseReference;
    private EditText pass1, pass2;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        reset = findViewById(R.id.reset);

        final String number = getIntent().getExtras().getString("PhoneNumber");
        databaseReference = FirebaseDatabase.getInstance().getReference("Owner");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mPass1 = pass1.getText().toString().trim();
                String mPass2 = pass2.getText().toString().trim();
                if (!TextUtils.isEmpty(mPass1) && !TextUtils.isEmpty(mPass2) && mPass1.equals(mPass2)){
                    databaseReference.orderByChild("phone_number").equalTo(number).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                AddData value = ds.getValue(AddData.class);
                                String name = value.getName().toString();
                                AddData setData = new AddData(name, number,mPass1);
                                databaseReference.child(number).setValue(setData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SetNewPassword.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),SignIn.class);
                                            startActivity(intent);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SetNewPassword.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    pass2.setError("Password Don't Match!!");
                }
            }
        });


    }
}
