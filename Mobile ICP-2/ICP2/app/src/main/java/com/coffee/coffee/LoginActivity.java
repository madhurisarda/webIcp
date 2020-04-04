package com.coffee.coffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "LogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    //This method will call when the user clicks on login button
    public void login(View view) {
        TextView txtEmail = findViewById(R.id.txtUsername);
        String email = txtEmail.getText().toString();
        TextView txtPassword = findViewById(R.id.txtPassword);
        String password = txtPassword.getText().toString();

        TextView lblUsernameError = (TextView) findViewById(R.id.lblUsernameError);
        TextView lblPasswordError = (TextView) findViewById(R.id.lblPasswordError);
        //Setting empty string to the error labels.
        lblUsernameError.setText("");
        lblPasswordError.setText("");

        //Checking the username is empty or not.
        if(email.isEmpty())
        {
            lblUsernameError.setText("Please enter the email.");
        }
        //Checking the password is empty or not.
        else if(password.isEmpty())
        {
            lblPasswordError.setText("Please enter password");
        }
        //Validating the username and password.
        else
        {
            if(txtEmail.getText().toString().equals("madhurisarda32@gmail.com") &&
                    txtPassword.getText().toString().equals("madhuri")) {
                Toast.makeText(getApplicationContext(),
                        "Redirecting...",Toast.LENGTH_SHORT).show();
                reDirectToHomePage();
            }
        }
    }

    public void register(View view) {
        Intent redirect = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(redirect);
    }
    public void reDirectToHomePage(){
        Intent redirect = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(redirect);
    }
}
