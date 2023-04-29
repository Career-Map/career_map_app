package com.example.career_map;

import java.io.Serializable;

public class UserModel implements Serializable {
    String userName, userEmail, userPhoto, userPhone;
    String userBio;
    String userGraduate;
    String userID;

    private Boolean selected;

    public UserModel() {
    }

    public UserModel(String userName, String userEmail, String userPhoto, String userPhone, String userBio, String userGraduate, String userID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
        this.userPhone = userPhone;
        this.userBio = userBio;
        this.userGraduate = userGraduate;
        this.userID = userID;
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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserGraduate() {
        return userGraduate;
    }

    public void setUserGraduate(String userGraduate) {
        this.userGraduate = userGraduate;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean isSelected() {
        return selected;
    }
}
