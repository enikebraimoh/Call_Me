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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Settings extends AppCompatActivity {

    EditText Name, Bio;
    Button SaveBtn;
    ImageView ProfilePix;
    private static int Galleryrequestcode = 1;
    private Uri ImageUri;
    StorageReference userprofileref;
    private String DownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Name = findViewById(R.id.name);
        Bio = findViewById(R.id.bio);
        SaveBtn = findViewById(R.id.savebutton);
        ProfilePix = findViewById(R.id.profileimage);

        userprofileref = FirebaseStorage.getInstance().getReference().child("User Profile Images");

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
            if(Name.getText().toString().equals("") || Bio.getText().toString().equals("") || ImageUri == null){
                Toast.makeText(this, "please all fields are mandatory", Toast.LENGTH_SHORT).show();
            }
            else{
                SaveUserInfo();

            }

        });

    }

    private void SaveUserInfo() {
        StorageReference filepath = userprofileref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            UploadTask uploadTask = filepath.putFile(ImageUri);
            uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Object>() {
                @Override
                public Object then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){

                      throw  task.getException();
                    }
                    DownloadUrl = filepath.getDownloadUrl().toString();
                    return filepath.getDownloadUrl();
                } 
            }).addOnCompleteListener(new OnCompleteListener<Object>() {
                @Override
                public void onComplete(@NonNull Task<Object> task) {



                }
            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Galleryrequestcode && requestCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            ProfilePix.setImageURI(ImageUri);
        }
    }
}