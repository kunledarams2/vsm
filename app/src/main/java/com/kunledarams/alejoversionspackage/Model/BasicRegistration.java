package com.kunledarams.alejoversionspackage.Model;

/**
 * Created by ok on 9/27/2018.
 */

public class BasicRegistration {
    @com.google.gson.annotations.SerializedName("Id")
    public  String VID;
    @com.google.gson.annotations.SerializedName("visitorName")
    public String visitorName;
    @com.google.gson.annotations.SerializedName("visitorPhone")
    public String visitorPhone;
    @com.google.gson.annotations.SerializedName("visitorEmail")
    public String visitorEmail;
    @com.google.gson.annotations.SerializedName("visitorAddress")
    public String visitorAddress;
    @com.google.gson.annotations.SerializedName("vistorImage")
    public  String vistorImage;
    @com.google.gson.annotations.SerializedName("Gender")
    public  String vistorGender;
    @com.google.gson.annotations.SerializedName("visitorCompany")
    public  String visitorCompany;


    public BasicRegistration(String visitorName, String visitorPhone, String visitorEmail, String visitorAddress, String vistorImage,String vistorGender, String visitorCompany) {
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.visitorEmail = visitorEmail;
        this.visitorAddress = visitorAddress;
        this.vistorImage = vistorImage;
        this.vistorGender=vistorGender;
        this.visitorCompany=visitorCompany;
    }

    public BasicRegistration() {
    }

    public String getVisitorCompany() {
        return visitorCompany;
    }

    public void setVisitorCompany(String visitorCompany) {
        this.visitorCompany = visitorCompany;
    }

    public String getVistorGender() {
        return vistorGender;
    }

    public void setVistorGender(String vistorGender) {
        this.vistorGender = vistorGender;
    }

    public String getVID() {
        return VID;
    }

    public void setVID(String VID) {
        this.VID = VID;
    }

    public String getVistorImage() {
        return vistorImage;
    }

    public void setVistorImage(String vistorImage) {
        this.vistorImage = vistorImage;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorEmail() {
        return visitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getVisitorAddress() {
        return visitorAddress;
    }

    public void setVisitorAddress(String visitorAddress) {
        this.visitorAddress = visitorAddress;
    }
}
