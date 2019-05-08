package com.andband.profiles.presistence.message;

import com.andband.profiles.persistence.message.Message;

public class MessageBuilder {

    private Message message;

    public MessageBuilder() {
        message = new Message();
    }

    public MessageBuilder withId(String id) {
        message.setId(id);
        return this;
    }

    public MessageBuilder withSenderProfileId(String senderProfileId) {
        message.setSenderProfileId(senderProfileId);
        return this;
    }

    public MessageBuilder withReceiverProfileId(String receiverProfileId) {
        message.setReceiverProfileId(receiverProfileId);
        return this;
    }

    public MessageBuilder withSubject(String subject) {
        message.setSubject(subject);
        return this;
    }

    public MessageBuilder withBody(String body) {
        message.setBody(body);
        return this;
    }

    public Message build() {
        return message;
    }

}
