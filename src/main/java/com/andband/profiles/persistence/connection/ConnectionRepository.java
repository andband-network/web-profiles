package com.andband.profiles.persistence.connection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConnectionRepository extends CrudRepository<Connection, String> {

    @Query("select connectedProfileId from Connection where profileId = :profileId")
    List<String> findConnectedProfileIds(String profileId);

    @Query("select c from Connection c where profileId = :profileId and connectedProfileId = :connectedProfileId")
    Connection findConnection(String profileId, String connectedProfileId);

    @Modifying
    @Query("delete from Connection where " +
            "(profileId = :profileId and connectedProfileId = :connectedProfileId) or " +
            "(profileId = :connectedProfileId and connectedProfileId = :profileId)")
    void deleteConnection(String profileId, String connectedProfileId);

}
