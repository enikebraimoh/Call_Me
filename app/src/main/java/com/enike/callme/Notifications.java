package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Notifications extends AppCompatActivity {
    RecyclerView Notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Notifications = findViewById(R.id.notificationsview);
        Notifications.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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