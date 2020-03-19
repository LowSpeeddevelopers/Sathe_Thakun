package com.nexttech.sathethakun.Model;

import com.nexttech.sathethakun.LoginandRegisterholder;

public class FDM {
    private String Latitude;
    private String Longitude;
    private String Time;

    public FDM(String Latitude,String Longitude,String Time){
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Time = Time;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }
}
