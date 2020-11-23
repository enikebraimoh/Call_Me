package com.enike.callme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    DatabaseReference Contactsref;
    DatabaseReference Usersref;
    FirebaseAuth mAuth;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid().toString();
        Contactsref = FirebaseDatabase.getInstance().getReference().child("Contacts");
        Usersref = FirebaseDatabase.getInstance().getReference().child("Users");

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


        FirebaseRecyclerOptions<contactsmodel> firebaseRecyclerOptions
                = new FirebaseRecyclerOptions.Builder<contactsmodel>()
                .setQuery(Contactsref.child(currentUser),contactsmodel.class).build();


        FirebaseRecyclerAdapter<contactsmodel,ContactViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<contactsmodel, ContactViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ContactViewHolder holder, int i, @NonNull contactsmodel contactsmodel) {
                String UserListId = getRef(i).getKey();
                Usersref.child(UserListId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                         String Image = snapshot.child("Picture").getValue().toString();
                         String Name = snapshot.child("Name").getValue().toString();

                            Picasso.get().load(Image).into(holder.UserImage);
                            holder.user_name.setText(Name);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_design,parent,false);
                ContactViewHolder viewHolder = new ContactViewHolder(view);
                return viewHolder;
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

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