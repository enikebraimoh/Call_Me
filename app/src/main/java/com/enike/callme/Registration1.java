package com.enike.callme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration1 extends AppCompatActivity {

    private EditText phone_number;
    private Button send_button;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);

        phone_number = findViewById(R.id.phonenumber);
        send_button = findViewById(R.id.sendbutton);


        send_button.setOnClickListener(view -> ValidatePhoneField("+234" + phone_number.getText().toString().trim()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(Registration1.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void ValidatePhoneField(String Phone_Number){
        if(Phone_Number.length() != 14){
            Toast.makeText(this, "incorrect phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            NextIntent(Phone_Number);
        }
    }

    private void NextIntent(String Phone_Number){
        Intent intent = new Intent(Registration1.this, Registration2.class);
        intent.putExtra("number",Phone_Number);
        startActivity(intent);
    }


}