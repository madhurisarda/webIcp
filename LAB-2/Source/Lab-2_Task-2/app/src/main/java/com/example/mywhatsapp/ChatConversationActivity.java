package com.example.mywhatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class ChatConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton imagebutton;
    private ImageView button;
    private ImageView cameraButton;
    private ImageView attachButton;
    private EditText editText;
    private ScrollView scrollView;
    private TextView textView;
    private TextView timeView;
    private FirebaseAuth mAuth;
    private  String currentGroup,currentUserid,currentUserName,currentdate,currenttime;
    private DatabaseReference dbRef,groupdbref,groupmessagekeyref, calldbref, callkeyref;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private static final int PICKFILE_RESULT_CODE = 1;
    private int audioCall = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatconversation);

        currentGroup = getIntent().getExtras().get("chatName").toString();
        Toast.makeText(ChatConversationActivity.this,currentGroup,Toast.LENGTH_SHORT);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupdbref = FirebaseDatabase.getInstance().getReference().child("ChatsList").child(currentGroup);
        calldbref = FirebaseDatabase.getInstance().getReference().child("CallsList").child(currentGroup);

        button = findViewById(R.id.buttonCall);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9135498103"));
                if (ContextCompat.checkSelfPermission(ChatConversationActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatConversationActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                }
                audioCall = 1;
                startActivity(callIntent);
                saveCalltoDB(audioCall);

            }
        });

        cameraButton = findViewById(R.id.cameraID);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(cameraIntent);

            }
        });

        attachButton = findViewById(R.id.attachID);

        attachButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent attachIntent = new Intent(Intent.ACTION_GET_CONTENT);
                attachIntent.setType("file/*");
                startActivity(attachIntent);

            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
    protected void onStart() {
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

    private void InitializeFields() {

        toolbar= (Toolbar) findViewById(R.id.single_chat_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroup);

        imagebutton = (ImageButton) findViewById(R.id.send_m);
        editText = (EditText) findViewById(R.id.input_g_message);
        textView = (TextView) findViewById(R.id.chatTV);
        timeView = (TextView) findViewById(R.id.timeTV);
        scrollView = (ScrollView) findViewById(R.id.Scroll_View);
    }


    private void GetuserInfo() {

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

    private void saveCalltoDB(int callType) {
        String messageKEY = calldbref.push().getKey();

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd,yyyy");
        currentdate = currentDateFormat.format(currentDate.getTime());


        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        currenttime = currentTimeFormat.format(currentTime.getTime());

        HashMap<String,Object>  grpmessagekey = new HashMap<>();
        calldbref.updateChildren(grpmessagekey);
        callkeyref = calldbref.child(messageKEY);

        HashMap<String,Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("name",currentUserName);
        messageInfoMap.put("date",currentdate);
        messageInfoMap.put("time",currenttime);
        if(callType == 1)
        {
            messageInfoMap.put("callType", "audio");
        }
        else {
            messageInfoMap.put("callType","video");
        }


        callkeyref.updateChildren(messageInfoMap);
    }

    private void saveMessagetoDb() {
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

    private void DisplayMessages(DataSnapshot dataSnapshot) {

        Iterator iter = dataSnapshot.getChildren().iterator();
        String currentUid = mAuth.getCurrentUser().getDisplayName();

        int TYPE_INCOMING = 0;

        while(iter.hasNext())
        {
            String chatName = (String) ((DataSnapshot)iter.next()).getValue();
            if(chatName != null && currentUid != null && currentUid.equalsIgnoreCase(chatName)) {
                TYPE_INCOMING = 1;
            }
            String chatDate = (String) ((DataSnapshot)iter.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iter.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iter.next()).getValue();

            textView.append(chatName + " :\n" + chatMessage + ":\n" + chatTime + "    "+ chatDate + "\n\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }

    }


}
