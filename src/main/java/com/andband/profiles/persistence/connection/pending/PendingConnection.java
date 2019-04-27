package com.andband.profiles.persistence.connection.pending;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "pending_connection",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_connection_link",
                columnNames = { "profile_id", "connected_profile_id" }
        )
)
public class PendingConnection {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "profile_id", nullable = false, updatable = false)
    private String profileId;

    @Column(name = "connected_profile_id", nullable = false, updatable = false)
    private String connectedProfileId;

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

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getConnectedProfileId() {
        return connectedProfileId;
    }

    public void setConnectedProfileId(String connectedProfileId) {
        this.connectedProfileId = connectedProfileId;
    }

}
