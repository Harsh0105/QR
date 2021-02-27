package com.example.qr;

public class UserProfile {
    public String userRoll;
    public String userEmail;
    public String userMobile;
    public UserProfile(){
    }

    public UserProfile(String userRoll, String userEmail, String userMobile) {
        this.userRoll = userRoll;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    public String getUserRoll() {
        return userRoll;
    }

    public void setUserRoll(String userRoll) {
        this.userRoll = userRoll;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
