package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notifications extends AppCompatActivity {
    RecyclerView Notifications;
    DatabaseReference FriendRequests;
    DatabaseReference Contactsref;
    DatabaseReference Users;
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
        Users = FirebaseDatabase.getInstance().getReference().child("Contacts");
        currentUser = mAuth.getCurrentUser().getUid();


    }

    @Override
    protected void onStart() {
        super.onStart();
      FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<contactsmodel>().
                setQuery(FriendRequests.child(currentUser),contactsmodel.class).build();

        FirebaseRecyclerAdapter<contactsmodel,NotificationDesignViewHolder> viewHolderFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<contactsmodel, NotificationDesignViewHolder>() {
                    @Override
                    protected void onBindViewHolder(@NonNull NotificationDesignViewHolder notificationDesignViewHolder, int i, @NonNull contactsmodel contactsmodel) {

                    }

                    @NonNull
                    @Override
                    public NotificationDesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }
                };

    }

    static class NotificationDesignViewHolder extends RecyclerView.ViewHolder{
        ImageView UserImage;
        Button Accept, Decline;
        CardView mCardView;
        public NotificationDesignViewHolder(@NonNull View itemView) {
            super(itemView);

            UserImage = itemView.findViewById(R.id.userimage);
            Accept = itemView.findViewById(R.id.acceptfriendrequest);
            Decline = itemView.findViewById(R.id.declinefriendrequest);
            mCardView = itemView.findViewById(R.id.cardview);

        }
    }



}