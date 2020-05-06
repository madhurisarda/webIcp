package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settingsactivity extends AppCompatActivity {

    private Button UpdateButton;
    private EditText username,phnnum;
    private CircleImageView userProfileImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private static  final int GalleryPick =1;
    private StorageReference UserProfileImagesRef;
    private ProgressDialog loadingbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsactivity);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference();

        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        InitializeFields();
        RetrieveUserInfo();
        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSettingstoDb();
            }
        });
        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,GalleryPick);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.Settings_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");
    }



    private void InitializeFields() {
        UpdateButton = (Button) findViewById(R.id.update_button);
        username = (EditText) findViewById(R.id.set_user_name);
        phnnum=(EditText) findViewById(R.id.set_mobile_num);
        userProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        loadingbar = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!= null)
        {
            Uri ImageUrl = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please wait,While your Profile Image is Updating");
                loadingbar.setCanceledOnTouchOutside(true);
                loadingbar.show();
                Uri resultUri = result.getUri();
                final StorageReference filePath = UserProfileImagesRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Uri downloaduri = uri;
                               final String downloadUriString = downloaduri.toString();

                                dbRef.child("Users").child(currentUserID).child("image")
                                        .setValue(downloadUriString)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(Settingsactivity.this,"Profile Image Saved Successfully",Toast.LENGTH_SHORT);
                                                    loadingbar.dismiss();
                                                }
                                                else
                                                {
                                                    String message = task.getException().toString();
                                                    Toast.makeText(Settingsactivity.this,"Error:"+message,Toast.LENGTH_SHORT);
                                                    loadingbar.dismiss();
                                                }
                                            }
                                        });

                            }
                        });

                    }
                });

            }
        }
    }



    private void updateSettingstoDb() {

        String Username = username.getText().toString();
        String mobilenum =phnnum.getText().toString();




        if(TextUtils.isEmpty(Username))
        {
            Toast.makeText(this,"Please Enter your Name...",Toast.LENGTH_SHORT);
        }
        if(TextUtils.isEmpty(mobilenum))
        {
            Toast.makeText(this,"Please Enter your Mobile Number...",Toast.LENGTH_SHORT);
        }
        else
        {
            HashMap<String,String> UserMap = new HashMap<>();
            UserMap.put("uid",currentUserID);
            UserMap.put("name",Username);
            UserMap.put("phnnum",mobilenum);
            dbRef.child("Users").child(currentUserID).setValue(UserMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                SenduserToMainActivity();
                                Toast.makeText(Settingsactivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT);
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(Settingsactivity.this,"Error:"+message,Toast.LENGTH_SHORT);
                            }


                        }
                    });
        }
    }

    private void RetrieveUserInfo()
    {
        dbRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && ((dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))))
                        {
                            String Retusername = dataSnapshot.child("name").getValue().toString();
                            String Retphnnum = dataSnapshot.child("phnnum").getValue().toString();
                            String image = dataSnapshot.child("image").getValue().toString();

                            username.setText(Retusername);
                            phnnum.setText(Retphnnum);

                            Glide.with(Settingsactivity.this).load(image).override(100)
                                    .into(userProfileImage);




                        }
                        else if(dataSnapshot.exists() && (dataSnapshot.hasChild("name")))
                        {
                            String Retusername = dataSnapshot.child("name").getValue().toString();
                            String Retphnnum = dataSnapshot.child("phnnum").getValue().toString();


                            username.setText(Retusername);
                            phnnum.setText(Retphnnum);

                        }
                        else
                        {
                            Toast.makeText(Settingsactivity.this,"Please Enter your Pofile Details..",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SenduserToMainActivity() {
        Intent mainintent = new Intent(Settingsactivity.this, MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(Settingsactivity.this);
        startActivity(intent);
        finish();
    }
}
