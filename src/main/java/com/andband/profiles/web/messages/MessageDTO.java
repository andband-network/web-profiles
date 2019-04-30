package com.andband.profiles.web.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String senderProfileId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String senderProfileName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String receiverProfileId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String receiverProfileName;

    private String subject;

    private String body;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getSenderProfileId() {
        return senderProfileId;
    }

    public void setSenderProfileId(String senderProfileId) {
        this.senderProfileId = senderProfileId;
    }

    public String getSenderProfileName() {
        return senderProfileName;
    }

    public void setSenderProfileName(String senderProfileName) {
        this.senderProfileName = senderProfileName;
    }

    public String getReceiverProfileId() {
        return receiverProfileId;
    }

    public void setReceiverProfileId(String receiverProfileId) {
        this.receiverProfileId = receiverProfileId;
    }

    public String getReceiverProfileName() {
        return receiverProfileName;
    }

    public void setReceiverProfileName(String receiverProfileName) {
        this.receiverProfileName = receiverProfileName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
