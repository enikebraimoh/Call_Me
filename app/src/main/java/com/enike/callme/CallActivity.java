package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CallActivity extends AppCompatActivity {

    DatabaseReference UsersRef;
    String RecieverId,Username, Userimage;
    TextView name;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        name = findViewById(R.id.callusername);
        mImageView = findViewById(R.id.callimage);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RecieverId = getIntent().getExtras().get("reciever_Id").toString();

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
}