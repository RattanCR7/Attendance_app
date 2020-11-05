package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String Password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email id is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email id");
            editTextEmail.requestFocus();
            return;
        }

        if(Password.length() < 6 ){
            editTextPassword.setError("Password minimum length is 6");
            editTextPassword.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(signupActivity.this, profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if(mAuth.getCurrentUser() != null){
//            finish();
//            startActivity(new Intent(this,profile.class));
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
