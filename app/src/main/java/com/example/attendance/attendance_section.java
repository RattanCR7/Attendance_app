package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.example.attendance.data.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.*;

public class attendance_section extends AppCompatActivity {

    ListView listView;
    TextView textView;
    DatePicker datePicker;
    Button save_date,save_attendace;
    Button load_attendance;
    LinearLayout datelinearLayout,linear_date_layout, main_list_layout;
    HashMap<Integer,String> monthmap;
    StringBuilder date_selected;
    FirebaseAuth mAuth;
    String subject_name;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_section);

        bundle = getIntent().getExtras();
        subject_name = bundle.getString("subject");

        mAuth = FirebaseAuth.getInstance();
        final Mydatabase appdb = Mydatabase.getInstance(this);

//        database db = new database();
//        appdb.getInstance(getApplicationContext()).userdao().deleteAll(db);

        textView = (TextView)findViewById(R.id.schedule);
        datePicker = (DatePicker)findViewById(R.id.simpleDatePicker);
        save_date = (Button)findViewById(R.id.calender_save_button);
        load_attendance = (Button)findViewById(R.id.load_attendance);
        datelinearLayout = (LinearLayout)findViewById(R.id.date_layout);
        main_list_layout = (LinearLayout)findViewById(R.id.main_list_layout);
        linear_date_layout = (LinearLayout)findViewById(R.id.load_date_layout);
        listView = (ListView)findViewById(R.id.attendance__list);
        save_attendace = (Button)findViewById(R.id.save_attendance);
        monthmap = new HashMap<>();
        monthmap.put(0,"Jan");monthmap.put(1,"Feb");monthmap.put(2,"Mar");monthmap.put(3,"Apr");monthmap.put(4,"May");
        monthmap.put(5,"Jun");monthmap.put(6,"Jul");monthmap.put(7,"Aug");monthmap.put(8,"Sep");monthmap.put(9,"Oct");
        monthmap.put(10,"Nov");monthmap.put(11,"Dec");

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                attendance_data user = (attendance_data) listView.getItemAtPosition(position);
                user.setActive(!currentCheck);
            }
        });

        save_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                date_selected = new StringBuilder();

                date_selected.append(datePicker.getDayOfMonth()+"/"+ monthmap.get(datePicker.getMonth()) + "/" + datePicker.getYear());
                datelinearLayout.setVisibility(View.GONE);
                linear_date_layout.setVisibility(View.VISIBLE);
                main_list_layout.setVisibility(View.VISIBLE);
                textView.setText(date_selected);
                initListDataView();
            }
        });
        load_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Attendance loading :", Toast.LENGTH_SHORT).show();

                List<database> data = appdb.getInstance(getApplicationContext()).userdao().loadAllById(""+subject_name,""+date_selected.toString());
                StringBuilder info= new StringBuilder();
                for(database datb : data){
                    String sub_name = datb.getSubject();
                    int id = datb.getId();
                    String rollno = datb.getRollno();
                    String date = datb.getDate();
                    String attend = datb.getAttend();
                    info.append("   "+rollno+"   "+sub_name+"   "+date+"   "+attend+"\n");
                }

                Intent intent = new Intent(attendance_section.this,attendance_display.class);
                intent.putExtra("data",info.toString());
                startActivity(intent);
            }
        });
        save_attendace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray sba = listView.getCheckedItemPositions();
                FirebaseUser user = mAuth.getCurrentUser();
                String emailid = user.getEmail();

                appdb.getInstance(getApplicationContext()).userdao().deleteRow(subject_name, date_selected.toString(), emailid);

                for(int i=0;i<listView.getAdapter().getCount()-1;i++){
                    if(sba.get(i)){
                        database userdb = new database(""+subject_name,"",""+date_selected.toString(),Integer.toString(i+1),""+emailid,"Present");
                        appdb.getInstance(getApplicationContext()).userdao().insertAll(userdb);
                    }
                    else{
                        database userdb = new database(""+subject_name,"",""+date_selected.toString(),Integer.toString(i+1),""+emailid,"Absent");
                        appdb.getInstance(getApplicationContext()).userdao().insertAll(userdb);
                    }
                }

                Toast.makeText(getApplicationContext(),"Data Saved :)",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_att_section,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.change_date){
            if(datelinearLayout.getVisibility() == View.GONE){
                datelinearLayout.setVisibility(View.VISIBLE);
                main_list_layout.setVisibility(View.GONE);
            }
            if(linear_date_layout.getVisibility() == View.VISIBLE) {
                linear_date_layout.setVisibility(View.INVISIBLE);
            }
        }
        return true;
    }

    public void initListDataView(){
        ArrayList<attendance_data> user = new ArrayList<>();

        for(int i=1;i<=31;i++){
            user.add(new attendance_data(Integer.toString(i)));
        }

        ArrayAdapter<attendance_data> arrayAdapter
                = new ArrayAdapter<attendance_data>(this, android.R.layout.simple_list_item_checked , user);
        listView.setAdapter(arrayAdapter);

    }
}