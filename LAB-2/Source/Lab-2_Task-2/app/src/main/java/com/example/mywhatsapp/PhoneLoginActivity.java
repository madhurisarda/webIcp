package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private Button sendverifybutton,verifybutton;
    private FirebaseAuth mAuth;
    private EditText phninput,verifyinput;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog pdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        sendverifybutton = (Button) findViewById(R.id.send_verify_button);
        verifybutton = (Button) findViewById(R.id.verify_button);
        phninput = (EditText) findViewById(R.id.phone_num_input);
        verifyinput = (EditText) findViewById(R.id.verification_code_input);
        mAuth = FirebaseAuth.getInstance();
        pdialog = new ProgressDialog(this);

        sendverifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String phnnum = phninput.getText().toString();
                if(TextUtils.isEmpty(phnnum))
                {
                    Toast.makeText(PhoneLoginActivity.this,"Please Enter Your Phone Number...",Toast.LENGTH_SHORT);
                }
                else
                {
                    pdialog.setTitle("Phone Verification...");
                    pdialog.setMessage("Please Wait, while we are authenticating your login...");
                    pdialog.setCanceledOnTouchOutside(false);
                    pdialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phnnum,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneLoginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendverifybutton.setVisibility(View.INVISIBLE);
                phninput.setVisibility(View.INVISIBLE);

                String verificationcode = verifyinput.getText().toString();

                if(TextUtils.isEmpty(verificationcode))
                {
                    Toast.makeText(PhoneLoginActivity.this, "Please enter the Code", Toast.LENGTH_SHORT).show();
                }
                else {

                    pdialog.setTitle("Code Verification...");
                    pdialog.setMessage("Please Wait, while we are verifying your code...");
                    pdialog.setCanceledOnTouchOutside(false);
                    pdialog.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pdialog.dismiss();

                Toast.makeText(PhoneLoginActivity.this,"Invalid Mobile Number",Toast.LENGTH_SHORT);

                sendverifybutton.setVisibility(View.VISIBLE);
                phninput.setVisibility(View.VISIBLE);
                verifybutton.setVisibility(View.INVISIBLE);
                verifyinput.setVisibility(View.INVISIBLE);

            }


            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                pdialog.dismiss();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(PhoneLoginActivity.this,"Verification Code has been sent to your number..",Toast.LENGTH_SHORT);
                sendverifybutton.setVisibility(View.INVISIBLE);
                phninput.setVisibility(View.INVISIBLE);
                verifybutton.setVisibility(View.VISIBLE);
                verifyinput.setVisibility(View.VISIBLE);


            }
        };


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pdialog.dismiss();
                            Toast.makeText(PhoneLoginActivity.this,"You are logged in Successfully",Toast.LENGTH_SHORT);
                            sendUsertoMainactivity();
                        }
                        else
                        {

                            String message = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this,"Error:"+message,Toast.LENGTH_SHORT);
                        }
                    }

                });
    }

    private void sendUsertoMainactivity() {

        Intent mainintent = new Intent(PhoneLoginActivity.this,MainActivity.class);
        startActivity(mainintent);
        finish();
    }
}
