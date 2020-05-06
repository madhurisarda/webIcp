package com.example.mywhatsapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Searchcontact extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView SearchContactsRecycler;
    private DatabaseReference UserRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcontact);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        SearchContactsRecycler = (RecyclerView) findViewById(R.id.search_contacts_recycler_list);
        SearchContactsRecycler.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.Search_Contacts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Contacts");
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(UserRef,Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts,SearchContactViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, SearchContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchContactViewHolder holder, int position, @NonNull Contacts model) {

                holder.username.setText(model.getName());
                holder.phnnum.setText(model.getPhnnum());

                String Image = model.getImage();

                Glide.with(Searchcontact.this).load(Image).override(100).dontAnimate()
                        .into(holder.profileImage);

            }

            @NonNull
            @Override
            public SearchContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_dsplay_layout,parent,false);
               SearchContactViewHolder viewholder = new SearchContactViewHolder(view);
               return viewholder;
            }
        };


        SearchContactsRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    public static  class SearchContactViewHolder extends RecyclerView.ViewHolder
    {
        TextView username,phnnum;
        CircleImageView profileImage;
        public  SearchContactViewHolder(@NotNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.User_profile_id);
            phnnum = itemView.findViewById(R.id.user_phnnum);

            profileImage= itemView.findViewById(R.id.contact_profile_image);




        }
    }

    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(Searchcontact.this);
        startActivity(intent);
        finish();
    }
}
