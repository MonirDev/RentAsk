package com.teamdakatia.rentask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTP_varification extends AppCompatActivity {

    private static final long COUNTDOWN_IN_MILLIS = 10000;
    private static final String KEY_MILLIS_LEFT = "KeyMillisLeft";
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;
    ProgressBar progressBar;
    TextView textViewCountDown;
    EditText verifyCode;
    Button setPassword,resend;
    String codeSend,phoneNumber;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        verifyCode = findViewById(R.id.vercodeID);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        setPassword = findViewById(R.id.setpass);
        resend  =findViewById(R.id.resendcode);

        final Intent intent  = getIntent();
        phoneNumber = intent.getStringExtra("Mobile");
        //for send code
        sendVerifycode();
        //Time countdown ....
        timeLeftMillis = COUNTDOWN_IN_MILLIS;
        startCountdown();

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verifyCode.getText().toString().trim();
                if (!code.isEmpty() && code.length() == 6){
                    verifySinginCode();
                }else {
                    Toast.makeText(OTP_varification.this,
                            "Verification code doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerifycode();
                timeLeftMillis  = COUNTDOWN_IN_MILLIS;
                startCountdown();
                resend.setVisibility(View.GONE);
            }
        });
    }


    private void verifySinginCode() {
        progressBar.setVisibility(View.VISIBLE);
        String code = verifyCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSend,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    sendSetNewPassword();
                    finish();

                }else {
                    progressBar.setVisibility(View.GONE);
                    if (task.getException() instanceof FirebaseAuthInvalidUserException){
                        Toast.makeText(OTP_varification.this,
                                "Invalid Verification Code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void sendSetNewPassword() {
        Intent intent = new Intent(OTP_varification.this,SetNewPassword.class);
        intent.putExtra("PhoneNumber", phoneNumber );
        startActivity(intent);
        finish();
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(timeLeftMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftMillis = l;
                updateCoundown();
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                updateCoundown();
                resend.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    private void updateCoundown() {
        int min = (int)(timeLeftMillis / 1000) / 60;
        int sec = (int)(timeLeftMillis / 1000) % 60;
        String timeFor = String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        textViewCountDown.setText(timeFor);
    }



    private void sendVerifycode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88"+
                        phoneNumber, 60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSend = s;
        }
    };

}
