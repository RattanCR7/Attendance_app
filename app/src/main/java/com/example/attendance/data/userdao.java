package com.example.attendance.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.*;
import java.util.*;

@Dao
public interface userdao {

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    void adduser(database dataBase);

    @Query("SELECT * FROM user_table")
    public List<database> getAll();
    @Query("SELECT * FROM user_table WHERE subject_name = :subject AND date = :date")
    List<database> loadAllById(String subject, String date);
    @Insert
    void insertAll(database... user);
    @Delete
    void deleteAll(database user);
    @Query("DELETE FROM user_table WHERE subject_name = :subject AND date = :date AND Teacher_email_id = :emailid")
    void deleteRow(String subject, String date, String emailid);
}
