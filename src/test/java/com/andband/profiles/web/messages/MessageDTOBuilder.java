package com.andband.profiles.web.messages;

public class MessageDTOBuilder {

    private MessageDTO message;

    public MessageDTOBuilder() {
        message = new MessageDTO();
    }

    public MessageDTOBuilder withSenderProfileId(String senderProfileId) {
        message.setSenderProfileId(senderProfileId);
        return this;
    }

    public MessageDTOBuilder withSenderProfileName(String senderProfileName) {
        message.setSenderProfileName(senderProfileName);
        return this;
    }

    public MessageDTOBuilder withReceiverProfileId(String receiverProfileId) {
        message.setReceiverProfileId(receiverProfileId);
        return this;
    }

    public MessageDTOBuilder withReceiverProfileName(String receiverProfileName) {
        message.setReceiverProfileName(receiverProfileName);
        return this;
    }

    public MessageDTOBuilder withSubject(String subject) {
        message.setSubject(subject);
        return this;
    }

    public MessageDTOBuilder withBody(String body) {
        message.setBody(body);
        return this;
    }

    public MessageDTO build() {
        return message;
    }

}
