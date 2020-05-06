package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton;
    private EditText UserEmail,UserPassword;
    private TextView AlreadyHaveAccountLink;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
        InitializeFields();

        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAccount();
            }
        });

    }

    private void RegisterAccount() {

        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();


        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email ",Toast.LENGTH_SHORT);
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT);
        }
        else
        {
            progressDialog.setTitle("Registering Your Account ");
            progressDialog.setMessage("Plesae wait, while we are creating new account");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String currentUserId = mAuth.getCurrentUser().getUid();
                                dbReference.child("Users").child(currentUserId).setValue("");
                                SenduserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this,"Error:" +message,Toast.LENGTH_SHORT);
                                progressDialog.dismiss();
                            }

                        }
                    });
        }
    }

    private void SenduserToMainActivity() {
        Intent mainintent = new Intent(RegisterActivity.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();


    }

    private void InitializeFields() {
        RegisterButton = (Button) findViewById(R.id.register_button);
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        AlreadyHaveAccountLink = (TextView) findViewById(R.id.already_have_account);
         progressDialog = new ProgressDialog(this);

    }

    private void SendUserToLoginActivity() {

        Intent loginintent = new Intent(RegisterActivity.this,loginActivity.class);
        startActivity(loginintent);
    }
}
