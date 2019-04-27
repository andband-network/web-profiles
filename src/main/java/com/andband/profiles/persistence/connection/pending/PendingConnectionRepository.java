package com.andband.profiles.persistence.connection.pending;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PendingConnectionRepository extends CrudRepository<PendingConnection, String> {

    @Query("select pc from PendingConnection pc where pc.profileId = :profileId and pc.connectedProfileId = :connectedProfileId")
    PendingConnection findConnection(String profileId, String connectedProfileId);

    @Modifying
    @Query("delete from Connection where " +
            "(profileId = :profileId and connectedProfileId = :connectedProfileId) or " +
            "(profileId = :connectedProfileId and connectedProfileId = :profileId)")
    void deleteConnection(String profileId, String connectedProfileId);

}
