package com.andband.profiles.persistence.message;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "sender_profile_id", nullable = false, updatable = false)
    private String senderProfileId;

    @Column(name = "receiver_profile_id", nullable = false, updatable = false)
    private String receiverProfileId;

    @Column(name = "subject", nullable = false, updatable = false)
    private String subject;

    @Column(name = "body", nullable = false, updatable = false)
    private String body;

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

    public String getReceiverProfileId() {
        return receiverProfileId;
    }

    public void setReceiverProfileId(String receiverProfileId) {
        this.receiverProfileId = receiverProfileId;
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
