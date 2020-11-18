package com.example.terrasproject;

public class Student {
    private String studentID;
    private String pw;
    private String studentName;
    private String phoneNumber;

    public Student(){}

    public Student(String studentID, String password, String studentName,String phoneNumber){
        this.studentID = studentID;
        pw = password;
        this.studentName = studentName;
        this.phoneNumber = phoneNumber;
    }
    public String getStudentID(){
        return studentID;
    }

    public void setStudentID(String studentID){
        this.studentID = studentID;
    }

    public String getPassword(){
        return pw;
    }

    public void setPassword(String password){
        pw = password;
    }

    public String getStudentName(){
        return studentName;
    }

    public void setStudentName(String studentName){
        this.studentName = studentName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
}
