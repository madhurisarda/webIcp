package com.example.mywhatsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabAccessorAdapter myTabAccessorAdapter;
    private FirebaseUser currentuser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        currentuser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Whatsapp");

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }

    protected  void onStart()
    {
        super.onStart();
        if (currentuser == null) {
            SendUserToLoginActivity();
        }
        else
        {
            UserExist();
        }


    }

    private void UserExist() {

        String userID = mAuth.getCurrentUser().getUid();
        dbRef.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("name").exists())
                {
                    Toast.makeText(MainActivity.this,"welcome",Toast.LENGTH_SHORT);
                }
                else
                {
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    protected  void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()== R.id.log_out)
         {
             mAuth.signOut();
             SendUserToLoginActivity();

         }
         if(item.getItemId()==R.id.settings)
         {
             SendUserToSettingsActivity();

         }
         if(item.getItemId()==R.id.CreateGroup)
         {
             CreateGroup();
         }
         if(item.getItemId()== R.id.SearchContacts)
         {
             SendUserToSearchContactsActivity();
         }

         return true;
    }

    private void CreateGroup() {

        AlertDialog.Builder alertgroup = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        alertgroup.setTitle("Enter Group Name :");
        final EditText groupname = new EditText(MainActivity.this);
        groupname.setHint("Group Name ...");
        alertgroup.setView(groupname);
        alertgroup.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupname.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(MainActivity.this, "please Enter Group Name...", Toast.LENGTH_SHORT);
                } else {

                    CreateNewGroup(groupName);
                }
            }
        });
        alertgroup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertgroup.show();
    }

    private void CreateNewGroup(final String groupName) {

        dbRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,groupName+"Group is Created Successfully..",Toast.LENGTH_SHORT);
                        }

                    }
                });
    }

    protected  void SendUserToSettingsActivity()
    {
        Intent settingsintent = new Intent(MainActivity.this,Settingsactivity.class);
        settingsintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsintent);
        finish();

    }

    protected  void SendUserToSearchContactsActivity()
    {
        Intent searchcontacts = new Intent(MainActivity.this,Searchcontact.class);

        startActivity(searchcontacts);
        finish();

    }
}
