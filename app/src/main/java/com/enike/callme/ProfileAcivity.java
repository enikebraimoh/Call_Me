package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileAcivity extends AppCompatActivity {

    String RecieverName,RecieverId,RecieverImage;
    ImageView Recieverimage;
    Button sendRequestbtn, CanclefriendRequestbtn;
    TextView Recievername;
    String currentState = "new";
    String SenderId;
    FirebaseAuth auth;
    DatabaseReference FriendRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acivity);

        FriendRequests = FirebaseDatabase.getInstance().getReference().child("Friend Requests");

        Recieverimage = findViewById(R.id.recieverimage);
        sendRequestbtn = findViewById(R.id.sendfriendrequest);
        CanclefriendRequestbtn = findViewById(R.id.canclefriendrequest);
        Recievername = findViewById(R.id.recievername);

        auth = FirebaseAuth.getInstance();
        SenderId = auth.getCurrentUser().getUid();

        RecieverId = getIntent().getExtras().get("userId").toString();
        RecieverImage = getIntent().getExtras().get("userProfileImage").toString();
        RecieverName = getIntent().getExtras().get("UserName").toString();


        Recievername.setText(RecieverName);
        Picasso.get().load(RecieverImage).placeholder(R.drawable.ic_image).into(Recieverimage);

        ManageClicks();


    }

    private void ManageClicks() {
        if(SenderId.equals(RecieverId)){
            sendRequestbtn.setVisibility(View.GONE);
        }else{
            if (currentState.equals("new")){

                SendFriendRequest();

            }else if(currentState.equals("sent")){
                CancelFriendRequest();

            }else if(currentState.equals("canceled")){

            }


        }


    }

    private void CancelFriendRequest() {


    }

    private void SendFriendRequest() {
        FriendRequests.child(SenderId).child(RecieverId).child("Request_type")
                .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FriendRequests.child(RecieverId).child(SenderId).child("Request_type")
                        .setValue("recieved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        currentState = "Request Sent";
                        sendRequestbtn.setText("Cancel Friend Request");
                        Toast.makeText(ProfileAcivity.this, "Friend Request sent", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


    }
}