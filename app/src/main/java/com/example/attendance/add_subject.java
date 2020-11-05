package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_subject extends AppCompatActivity {

    EditText editTextName;
    Button buttonSave;
    Spinner spinnercourse;
    DatabaseReference databaseSubjects;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        databaseSubjects = FirebaseDatabase.getInstance().getReference("user");

        editTextName = (EditText)findViewById(R.id.enter_subject_name);
        buttonSave = (Button)findViewById(R.id.buttonsave);
        spinnercourse = (Spinner)findViewById(R.id.spinnercourse);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSubject(){
        String subname = editTextName.getText().toString().trim();
        String course = spinnercourse.getSelectedItem().toString();

        if(!TextUtils.isEmpty(subname)){
            finish();
            FirebaseUser user = mAuth.getCurrentUser();
            String user_email = user.getEmail();
            String id = databaseSubjects.push().getKey();
            subjects subjects = new subjects(user_email,id, subname, course);
            databaseSubjects.child(id).setValue(subjects);
        }
        else{
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_SHORT).show();
        }
    }
}
