package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallActivity extends AppCompatActivity {

    DatabaseReference UsersRef;
    String RecieverId,Username, Userimage;
    TextView name;
    ImageView mImageView, AcceptCall, CancelCall;
    FirebaseAuth mAuth;
    String SenderId;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        mMediaPlayer = MediaPlayer.create(this,R.raw.ringtone);

        RecieverId = getIntent().getExtras().get("reciever_Id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        SenderId = mAuth.getCurrentUser().getUid();

        name = findViewById(R.id.callusername);
        mImageView = findViewById(R.id.callimage);

        AcceptCall = findViewById(R.id.callaccept);
        CancelCall = findViewById(R.id.calldecline);

        AcceptCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                HashMap<String,Object> pickedObject = new HashMap<>();
                pickedObject.put("picked","picked");

                UsersRef.child(SenderId).child("Ringing").updateChildren(pickedObject).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mMediaPlayer.stop();
                                Intent intent = new Intent(CallActivity.this,VideoCallSession.class);
                                startActivity(intent);
                                finish();
                            }
                        });


            }
        });

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(RecieverId).exists()){
                    Username = snapshot.child(RecieverId).child("Name").getValue().toString();
                    Userimage = snapshot.child(RecieverId).child("Picture").getValue().toString();

                    Picasso.get().load(Userimage).into(mImageView);
                    name.setText(Username);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CancelCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                CancelCalling();

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        makeCall();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(SenderId).hasChild("Ringing")){
                    AcceptCall.setVisibility(View.VISIBLE);
                }
                if (snapshot.child(RecieverId).child("Ringing").hasChild("picked")){
                    mMediaPlayer.stop();
                    Intent intent = new Intent(CallActivity.this,VideoCallSession.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void makeCall() {
        mMediaPlayer.start();
        UsersRef.child(RecieverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild("Ringing") && !snapshot.hasChild("Calling")){


                    HashMap<String,Object> CallingObject = new HashMap<>();
                    CallingObject.put("calling",RecieverId);


                    UsersRef.child(SenderId).child("Calling").updateChildren(CallingObject)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        HashMap<String,Object> ReceivingObject = new HashMap<>();
                                        ReceivingObject.put("ringing",SenderId);

                                        UsersRef.child(RecieverId).child("Ringing")
                                                .updateChildren(ReceivingObject)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });

                                    }


                                }
                            });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void CancelCalling() {
        // Sender Part
        mMediaPlayer.stop();
        UsersRef.child(SenderId).child("Calling").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("calling")){
                    String CallerId = snapshot.child("calling").getValue().toString();
                    UsersRef.child(CallerId).child("Ringing").removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      UsersRef.child(SenderId).child("Calling").removeValue()
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      Intent intent = new Intent(CallActivity.this,MainActivity.class);
                                                      startActivity(intent);
                                                      finish();

                                                  }
                                              });
                                  }
                                }
                            });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // reciever part

        UsersRef.child(SenderId).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("ringing")){
                    String RingingId = snapshot.getValue().toString();
                    UsersRef.child(RingingId).child("Calling").removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        UsersRef.child(SenderId).child("Ringing").removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Intent intent = new Intent(CallActivity.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}