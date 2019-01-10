package com.kunledarams.alejoversionspackage.Model;

/**
 * Created by ok on 7/31/2018.
 */

public class Preschedule {

    @com.google.gson.annotations.SerializedName("id")
    public String visitorid;
    @com.google.gson.annotations.SerializedName("visitorname")
    public  String visitorName;
    @com.google.gson.annotations.SerializedName("visitoremail")
    public String visitorEmail;
    @com.google.gson.annotations.SerializedName("visitorphone")
    public String visitorPhone;
    @com.google.gson.annotations.SerializedName("visitoradress")
    public  String visitorAdress;
    @com.google.gson.annotations.SerializedName("visitorpurpose")
    public String visitorPurpose;
    @com.google.gson.annotations.SerializedName("visitortosee")
    public String employeeTosee;
    @com.google.gson.annotations.SerializedName("employeeid")
    public String employeeid;
    @com.google.gson.annotations.SerializedName("PreScheduleDate")
    public String PreScheduleDate;
    @com.google.gson.annotations.SerializedName("accesscode")
    public String accesscode;
    @com.google.gson.annotations.SerializedName("visitorCompany")
    public  String visitorCompay;
    @com.google.gson.annotations.SerializedName("validateaccess")
    public String validateaccess;



    public Preschedule(String visitorid, String visitorName, String visitorEmail, String visitorPhone, String visitorAdress,
                       String visitorPurpose, String employeeTosee, String employeeid, String PreScheduleDate, String accesscode, String visitorCompay, String validateaccess) {
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.visitorPhone = visitorPhone;
        this.visitorAdress = visitorAdress;
        this.visitorPurpose = visitorPurpose;
        this.employeeTosee = employeeTosee;
        this.visitorid = visitorid;
        this.employeeid = employeeid;
        this.PreScheduleDate= PreScheduleDate;
        this.accesscode=accesscode;
        this.visitorCompay=visitorCompay;
        this.validateaccess = validateaccess;
    }

    public Preschedule() {
    }


    public String getValidateaccess() {
        return validateaccess;
    }

    public void setValidateaccess(String validateaccess) {
        this.validateaccess = validateaccess;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public String getVisitorCompay() {
        return visitorCompay;
    }

    public void setVisitorCompay(String visitorCompay) {
        this.visitorCompay = visitorCompay;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode = accesscode;
    }

    public String getPreScheduleDate() {
        return PreScheduleDate;
    }

    public void setPreScheduleDate(String preScheduleDate) {
        PreScheduleDate = preScheduleDate;
    }

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getVisitorid() {
        return visitorid;
    }

    public void setVisitorid(String visitorid) {
        this.visitorid = visitorid;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorEmail() {
        return visitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorAdress() {
        return visitorAdress;
    }

    public void setVisitorAdress(String visitorAdress) {
        this.visitorAdress = visitorAdress;
    }

    public String getVisitorPurpose() {
        return visitorPurpose;
    }

    public void setVisitorPurpose(String visitorPurpose) {
        this.visitorPurpose = visitorPurpose;
    }

    public String getEmployeeTosee() {
        return employeeTosee;
    }

    public void setEmployeeTosee(String employeeTosee) {
        this.employeeTosee = employeeTosee;
    }
}
