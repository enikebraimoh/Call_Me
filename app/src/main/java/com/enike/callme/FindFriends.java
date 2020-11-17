package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FindFriends extends AppCompatActivity {

    EditText Search;
    RecyclerView List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Search = findViewById(R.id.searchbar);
        List = findViewById(R.id.findfriendslist);
        List.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    static class Contact_ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView MyContactImage;
        TextView ContactName;
        Button Call;
        CardView mCardView;

        public Contact_ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            MyContactImage = itemView.findViewById(R.id.mycontactimage);
            ContactName = itemView.findViewById(R.id.contactname);
            Call = itemView.findViewById(R.id.callbtn);
            mCardView = itemView.findViewById(R.id.contactcardview);

        }
    }

}