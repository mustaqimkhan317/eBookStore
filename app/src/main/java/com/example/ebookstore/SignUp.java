package com.example.ebookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mSignUp;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mNameField = findViewById(R.id.nameField);
        mEmailField = findViewById(R.id.emailSignUp);
        mPasswordField = findViewById(R.id.passwordSignUp);


        mSignUp = findViewById(R.id.signUp);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });






    }

    private void startRegister() {
        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_usser_db = mDatabase.child(user_id);

                        current_usser_db.child("Name").setValue(name);

                       // Intent loginAct = new Intent(SignUp.this , UserPage.class);
                       // loginAct.putExtra("key" , user_id);
                       // startActivity(loginAct);

                        //SignUp.this.finish();


                    }
                }
            });


        }








    }
}
