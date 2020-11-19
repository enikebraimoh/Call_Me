package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Settings extends AppCompatActivity {

    EditText Name, Bio;
    Button SaveBtn, Logout;
    ImageView ProfilePix;
    private static int Galleryrequestcode = 1;
    private Uri ImageUri;
    StorageReference userprofileref;
    private String DownloadUrl;
    DatabaseReference Usersref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Name = findViewById(R.id.name);
        Bio = findViewById(R.id.bio);
        SaveBtn = findViewById(R.id.savebutton);
        Logout = findViewById(R.id.logoutbtn);
        ProfilePix = findViewById(R.id.profileimage);

        userprofileref = FirebaseStorage.getInstance().getReference().child("User Profile Images");
        Usersref = FirebaseDatabase.getInstance().getReference().child("Users");

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Settings.this,Registration1.class);
                startActivity(intent);
                finish();
            }
        });

        ProfilePix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,Galleryrequestcode);


            }
        });

        SaveBtn.setOnClickListener(view -> {
            if(Name.getText().toString().equals("") || Bio.getText().toString().equals("")){
                Toast.makeText(this, "please all fields are mandatory", Toast.LENGTH_SHORT).show();
            }else if(ImageUri == null){

                Usersref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.hasChild("Picture")){
                           HashMap<String,Object> Profile = new HashMap<>();
                           Profile.put("Name",Name.getText().toString());
                           Profile.put("bio",Bio.getText().toString());

                           DatabaseReference usersref = Usersref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                           usersref.updateChildren(Profile).addOnCompleteListener(task1 -> {
                               Intent intent = new Intent(Settings.this,MainActivity.class);
                               startActivity(intent);
                               finish();
                               Toast.makeText(Settings.this, "Updated suscessfully", Toast.LENGTH_SHORT).show();
                           });



                       }
                       else{
                            Toast.makeText(Settings.this,"please select a picture",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else{

                SaveUserInfo();

            }

        });
        RetreveUserInfo();

    }

    private void SaveUserInfo() {
        StorageReference filepath = userprofileref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            UploadTask uploadTask = filepath.putFile(ImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                      throw  task.getException();
                    }

                    DownloadUrl = filepath.getDownloadUrl().toString();
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        DownloadUrl = task.getResult().toString();

                        HashMap<String,Object> Profile = new HashMap<>();
                        Profile.put("Name",Name.getText().toString());
                        Profile.put("bio",Bio.getText().toString());
                        Profile.put("Picture",DownloadUrl);

                        DatabaseReference usersref = Usersref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        usersref.updateChildren(Profile).addOnCompleteListener(task1 -> {
                            Intent intent = new Intent(Settings.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Settings.this, "Uploaded suscessfully", Toast.LENGTH_SHORT).show();

                        });
                    }
                }
            });

    }

    private void RetreveUserInfo(){

        Usersref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Name.setText(snapshot.child("Name").getValue().toString());
                    Bio.setText(snapshot.child("bio").getValue().toString());
                    Picasso.get().load(snapshot.child("Picture").getValue().toString()).placeholder(R.drawable.ic_image).into(ProfilePix);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Galleryrequestcode && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            ProfilePix.setImageURI(ImageUri);
        }
        else{
            Toast.makeText(this, "image selection failed", Toast.LENGTH_SHORT).show();
        }
    }
}