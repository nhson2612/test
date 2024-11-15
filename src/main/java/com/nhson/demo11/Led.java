package com.nhson.demo11;

import java.util.Date;

public class Led {
    private boolean isOn;
    private String time;

    public Led(boolean isOn, String time) {
        this.isOn = isOn;
        this.time = time;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOn() {
        return isOn;
    }

    public String getTime() {
        return time;
    }

    public String toString(){
        return "" +
                "{" +
                "isOn=" + isOn +
                ", time=" + time +
                "}";
    }

}
