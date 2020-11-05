package com.example.attendance.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.*;

@Database(entities = {database.class}, version = 1)
public abstract class Mydatabase extends RoomDatabase {

    private static Mydatabase instance;
    public static synchronized Mydatabase getInstance(final Context context){
        if (instance == null) {
            synchronized (Mydatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), Mydatabase.class, "user_table")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
    public abstract userdao userdao();
}
