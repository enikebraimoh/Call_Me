package com.enike.callme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileAcivity extends AppCompatActivity {

    String RecieverName,RecieverId,RecieverImage;
    ImageView Recieverimage;
    Button sendRequest, CanclefriendRequest;
    TextView Recievername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acivity);

        Recieverimage = findViewById(R.id.recieverimage);
        sendRequest = findViewById(R.id.sendfriendrequest);
        CanclefriendRequest = findViewById(R.id.canclefriendrequest);
        Recievername = findViewById(R.id.recievername);

        RecieverId = getIntent().getExtras().get("userId").toString();
        RecieverImage = getIntent().getExtras().get("userProfileImage").toString();
        RecieverName = getIntent().getExtras().get("UserName").toString();


        Recievername.setText(RecieverName);
        Picasso.get().load(RecieverImage).placeholder(R.drawable.ic_image).into(Recieverimage);


    }
}