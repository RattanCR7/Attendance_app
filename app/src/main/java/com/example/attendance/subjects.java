package com.example.attendance;

public class subjects {

    String Subjectid;
    String SubjectName;
    String CourseName;
    String user_email;
    public subjects(){

    }

    public subjects(String userid){
        this.user_email = userid;
    }

    public subjects(String userid, String subjectid, String subjectName, String courseName) {
        this.Subjectid = subjectid;
        this.SubjectName = subjectName;
        this.CourseName = courseName;
        this.user_email = userid;
    }

    public String getUser_email() {
        return user_email;
    }
    public String getSubjectid() {
        return Subjectid;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public String getCourseName() {
        return CourseName;
    }
}
