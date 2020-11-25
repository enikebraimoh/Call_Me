package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    ImageView mImageView;
    FirebaseAuth mAuth;
    String SenderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);


        RecieverId = getIntent().getExtras().get("reciever_Id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        SenderId = mAuth.getCurrentUser().getUid();

        name = findViewById(R.id.callusername);
        mImageView = findViewById(R.id.callimage);

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

    }

    @Override
    protected void onStart() {
        super.onStart();

        makeCall();

    }

    private void makeCall() {

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


}