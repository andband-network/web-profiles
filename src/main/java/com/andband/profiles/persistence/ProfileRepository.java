package com.andband.profiles.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, String> {

    Profile findByAccountId(String accountId);

    @Query("select id from Profile where accountId = :accountId")
    String findProfileIdByAccountId(String accountId);

    @Query("select imageId from Profile where id = :id")
    String findImageIdByProfileId(String id);

}
