package com.andband.profiles.web.messages;

import com.andband.profiles.client.notification.NotificationService;
import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.message.Message;
import com.andband.profiles.persistence.message.MessageRepository;
import com.andband.profiles.persistence.message.view.MessageView;
import com.andband.profiles.persistence.message.view.MessageViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessagesService {

    private MessageRepository messageRepository;
    private MessageViewRepository messageViewRepository;
    private MessageMapper messageMapper;
    private NotificationService notificationService;

    public MessagesService(MessageRepository messageRepository, MessageViewRepository messageViewRepository, MessageMapper messageMapper, NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.messageViewRepository = messageViewRepository;
        this.messageMapper = messageMapper;
        this.notificationService = notificationService;
    }

    public void sendMessage(String senderProfileId, String receiverProfileId, MessageDTO messageDTO) {
        Message message = messageMapper.dtoToEntity(messageDTO);
        message.setSenderProfileId(senderProfileId);
        message.setReceiverProfileId(receiverProfileId);
        messageRepository.save(message);

        Optional<MessageView> messageView = messageViewRepository.findById(message.getId());
        if (!messageView.isPresent()) {
            throw new ApplicationException("error retrieving message view from database");
        }
        notificationService.profileMessage(messageView.get());
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
