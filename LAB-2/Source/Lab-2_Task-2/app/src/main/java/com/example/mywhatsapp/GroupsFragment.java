package com.example.mywhatsapp;


import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class GroupsFragment extends Fragment {


    private View groupsview;
    private ListView grouplistView;
    private ArrayAdapter<String> listaddapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private DatabaseReference dbRef;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupsview= inflater.inflate(R.layout.fragment_groups, container, false);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        InitializeFields();

        DisplayGroups();
        grouplistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String CurrentGroup = parent.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getContext(),GroupchatActivity.class);
                groupChatIntent.putExtra("groupName",CurrentGroup);
                startActivity(groupChatIntent);
            }
        });


        return  groupsview;
    }


    private void InitializeFields() {
        grouplistView = (ListView) groupsview.findViewById(R.id.list_view);
        listaddapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        grouplistView.setAdapter(listaddapter);
    }
    private void DisplayGroups() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<>();
                Iterator iter = dataSnapshot.getChildren().iterator();
                while(iter.hasNext()) {

                    set.add(((DataSnapshot) iter.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                listaddapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
