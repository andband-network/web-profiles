package com.andband.profiles.web.messages;

import com.andband.profiles.client.notification.NotificationService;
import com.andband.profiles.persistence.message.Message;
import com.andband.profiles.persistence.message.MessageRepository;
import com.andband.profiles.persistence.message.view.MessageView;
import com.andband.profiles.persistence.message.view.MessageViewRepository;
import com.andband.profiles.presistence.message.MessageBuilder;
import com.andband.profiles.presistence.message.view.MessageViewBuilder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessagesServiceTest {

    @InjectMocks
    private MessagesService messagesService;

    @Mock
    private MessageRepository mockMessageRepository;

    @Mock
    private MessageViewRepository mockMessageViewRepository;

    @Mock
    private MessageMapper mockMessageMapper;

    @Mock
    private NotificationService mockNotificationService;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void testSendMessage() {
        String senderProfileId = "sender123";
        String senderProfileName = "senderName";
        String receiverProfileId = "receiver123";
        String receiverProfileName = "receiverName";
        String subject = "Message Subject";
        String body = "Message Body";

        MessageDTO messageDTO = new MessageDTOBuilder()
                .withSubject(subject)
                .withBody(body)
                .build();
        Message message = new MessageBuilder()
                .withSubject(subject)
                .withBody(body)
                .build();

        MessageView messageView = new MessageViewBuilder()
                .withSenderProfileId(senderProfileId)
                .withSenderProfileName(senderProfileName)
                .withReceiverProfileId(receiverProfileId)
                .withReceiverProfileName(receiverProfileName)
                .withSubject(subject)
                .withBody(body)
                .build();

        Optional<MessageView> optionalMessageView = Optional.of(messageView);

        when(mockMessageMapper.dtoToEntity(messageDTO)).thenReturn(message);
        when(mockMessageViewRepository.findById(any())).thenReturn(optionalMessageView);

        messagesService.sendMessage(senderProfileId, receiverProfileId, messageDTO);

        assertThat(message.getReceiverProfileId()).isEqualTo(receiverProfileId);
        assertThat(message.getSenderProfileId()).isEqualTo(senderProfileId);

        verify(mockMessageMapper).dtoToEntity(messageDTO);
        verify(mockMessageRepository).save(message);
        verify(mockMessageViewRepository).findById(any());
        verify(mockNotificationService).profileMessage(optionalMessageView.get());
    }

    @Test
    public void testGetMessages() {
        String profileId = "profile123";

        List<MessageView> receivedMessages = new ArrayList<>();
        List<MessageDTO> expectedMessages = new ArrayList<>();

        when(mockMessageViewRepository.findByReceiverProfileId(profileId)).thenReturn(receivedMessages);
        when(mockMessageMapper.entityToDTO(receivedMessages)).thenReturn(expectedMessages);

        List<MessageDTO> actualMessages = messagesService.getMessages(profileId);

        assertThat(actualMessages).isEqualTo(expectedMessages);

        verify(mockMessageViewRepository).findByReceiverProfileId(profileId);
        verify(mockMessageMapper).entityToDTO(receivedMessages);
    }

    @Test
    public void testGetSentMessages() {
        String profileId = "profile123";

        List<MessageView> receivedMessages = new ArrayList<>();
        List<MessageDTO> expectedMessages = new ArrayList<>();

        when(mockMessageViewRepository.findBySenderProfileId(profileId)).thenReturn(receivedMessages);
        when(mockMessageMapper.entityToDTO(receivedMessages)).thenReturn(expectedMessages);

        List<MessageDTO> actualMessages = messagesService.getSentMessages(profileId);

        assertThat(actualMessages).isEqualTo(expectedMessages);

        verify(mockMessageViewRepository).findBySenderProfileId(profileId);
        verify(mockMessageMapper).entityToDTO(receivedMessages);
    }

}
