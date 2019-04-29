package com.andband.profiles.web.messages;

import com.andband.profiles.persistence.message.Message;
import com.andband.profiles.persistence.message.MessageRepository;
import com.andband.profiles.persistence.message.view.MessageView;
import com.andband.profiles.persistence.message.view.MessageViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {

    private MessageRepository messageRepository;
    private MessageViewRepository messageViewRepository;
    private MessageMapper messageMapper;

    public MessagesService(MessageRepository messageRepository, MessageViewRepository messageViewRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageViewRepository = messageViewRepository;
        this.messageMapper = messageMapper;
    }

    public void sendMessage(String senderProfileId, String receiverProfileId, MessageDTO messageDTO) {
        Message message = messageMapper.dtoToEntity(messageDTO);
        message.setSenderProfileId(senderProfileId);
        message.setReceiverProfileId(receiverProfileId);
        messageRepository.save(message);
    }

    public List<MessageDTO> getMessages(String profileId) {
        List<MessageView> receivedMessages = messageViewRepository.findByReceiverProfileId(profileId);
        return messageMapper.entityToDTO(receivedMessages);
    }

    public List<MessageDTO> getSentMessages(String profileId) {
        List<MessageView> messages = messageViewRepository.findBySenderProfileId(profileId);
        return messageMapper.entityToDTO(messages);
    }

}
