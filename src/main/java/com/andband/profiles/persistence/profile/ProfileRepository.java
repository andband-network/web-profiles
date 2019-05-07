package com.andband.profiles.persistence.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, String> {

    Profile findByAccountId(String accountId);

    @Query("select id from Profile where accountId = :accountId")
    String findProfileIdByAccountId(String accountId);

    @Query("select accountId from Profile where id = :id")
    String findAccountIdByProfileId(String id);

    @Query("select imageId from Profile where id = :id")
    String findImageIdByProfileId(String id);

    List<Profile> findByNameContaining(String name);

    List<Profile> findByNameContainingAndShowLocationEquals(String name, boolean showLocation);

}
