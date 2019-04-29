package com.andband.profiles.persistence.message.view;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageViewRepository extends CrudRepository<MessageView, Long> {

    List<MessageView> findByReceiverProfileId(String profileId);

    List<MessageView> findBySenderProfileId(String profileId);

}
