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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {


    private Button LoginButton,PhoneLoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccountLink,ForgotPasswordLink;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        InitializeFields();
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserLogin();
            }
        });

        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phnloginintent = new Intent(loginActivity.this,PhoneLoginActivity.class);
                startActivity(phnloginintent);
            }
        });
    }

    private void UserLogin() {
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
            progressDialog.setTitle("Looging into Your Account ");
            progressDialog.setMessage("Plesae wait, while we are logging in");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                SenduserToMainActivity();
                                Toast.makeText(loginActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT);
                                progressDialog.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(loginActivity.this,"Error:"+message,Toast.LENGTH_SHORT);
                                progressDialog.dismiss();
                            }

                        }
                    });
        }
    }

    private void InitializeFields() {


        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButton = (Button) findViewById(R.id.phone_login_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        NeedNewAccountLink = (TextView) findViewById(R.id.need_new_account_link);
        ForgotPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        progressDialog = new ProgressDialog(this);
    }



    private void SenduserToMainActivity() {
        Intent mainintent = new Intent(loginActivity.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();


    }

    private void SendUserToRegisterActivity() {

        Intent registerintent = new Intent(loginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
    }
}
