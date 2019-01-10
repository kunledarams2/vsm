package com.kunledarams.alejoversionspackage.Model;

/**
 * Created by ok on 7/25/2018.
 */

public class RegistrationForm {
    @com.google.gson.annotations.SerializedName("id")
    public String UserId;
    @com.google.gson.annotations.SerializedName("employeeName")
    public String EmployeeName;
    @com.google.gson.annotations.SerializedName("employeeEmail")
    public  String EmployeeEmail;
    @com.google.gson.annotations.SerializedName("employeePhone")
    public String EmployeePhone;
    @com.google.gson.annotations.SerializedName("employeeId")
    public String EmployeeId;
    @com.google.gson.annotations.SerializedName("employeeDepartment")
    public String EmployeeDepartment;
    @com.google.gson.annotations.SerializedName("employeeImage")
    public String EmployeeImage;

    public RegistrationForm(String UserId, String employeeName, String employeeEmail, String employeePhone, String employeeId, String employeeDepartment, String employeeImage) {
        this.UserId = UserId;
        EmployeeName = employeeName;
        EmployeeEmail = employeeEmail;
        EmployeePhone = employeePhone;
        EmployeeId = employeeId;
        EmployeeDepartment = employeeDepartment;
        EmployeeImage = employeeImage;
    }

    public RegistrationForm() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }



    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return EmployeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        EmployeeEmail = employeeEmail;
    }

    public String getEmployeePhone() {
        return EmployeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        EmployeePhone = employeePhone;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getEmployeeDepartment() {
        return EmployeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        EmployeeDepartment = employeeDepartment;
    }

    public String getEmployeeImage() {
        return EmployeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        EmployeeImage = employeeImage;
    }
}
