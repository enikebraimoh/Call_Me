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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    DatabaseReference Contactsref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acivity);

        FriendRequests = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        Contactsref = FirebaseDatabase.getInstance().getReference().child("Contacts");

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

        FriendRequests.child(SenderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(RecieverId)){
                    String request_type = snapshot.child(RecieverId).child("Request_type").getValue().toString();
                    if (request_type.equals("sent")){
                        currentState = "sent";
                        sendRequestbtn.setText("Cancel Friend Request");
                    }else if(request_type.equals("recieved")){
                        currentState = "request recieved";
                        sendRequestbtn.setText("Accept Friend Request");

                        CanclefriendRequestbtn.setVisibility(View.VISIBLE);
                        CanclefriendRequestbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CancelFriendRequest();
                            }
                        });

                    }


                }else{
                    Contactsref.child(SenderId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(RecieverId)){
                                currentState = "friends";
                                sendRequestbtn.setText("Delete Contact");
                            }else {
                                currentState = "new";
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(SenderId.equals(RecieverId)){

            sendRequestbtn.setVisibility(View.GONE);
        }else{

           sendRequestbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (currentState.equals("new")){

                       SendFriendRequest();

                   }else if(currentState.equals("sent")){
                       CancelFriendRequest();

                   }else if(currentState.equals("request recieved")){
                       AcceptFriendRequest();

                   }else if(currentState.equals("friends")){
                       DeleteContacts();
                   }

               }
           });

        }


    }

    private void DeleteContacts() {
        Contactsref.child(SenderId).child(RecieverId)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    Contactsref.child(RecieverId).child(SenderId)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){

                            }
                        }
                    });
                }
            }
        });
    }

    private void AcceptFriendRequest() {
        Contactsref.child(SenderId).child(RecieverId).child("contact")
                .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                  Contactsref.child(RecieverId).child(SenderId).child("contact")
                          .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              FriendRequests.child(SenderId).child(RecieverId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {

                                      if(task.isSuccessful()){

                                          FriendRequests.child(RecieverId).child(SenderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {

                                                  if(task.isSuccessful()){
                                                      currentState= "friends";
                                                      sendRequestbtn.setText("Delete Contact");
                                                      CanclefriendRequestbtn.setVisibility(View.GONE);
                                                  }

                                              }
                                          });
                                      }

                                  }
                              });

                          }

                      }
                  });
              }
            }
        });

    }

    private void CancelFriendRequest() {

        FriendRequests.child(SenderId).child(RecieverId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    FriendRequests.child(RecieverId).child(SenderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                currentState= "new";
                                sendRequestbtn.setText("Send Friend Request");
                            }

                        }
                    });
                }

            }
        });

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