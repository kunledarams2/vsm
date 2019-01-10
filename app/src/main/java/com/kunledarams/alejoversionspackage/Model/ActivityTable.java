package com.kunledarams.alejoversionspackage.Model;

/**
 * Created by ok on 9/27/2018.
 */

public class ActivityTable {
    @com.google.gson.annotations.SerializedName("Id")
    private String PID;
    @com.google.gson.annotations.SerializedName("VisitorID")
    private String vID;
    @com.google.gson.annotations.SerializedName("Visitor_Name")
    private String vPName;
    @com.google.gson.annotations.SerializedName("Whom_TO_SEE")
    private String staffTosee;
    @com.google.gson.annotations.SerializedName("Purpose")
    private String purpose;
    @com.google.gson.annotations.SerializedName("AccessCode")
    private String AccessCode;
    @com.google.gson.annotations.SerializedName("Status")
    private String Status;


    public ActivityTable(String vID, String vPName, String staffTosee, String purpose, String AccessCode,String Status) {
        this.vID = vID;
        this.vPName = vPName;
        this.staffTosee = staffTosee;
        this.purpose = purpose;
        this. AccessCode= AccessCode;
        this.Status= Status;
    }

    public ActivityTable() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getAccessCode() {
        return AccessCode;
    }

    public void setAccessCode(String accessCode) {
        AccessCode = accessCode;
    }

    public String getvID() {
        return vID;
    }

    public void setvID(String vID) {
        this.vID = vID;
    }

    public String getvPName() {
        return vPName;
    }

    public void setvPName(String vPName) {
        this.vPName = vPName;
    }

    public String getStaffTosee() {
        return staffTosee;
    }

    public void setStaffTosee(String staffTosee) {
        this.staffTosee = staffTosee;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
