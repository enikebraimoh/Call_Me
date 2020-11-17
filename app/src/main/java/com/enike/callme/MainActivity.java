package com.enike.callme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    }

  BottomNavigationView.OnNavigationItemSelectedListener bottonnavlistener =
          new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
      }
  };

}