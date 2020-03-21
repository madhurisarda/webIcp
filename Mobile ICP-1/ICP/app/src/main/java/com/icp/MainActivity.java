package com.icp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void login(View view){
EditText uname=(EditText) findViewById(R.id.editText);
EditText pwd=(EditText) findViewById(R.id.editText2);
String username=uname.getText().toString();
String password=pwd.getText().toString();

boolean validationflag=false;
if(!username.isEmpty() && !password.isEmpty()){
    if(username.equals("Admin") && password.equals("admin")){
        validationflag=true;
    }
}
if(!validationflag){
    Toast toast=Toast.makeText(getApplicationContext(),"Please enter valid credentials..",Toast.LENGTH_SHORT);
    toast.show();
}
else{
    Intent redirect= new Intent(MainActivity.this,HomeActivity.class);
    startActivity(redirect);
}
    }
}
