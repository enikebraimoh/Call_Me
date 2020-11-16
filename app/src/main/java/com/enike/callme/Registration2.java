package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Registration2 extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationCode;
    PinView OtpView;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);


        String phoneNumber = getIntent().getStringExtra("number");
        OtpView = findViewById(R.id.otpview);
        button = findViewById(R.id.buttonsend);

//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
////            @Override
////            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
////                String code = phoneAuthCredential.getSmsCode();
////                VerifyCode(code);
////                OtpView.setText(code);
////                Toast.makeText(VerifyPage.this, "Verified", Toast.LENGTH_LONG).show();
////            }
//
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                String c = phoneAuthCredential.getSmsCode();
//                OtpView.setText(c);
//                signInWithPhoneAuthCredential(phoneAuthCredential);
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                Toast.makeText(VerifyPage.this, "Wasn't able to send OTP" + e, Toast.LENGTH_LONG).show();
//                Log.d("TAG", "onVerificationFailed", e);
//            }
//
////            @Override
////            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
////                //super.onCodeSent(s, forceResendingToken);
////
////                verificationCode = s;
////                Toast.makeText(VerifyPage.this, "otp sent" , Toast.LENGTH_LONG).show();
////
////            }
//        };

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code =  phoneAuthCredential.getSmsCode();
                if(code != null){
                    OtpView.setText(code);
                    VerifyCode(code);
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Registration2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);




        button.setOnClickListener(view -> {
            if(!(OtpView.getText().equals(verificationCode))){
                NextIntent();
            }

        });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        NextIntent();
                    }
                    else {
                        Toast.makeText(this, "an error occcored while signing in", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void NextIntent(){
        Intent intent = new Intent(Registration2.this,MainActivity.class);
        startActivity(intent);
    }

    private void VerifyCode(String code){

        if(code != null){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,code);
            signInWithPhoneAuthCredential(credential);
        }

    }

}