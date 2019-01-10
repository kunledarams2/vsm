package com.kunledarams.alejoversionspackage.Model;

/**
 * Created by ok on 9/15/2018.
 */

public class HistoryM {

    @com.google.gson.annotations.SerializedName("id")
    public String vID;
    @com.google.gson.annotations.SerializedName("visitorName")
    public String visitorName;
    @com.google.gson.annotations.SerializedName("visitorPhone")
    public String visitorPhone;
    @com.google.gson.annotations.SerializedName("visitorCompany")
    public String visitorCompany;
    @com.google.gson.annotations.SerializedName("visitorPreTime")
    public String visitorPreTime;
    @com.google.gson.annotations.SerializedName("visitorPurpose")
    public  String visitorPurpose;
    @com.google.gson.annotations.SerializedName("visitortosee")
    public String employeeTosee;
    @com.google.gson.annotations.SerializedName("employeeid")
    public String employeeid;
    @com.google.gson.annotations.SerializedName("visitoremail")
    public String visitorEmail;
    @com.google.gson.annotations.SerializedName("visitoraddress")
    public String visitorAddress;
    @com.google.gson.annotations.SerializedName("PreScheduleDate")
    public String PreScheduleDate;
    @com.google.gson.annotations.SerializedName("Status")
    public String status;

    public HistoryM(String visitorName, String visitorPhone, String visitorCompany, String visitorPreTime, String visitorPurpose,String employeeid,
                    String employeeTosee,String status,String PreScheduleDate,String visitorAddress,String visitorEmail) {
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.visitorCompany = visitorCompany;
        this.visitorPreTime = visitorPreTime;
        this.visitorPurpose = visitorPurpose;
        this.employeeid=employeeid;
        this.employeeTosee= employeeTosee;
        this.PreScheduleDate=PreScheduleDate;
        this.visitorEmail= visitorEmail;
        this.visitorAddress= visitorAddress;
        this.status=status;
    }

    public HistoryM() {
    }

    public String getvID() {
        return vID;
    }

    public void setvID(String vID) {
        this.vID = vID;
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

    public String getVisitorCompany() {
        return visitorCompany;
    }

    public void setVisitorCompany(String visitorCompany) {
        this.visitorCompany = visitorCompany;
    }

    public String getVisitorPreTime() {
        return visitorPreTime;
    }

    public void setVisitorPreTime(String visitorPreTime) {
        this.visitorPreTime = visitorPreTime;
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

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getPreScheduleDate() {
        return PreScheduleDate;
    }

    public void setPreScheduleDate(String preScheduleDate) {
        PreScheduleDate = preScheduleDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
