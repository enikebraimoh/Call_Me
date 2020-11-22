package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Notifications extends AppCompatActivity {
    RecyclerView Notifications;
    DatabaseReference FriendRequests;
    DatabaseReference Contactsref;
    DatabaseReference Usersref;
    FirebaseAuth mAuth;
    String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Notifications = findViewById(R.id.notificationsview);
        Notifications.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FriendRequests = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        Contactsref = FirebaseDatabase.getInstance().getReference().child("Contacts");
        Usersref = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid().toString();

    }

    @Override
    protected void onStart() {
        super.onStart();
      FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<contactsmodel>().
                setQuery(FriendRequests.child(currentUser),contactsmodel.class).build();

        FirebaseRecyclerAdapter<contactsmodel,NotificationDesignViewHolder> viewHolderFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<contactsmodel, NotificationDesignViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull NotificationDesignViewHolder holder, int i, @NonNull contactsmodel contactsmodel) {

                        String UserListId = getRef(i).getKey();

                        DatabaseReference RequestType = getRef(i).child("Request_type").getRef();
                       RequestType.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if(snapshot.exists()){

                                   String Type = snapshot.getValue().toString();
                                   if(Type.equals("recieved")){
                                       Usersref.child(UserListId).addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               if(snapshot.hasChild("Picture")){
                                                   String imagePath = snapshot.child("Picture").getValue().toString();
                                                   Picasso.get().load(imagePath).into(holder.UserImage);
                                               }

                                               String Name = snapshot.child("Name").getValue().toString();
                                               holder.user_name.setText(Name);
                                               holder.Accept.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       Contactsref.child(currentUser).child(UserListId).child("contact")
                                                               .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               if (task.isSuccessful()){
                                                                   Contactsref.child(UserListId).child(currentUser).child("contact")
                                                                           .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if(task.isSuccessful()){
                                                                               FriendRequests.child(currentUser).child(UserListId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {

                                                                                       if(task.isSuccessful()){

                                                                                           FriendRequests.child(UserListId).child(currentUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                               @Override
                                                                                               public void onComplete(@NonNull Task<Void> task) {

                                                                                                   if(task.isSuccessful()){


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
                                               });

                                               holder.Decline.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       FriendRequests.child(currentUser).child(UserListId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {

                                                               if(task.isSuccessful()){

                                                                   FriendRequests.child(currentUser).child(UserListId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {

                                                                           if(task.isSuccessful()){
                                                                               Toast.makeText(Notifications.this, "Canceled", Toast.LENGTH_SHORT).show();
                                                                           }

                                                                       }
                                                                   });
                                                               }

                                                           }
                                                       });

                                                   }
                                               });

                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });



                                   }else{

                                   }

                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

                    }

                    @NonNull
                    @Override
                    public NotificationDesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_design,parent,false);
                        NotificationDesignViewHolder viewHolder = new NotificationDesignViewHolder(view);
                        return viewHolder;
                    }
                };

        Notifications.setAdapter(viewHolderFirebaseRecyclerAdapter);
        viewHolderFirebaseRecyclerAdapter.startListening();

    }

    static class NotificationDesignViewHolder extends RecyclerView.ViewHolder{
        TextView user_name;
        ImageView UserImage;
        Button Accept, Decline;
        CardView mCardView;
        public NotificationDesignViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            UserImage = itemView.findViewById(R.id.userimage);
            Accept = itemView.findViewById(R.id.acceptfriendrequest);
            Decline = itemView.findViewById(R.id.declinefriendrequest);
            mCardView = itemView.findViewById(R.id.cardview);

        }
    }


}