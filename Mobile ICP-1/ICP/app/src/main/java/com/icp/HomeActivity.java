package com.icp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }
    public void logout(View view){
        Intent redirect= new Intent(HomeActivity.this,MainActivity.class);
        startActivity(redirect);
    }
}
