package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallSession extends AppCompatActivity {

    private static final String API_KEY = "";
    private static final String TOKEN = "";
    private static final String SESSION_ID = "";

    private static final int REQUEST_CODE = 1234;
    private static final String LOG_NAME = VideoCallSession.class.getSimpleName();
    ImageView CLose_Session;
    FrameLayout Publisher_Layout, Subscriber_Layout;
    DatabaseReference UserRef;
    String CurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_session);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CLose_Session = findViewById(R.id.endvideosession);
        Publisher_Layout = findViewById(R.id.publisher_frame);
        Subscriber_Layout = findViewById(R.id.subscriber_frame);

        CLose_Session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRef.child(CurrentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Ringing")){
                            UserRef.child(CurrentUser).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(VideoCallSession.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        if(snapshot.hasChild("Calling")){
                            UserRef.child(CurrentUser).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(VideoCallSession.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,VideoCallSession.this);
    }

    @AfterPermissionGranted(REQUEST_CODE)
    private void afterpermissionsgranted(){
        String [] params = {Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(VideoCallSession.this,params)){



        }

    }

}