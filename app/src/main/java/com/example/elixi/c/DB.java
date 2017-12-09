package com.example.elixi.c;

/**
 * Created by Shmulik on 22 אוקטובר 2017.
 */

public class DB {
    private String time;
    private int  fab;
    public DB(String time, int fab) {

        this.fab = fab;
        this.time = time;
    }

    public int getFab() {
        return fab;
    }

    public void setFab(int fab) {
        this.fab = fab;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DB{" +
                ", fab='" + fab + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
