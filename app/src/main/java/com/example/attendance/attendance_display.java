package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class attendance_display extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_display);

        textView = (TextView)findViewById(R.id.attendance_text);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("data");
        if(message.length() == 0)
            message += "     No data available !";

        textView.setText(message);
    }
}