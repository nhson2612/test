package com.nhson.demo11;

import java.util.Date;

public class Led {
    private boolean isOn;
    private String time;
    private String endTime;

    public Led(boolean isOn, String time,String endTime) {
        this.isOn = isOn;
        this.time = time;
        this.endTime = endTime;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setTime(String time,String endTime) {
        this.time = time;
        this.endTime = endTime;
    }

    public boolean isOn() {
        return isOn;
    }

    public String getTime() {
        return time;
    }

    public String getEndTime() {
        return endTime;
    }

    public String toString(){
        return "" +
                "{" +
                "isOn=" + isOn +
                ", time=" + time +
                ", endTime=" + endTime +
                "}";
    }

}
