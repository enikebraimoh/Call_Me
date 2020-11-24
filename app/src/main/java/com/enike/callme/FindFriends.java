package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.nio.file.WatchEvent;

public class FindFriends extends AppCompatActivity {

    EditText Search;
    RecyclerView List;
    String str ="";
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Search = findViewById(R.id.searchbar);
        List = findViewById(R.id.findfriendslist);
        List.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                str = charSequence.toString().toLowerCase();

                onStart();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<contactsmodel> options = null;
        if (str.equals("")){
            options = new FirebaseRecyclerOptions.Builder<contactsmodel>().
                    setQuery(ref,contactsmodel.class).build();

        }else{
            options = new FirebaseRecyclerOptions.Builder<contactsmodel>().setQuery(ref
                            .orderByChild("Name").startAt(str)
                    .endAt(str+"\uf8ff"),
                    contactsmodel.class).build();
        }

        FirebaseRecyclerAdapter<contactsmodel,Contact_ItemViewHolder> contactsView = new FirebaseRecyclerAdapter<contactsmodel, Contact_ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Contact_ItemViewHolder holder, int i, @NonNull contactsmodel contactsmodel) {

                holder.ContactName.setText("@"+contactsmodel.getName().toLowerCase());
                Picasso.get().load(contactsmodel.getPicture()).into(holder.MyContactImage);
                holder.ContactBio.setText(contactsmodel.getBio());
                holder.Call.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userid = getRef(i).getKey();

                        Intent newintent = new Intent(FindFriends.this,ProfileAcivity.class);
                        newintent.putExtra("userId",userid);
                        newintent.putExtra("userProfileImage",contactsmodel.getPicture());
                        newintent.putExtra("UserName",contactsmodel.getName());
                        startActivity(newintent);

                    }
                });

            }

            @NonNull
            @Override
            public Contact_ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_design,parent,false);
                Contact_ItemViewHolder viewHolder = new Contact_ItemViewHolder(view);
                return viewHolder;
            }
        };

        List.setAdapter(contactsView);
        contactsView.startListening();


    }



    static class Contact_ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView MyContactImage;
        TextView ContactBio;
        TextView ContactName;
        Button Call;
        CardView mCardView;

        public Contact_ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            MyContactImage = itemView.findViewById(R.id.mycontactimage);
            ContactName = itemView.findViewById(R.id.contactname);
            ContactBio = itemView.findViewById(R.id.contactbio);
            Call = itemView.findViewById(R.id.callbtn);
            mCardView = itemView.findViewById(R.id.contactcardview);

        }
    }

}