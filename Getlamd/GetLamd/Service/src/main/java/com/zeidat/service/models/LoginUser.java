package com.zeidat.service.models;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    @SerializedName("nationalId")
    private String nationalId ;

    @SerializedName("password")
    private String password ;

    public LoginUser(String nationalId, String password) {
        this.nationalId = nationalId;
        this.password = password;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
