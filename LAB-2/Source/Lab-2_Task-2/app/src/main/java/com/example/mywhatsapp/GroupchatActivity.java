package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupchatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton imagebutton;
    private EditText editText;
    private ScrollView scrollView;
    private TextView textView;
    private FirebaseAuth mAuth;
    private  String currentGroup,currentUserid,currentUserName,currentdate,currenttime;
    private DatabaseReference dbRef,groupdbref,groupmessagekeyref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);

        currentGroup = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupchatActivity.this,currentGroup,Toast.LENGTH_SHORT);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupdbref = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroup);



        InitializeFields();

        GetuserInfo();

        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessagetoDb();
                editText.setText("");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        groupdbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void InitializeFields() {

        toolbar= (Toolbar) findViewById(R.id.group_chat_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroup);

        imagebutton = (ImageButton) findViewById(R.id.send_m);
        editText = (EditText) findViewById(R.id.input_g_message);
        textView = (TextView) findViewById(R.id.group_chat_text_display);
        scrollView = (ScrollView) findViewById(R.id.Scroll_View);
    }


    public void GetuserInfo() {

        dbRef.child(currentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("name").getValue().toString();

                }
                else
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void saveMessagetoDb() {
        String message = editText.getText().toString();
        String messageKEY = groupdbref.push().getKey();

        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Please Enter Message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd,yyyy");
            currentdate = currentDateFormat.format(currentDate.getTime());


            Calendar currentTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currenttime = currentTimeFormat.format(currentTime.getTime());

            HashMap<String,Object>  grpmessagekey = new HashMap<>();
            groupdbref.updateChildren(grpmessagekey);
            groupmessagekeyref = groupdbref.child(messageKEY);

            HashMap<String,Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currentdate);
            messageInfoMap.put("time",currenttime);

            groupmessagekeyref.updateChildren(messageInfoMap);
        }
    }

    public void DisplayMessages(DataSnapshot dataSnapshot) {

        Iterator iter = dataSnapshot.getChildren().iterator();

        while(iter.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iter.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iter.next()).getValue();
            String chatName = (String) ((DataSnapshot)iter.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iter.next()).getValue();

            textView.append(chatName + " :\n" + chatMessage + ":\n" + chatTime + "    "+ chatDate + "\n\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }



    }

}
