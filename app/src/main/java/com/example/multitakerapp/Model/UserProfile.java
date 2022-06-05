package com.example.multitakerapp.Model;

public class UserProfile {
    public String userName;
    public String userEmail;
    public String userPhone;
    public String city;
    public String selectUserType;


    public UserProfile(String userName, String userEmail, String userPhone, String city, String selectUserType) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.city = city;
        this.selectUserType = selectUserType;
    }

    public String getSelectUserType() {
        return selectUserType;
    }

    public void setSelectUserType(String selectUserType) {
        this.selectUserType = selectUserType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
