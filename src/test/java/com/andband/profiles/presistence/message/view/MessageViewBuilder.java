package com.andband.profiles.presistence.message.view;

import com.andband.profiles.persistence.message.view.MessageView;

public class MessageViewBuilder {

    private MessageView messageView;

    public MessageViewBuilder() {
        messageView = new MessageView();
    }

    public MessageViewBuilder withSenderProfileId(String senderProfileId) {
        messageView.setSenderProfileId(senderProfileId);
        return this;
    }

    public MessageViewBuilder withSenderProfileName(String senderProfileName) {
        messageView.setSenderProfileName(senderProfileName);
        return this;
    }

    public MessageViewBuilder withReceiverProfileId(String receiverProfileId) {
        messageView.setReceiverProfileId(receiverProfileId);
        return this;
    }

    public MessageViewBuilder withReceiverProfileName(String receiverProfileName) {
        messageView.setSenderProfileName(receiverProfileName);
        return this;
    }

    public MessageViewBuilder withSubject(String subject) {
        messageView.setSubject(subject);
        return this;
    }

    public MessageViewBuilder withBody(String body) {
        messageView.setBody(body);
        return this;
    }

    public MessageView build() {
        return messageView;
    }

}
