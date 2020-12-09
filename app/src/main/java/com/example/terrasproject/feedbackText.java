package com.example.terrasproject;

public class feedbackText {

    public String feedback;

    public feedbackText(){}

    public String getFeedback() {
        System.out.println("getfeedback"+feedback);

        return feedback;}

    public void setFeedback(String feedback) {
        System.out.println("parameter"+feedback);
        this.feedback = feedback;
        System.out.println("setfeedback"+this.feedback);
    }


}
