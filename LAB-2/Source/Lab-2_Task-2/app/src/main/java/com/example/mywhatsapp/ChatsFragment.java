package com.example.mywhatsapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.icu.text.Edits;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View chatsview;
    private ListView chatlistView;
    private ArrayAdapter<String> listaddapter;
    private ArrayList<String> list_of_chats = new ArrayList<>();
    private DatabaseReference dbRef;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatsview = inflater.inflate(R.layout.fragment_chats, container, false);
        dbRef = FirebaseDatabase.getInstance().getReference().child("ChatsList");
        InitializeFields();
        DisplayGroups();

        chatlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String CurrentChat = parent.getItemAtPosition(position).toString();
                Intent chatIntent = new Intent(getContext(),ChatConversationActivity.class);
                chatIntent.putExtra("chatName",CurrentChat);
                startActivity(chatIntent);
            }
        });

        return  chatsview;
    }

    private void InitializeFields() {
        chatlistView = (ListView) chatsview.findViewById(R.id.chatsRecyclerView);
        listaddapter = new ArrayAdapter<String>(getContext(),R.layout.item_chat, R.id.nameTV ,list_of_chats);
        chatlistView.setAdapter(listaddapter);
    }
    private void DisplayGroups() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for(DataSnapshot childItem : snapshot.getChildren()) {

                        Iterator iter = childItem.getChildren().iterator();
                        while(iter.hasNext())
                        {
                            String chatName = (String) ((DataSnapshot)iter.next()).getValue();
                            System.out.println(chatName);
                            String chatDate = (String) ((DataSnapshot)iter.next()).getValue();
                            System.out.println(chatDate);
                            String chatMessage = (String) ((DataSnapshot)iter.next()).getValue();
                            System.out.println(chatMessage);
                            String chatTime = (String) ((DataSnapshot)iter.next()).getValue();
                            System.out.println(chatTime);

                        }

                    }
                }

                Set<String> set = new HashSet<>();
                Iterator iter = dataSnapshot.getChildren().iterator();
                while(iter.hasNext()) {

                    set.add(((DataSnapshot) iter.next()).getKey());
                }

                list_of_chats.clear();
                list_of_chats.addAll(set);
                listaddapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
