package com.example.multitakerapp.Model;

public  class UserHostelDetail {
    public String hostelName;
    public String hostelnumber;
    public String hostelArea;
    public String hostelrent;
    public String hostelrooms;
    public String ImageUrl;



    public UserHostelDetail(String hostelName, String hostelnumber, String hostelArea, String hostelrent, String hostelrooms) {
        this.hostelName = hostelName;
        this.hostelnumber = hostelnumber;
        this.hostelArea = hostelArea;
        this.hostelrent = hostelrent;
        this.hostelrooms = hostelrooms;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getHostelnumber() {
        return hostelnumber;
    }

    public void setHostelnumber(String hostelnumber) {
        this.hostelnumber = hostelnumber;
    }

    public String getHostelArea() {
        return hostelArea;
    }

    public void setHostelArea(String hostelArea) {
        this.hostelArea = hostelArea;
    }

    public String getHostelrent() {
        return hostelrent;
    }

    public void setHostelrent(String hostelrent) {
        this.hostelrent = hostelrent;
    }

    public String getHostelrooms() {
        return hostelrooms;
    }

    public void setHostelrooms(String hostelrooms) {
        this.hostelrooms = hostelrooms;
    }
}
