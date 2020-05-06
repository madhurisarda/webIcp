package com.example.mywhatsapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.icu.text.Edits;

import androidx.annotation.NonNull;
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

public class CallsFragment extends Fragment {
    private View callsview;
    private ListView calllistView;
    private ArrayAdapter<String> listaddapter;
    private ArrayList<String> list_of_calls = new ArrayList<>();
    private DatabaseReference dbRef;

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        callsview= inflater.inflate(R.layout.fragment_calls, container, false);
        dbRef = FirebaseDatabase.getInstance().getReference().child("CallsList");
        InitializeFields();

        DisplayGroups();
//        calllistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String CurrentGroup = parent.getItemAtPosition(position).toString();
//                Intent groupChatIntent = new Intent(getContext(),GroupchatActivity.class);
//                groupChatIntent.putExtra("calledUser",CurrentGroup);
//                startActivity(groupChatIntent);
//            }
//        });

        return  callsview;
    }


    private void InitializeFields() {
        calllistView = (ListView) callsview.findViewById(R.id.calls_list);
        listaddapter = new ArrayAdapter<String>(getContext(),R.layout.item_call,R.id.nameTV,list_of_calls);
        calllistView.setAdapter(listaddapter);
    }
    private void DisplayGroups() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CallClass callResult = dataSnapshot.getValue(CallClass.class);
                System.out.println(callResult);

                Set<String> set = new HashSet<>();
                Iterator iter = dataSnapshot.getChildren().iterator();
                while(iter.hasNext()) {

//                    String value = ((DataSnapshot) iter.next()).getValue().toString();

//                    if(dataSnapshot.exists() && ((dataSnapshot.hasChild("name"))))
//                    {
//                        String calledPerson = dataSnapshot.child("name").getValue().toString();
//                        String callingDate = dataSnapshot.child("date").getValue().toString();
//                        String callTime = dataSnapshot.child("time").getValue().toString();
//                        String CallType = dataSnapshot.child("callType").getValue().toString();
//
//                    }

                    set.add(((DataSnapshot) iter.next()).getKey());
                }

                list_of_calls.clear();
                list_of_calls.addAll(set);
                listaddapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
