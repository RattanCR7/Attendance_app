package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class profile extends AppCompatActivity {
    ArrayList<String> subject_name;
    ArrayAdapter<String> arrayAdapter;
    ListView listViewSubjects;
    DatabaseReference databaseSubjects;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        listViewSubjects = (ListView) findViewById(R.id.subject_listView);
        subject_name = new ArrayList<>();

        readDataFirebase();

        listViewSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String subject_selected = subject_name.get(position);
                Toast.makeText(getApplicationContext(),subject_name.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(profile.this, attendance_section.class);
                intent.putExtra("subject",subject_selected);
                startActivity(intent);
            }
        });

        listViewSubjects.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, final int pos, long id)
            {
                final String sub_sel = subject_name.get(pos);
                new AlertDialog.Builder(profile.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to Delete this subject ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseSubjects = FirebaseDatabase.getInstance().getReference().child("user");
                                databaseSubjects.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String keyid="";
                                        for(DataSnapshot subjectsnap : dataSnapshot.getChildren()){
                                            if(subjectsnap.child("user_email").getValue().toString().equals(userid)) {
                                                if((subjectsnap.child("subjectName").getValue().toString()+" : "+subjectsnap.child("courseName").getValue().toString()).equals(sub_sel)){
                                                    keyid = subjectsnap.getKey();
                                                }
                                            }
                                        }
                                        databaseSubjects.child(keyid).removeValue();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                Toast.makeText(getApplicationContext(),"item deleted",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
    }

    public void readDataFirebase(){
        databaseSubjects = FirebaseDatabase.getInstance().getReference().child("user");
        databaseSubjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subject_name.clear();
                userid = user.getEmail();
                for(DataSnapshot subjectsnap : dataSnapshot.getChildren()){
                    if(subjectsnap.child("user_email").getValue().toString().equals(userid)) {
                        subject_name.add(subjectsnap.child("subjectName").getValue().toString() + " : " + subjectsnap.child("courseName").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subject_name);
        listViewSubjects.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readDataFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuAddSub){
            startActivity(new Intent(this,add_subject.class));
        }
        else if(id == R.id.menuLogout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        return true;
    }
}