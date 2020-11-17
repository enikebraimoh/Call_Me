package com.enike.callme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Settings extends AppCompatActivity {

    EditText Name, Bio;
    Button SaveBtn;
    ImageView ProfilePix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Name = findViewById(R.id.name);
        Bio = findViewById(R.id.bio);
        SaveBtn = findViewById(R.id.savebutton);
        ProfilePix = findViewById(R.id.profileimage);


    }
}