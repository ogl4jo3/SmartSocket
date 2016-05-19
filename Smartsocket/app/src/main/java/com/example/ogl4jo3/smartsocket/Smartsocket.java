package com.example.ogl4jo3.smartsocket;

public class Smartsocket {
    private String name;
    private int starttime_hour, starttime_minute, endtime_hour, endtime_minute;
    //private int imageId;
    //public Smartsocket(String name, int price, int imageId) {
    public Smartsocket(String name, int starttime_hour, int starttime_minute, int endtime_hour, int endtime_minute) {
        setName(name);
        setstarttime_hour(starttime_hour);
        setstarttime_minute(starttime_minute);
        setendtime_hour(endtime_hour);
        setendtime_minute(endtime_minute);
        //setImageId(imageId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getstarttime_hour() {
        return starttime_hour;
    }

    public void setstarttime_hour(int starttime_hour) {
        this.starttime_hour = starttime_hour;
    }

    public int getstarttime_minute() {
        return starttime_minute;
    }

    public void setstarttime_minute(int starttime_minute) {
        this.starttime_minute = starttime_minute;
    }

    public int getendtime_hour() {
        return endtime_hour;
    }

    public void setendtime_hour(int endtime_hour) {
        this.endtime_hour = endtime_hour;
    }

    public int getendtime_minute() {
        return endtime_minute;
    }

    public void setendtime_minute(int endtime_minute) {
        this.endtime_minute = endtime_minute;
    }
    /*public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }*/
}
