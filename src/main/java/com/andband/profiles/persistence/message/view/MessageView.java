package com.andband.profiles.persistence.message.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Immutable
@Table(name = "vw_message")
public class MessageView {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "sender_profile_id", updatable = false)
    private String senderProfileId;

    @Column(name = "sender_profile_name", updatable = false)
    private String senderProfileName;

    @Column(name = "receiver_profile_id", updatable = false)
    private String receiverProfileId;

    @Column(name = "receiver_profile_name", updatable = false)
    private String receiverProfileName;

    @Column(name = "subject", updatable = false)
    private String subject;

    @Column(name = "text", updatable = false)
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
