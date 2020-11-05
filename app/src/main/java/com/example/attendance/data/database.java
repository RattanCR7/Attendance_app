package com.example.attendance.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class database {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "subject_name")
    public String subject;
    @ColumnInfo(name = "branch_name")
    public String branch;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "roll_no")
    public String rollno;
    @ColumnInfo(name = "Teacher_email_id")
    public String emailid;
    @ColumnInfo(name = "attendance")
    public String attend;


    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getBranch() {
        return branch;
    }

    public String getDate() {
        return date;
    }

    public String getRollno() {
        return rollno;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getAttend() {
        return attend;
    }

    public database(){
    }

    public database(String subject, String branch, String date, String rollno, String emailid, String attend) {
        this.subject = subject;
        this.branch = branch;
        this.date = date;
        this.rollno = rollno;
        this.emailid = emailid;
        this.attend = attend;
    }
}
