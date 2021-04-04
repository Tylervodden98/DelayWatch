package com.example.delaywatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private CardView mLogin;
    private FirebaseAuth mAuth;
    private TextView mRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegister = findViewById(R.id.Register) ;
        mLogin = findViewById(R.id.cardView);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();
            //Authentication of password and email
             if(TextUtils.isEmpty(mEmail)||TextUtils.isEmpty(mPassword))
             {
                 email.setError("Email is required");
                 password.setError("Password is required");
                 return;
             }
             mAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         Toast.makeText(MainActivity.this , "Logged In Successfully", Toast.LENGTH_SHORT).show() ;
                     }
                     else
                     {
                      Toast.makeText(MainActivity.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                     }
                 }
             });

            }
        });


    }
}