package com.example.attendance;

import java.io.Serializable;

public class attendance_data implements Serializable {
    private String roll_no;
    private boolean active;

    public attendance_data(String roll_no){
        this.roll_no = roll_no;
    }

    public attendance_data(String roll_no, boolean active){
        this.roll_no = roll_no;
        this.active = active;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.roll_no;
    }
}
