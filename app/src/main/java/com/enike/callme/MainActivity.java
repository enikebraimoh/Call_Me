package com.enike.callme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ImageView FindPeoplePage;
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        mRecyclerView = findViewById(R.id.contactlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FindPeoplePage = findViewById(R.id.findpeopleimage);

        navView.setOnNavigationItemSelectedListener(bottonnavlistener);

        FindPeoplePage.setOnClickListener(view -> {

            Intent findfriends = new Intent(MainActivity.this, FindFriends.class);
            startActivity(findfriends);

        });


    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    static class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView user_name;
        ImageView UserImage;
        Button Accept;
        //CardView mCardView;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.contactname);
            UserImage = itemView.findViewById(R.id.mycontactimage);
            Accept = itemView.findViewById(R.id.callbtn);
            //mCardView = itemView.findViewById(R.id.cardview);

        }
    }


  BottomNavigationView.OnNavigationItemSelectedListener bottonnavlistener =
          item -> {
              switch (item.getItemId()){
                  case R.id.navigation_home:
    //                  Intent home = new Intent(MainActivity.this, MainActivity.class);
    //                  startActivity(home);
                      break;
                  case R.id.navigation_settings:
                      Intent settings = new Intent(MainActivity.this, Settings.class);
                      startActivity(settings);
                      break;
                  case R.id.navigation_notifications:
                      Intent notification = new Intent(MainActivity.this, Notifications.class);
                      startActivity(notification);
                      break;
              }

              return true;
          };

}