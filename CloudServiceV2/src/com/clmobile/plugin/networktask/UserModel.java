package com.clmobile.plugin.networktask;

public class UserModel extends BaseModel {
    private static final long serialVersionUID = -2834876695188126487L;
    public String UserID;

    public String getUserID() {
        return this.UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }
}