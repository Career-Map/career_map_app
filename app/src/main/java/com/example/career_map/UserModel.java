package com.example.career_map;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    String userName, userEmail, userPhoto, userPhone;
    String userBio;
    String userLinkedin;
    String userID;

    private Boolean selected;

    public UserModel() {
    }

    public UserModel(String userName, String userEmail, String userPhoto, String userPhone, String userBio, String userLinkedin, String userRole, String organizerRole, String userID, List<String> userChips) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
        this.userPhone = userPhone;
        this.userBio = userBio;
        this.userLinkedin = userLinkedin;
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

    public String getUserLinkedin() {
        return userLinkedin;
    }

    public void setUserLinkedin(String userLinkedin) {
        this.userLinkedin = userLinkedin;
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
