package com.andband.profiles.client.notification;

public class NotificationRequest {

    private String email;
    private String toProfileName;
    private String fromProfileName;
    private String text;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToProfileName() {
        return toProfileName;
    }

    public void setToProfileName(String toProfileName) {
        this.toProfileName = toProfileName;
    }

    public String getFromProfileName() {
        return fromProfileName;
    }

    public void setFromProfileName(String fromProfileName) {
        this.fromProfileName = fromProfileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
