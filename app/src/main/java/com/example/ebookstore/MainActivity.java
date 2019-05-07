package com.example.ebookstore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mLoginBtn;
    private Button mSignUpbtn;

    public FirebaseAuth mAuth;
    String email;

    private FirebaseAuth.AuthStateListener mAuthlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);

        mLoginBtn = findViewById(R.id.loginBtn);
        mSignUpbtn = findViewById(R.id.signUp);

        mDatabase = FirebaseDatabase.getInstance().getReference();




        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

            }
        });

        mSignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this , SignUp.class);
                startActivity(in);
            }
        });

        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() != null) {
                        Intent intent = new Intent(MainActivity.this, UserPage.class);
                        String user_id = mAuth.getCurrentUser().getUid();
                        intent.putExtra("key", user_id);
                        startActivity(intent);
                    }
                }

        };


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthlistener);
    }

    private void startSignIn(){

        email = mEmailField.getText().toString();
        final String password = mPasswordField.getText().toString();



        if(email.equalsIgnoreCase("admin@gmail.com") && password.equalsIgnoreCase("admin007")){
            Intent in = new Intent(MainActivity.this , UploadPdf.class);
            startActivity(in);
        }


        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        String user_id = mAuth.getCurrentUser().getUid();

                        if(!email.equalsIgnoreCase("admin@gmail.com") || user_id != null) {
                            Intent loginAct = new Intent(MainActivity.this, UserPage.class);
                            loginAct.putExtra("key", user_id);
                            startActivity(loginAct);

                        }

                    } else {
                        if (email.equalsIgnoreCase("admin@gmail.com") && password.equalsIgnoreCase("admin007")) {
                            Toast.makeText(MainActivity.this, "Admin login", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign in Problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


    }


}
