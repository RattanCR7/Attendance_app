package com.example.attendance;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String userid = currentUser.toString();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,profile.class));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this,signupActivity.class));
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }

    private void userLogin() {
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

        mAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this, profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
