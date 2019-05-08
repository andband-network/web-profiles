package com.andband.profiles.client.notification;

import com.andband.profiles.client.accounts.Account;
import com.andband.profiles.client.accounts.AccountBuilder;
import com.andband.profiles.client.accounts.AccountsService;
import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.message.view.MessageView;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import com.andband.profiles.presistence.message.view.MessageViewBuilder;
import com.andband.profiles.presistence.profile.ProfileBuilder;
import com.andband.profiles.util.RestApiTemplate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private RestApiTemplate mockNotificationApi;

    @Mock
    private AccountsService mockAccountsService;

    @Mock
    private ProfileRepository mockProfileRepository;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void testConnectionRequest() {
        String profileId = "abcd1234";
        String connectedProfileId = "efgh5678";

        Profile profile = new ProfileBuilder()
                .withId(profileId)
                .withName("User1")
                .withAccountId("account123")
                .withBio("Something about User1")
                .build();
        Optional<Profile> optionalProfile = Optional.of(profile);
        Profile connectedProfile = new ProfileBuilder()
                .withId(connectedProfileId)
                .withName("User2")
                .withAccountId("account456")
                .withBio("Something about User2")
                .build();
        Optional<Profile> optionalConnectedProfile = Optional.of(connectedProfile);
        Account connectedAccount = new AccountBuilder()
                .withId(connectedProfile.getAccountId())
                .withName(connectedProfile.getName())
                .withEmail("connectedUser@email.com")
                .build();

        when(mockProfileRepository.findById(profileId)).thenReturn(optionalProfile);
        when(mockProfileRepository.findById(connectedProfileId)).thenReturn(optionalConnectedProfile);
        when(mockAccountsService.getAccountFromId(connectedProfile.getAccountId())).thenReturn(connectedAccount);

        notificationService.connectionRequest(profileId, connectedProfileId);

        verify(mockProfileRepository).findById(profileId);
        verify(mockProfileRepository).findById(connectedProfileId);
        verify(mockAccountsService).getAccountFromId(connectedProfile.getAccountId());
        verify(mockNotificationApi).post(eq("/notification/connection-request"), any(NotificationRequest.class), eq(Void.class));

    }

    @Test(expectedExceptions = ApplicationException.class)
    public void testConnectionRequestProfileDoesExist() {
        String profileId = "abcd1234";
        String connectedProfileId = "connected123";
        Optional<Profile> optionalProfile = Optional.empty();

        when(mockProfileRepository.findById(profileId)).thenReturn(optionalProfile);

        notificationService.connectionRequest(profileId, connectedProfileId);
    }

    @Test
    public void testConnectionConfirmed() {
        String profileId = "abcd1234";
        String connectedProfileId = "efgh5678";

        Profile profile = new ProfileBuilder()
                .withId(profileId)
                .withName("User1")
                .withAccountId("account123")
                .withBio("Something about User1")
                .build();
        Optional<Profile> optionalProfile = Optional.of(profile);
        Profile connectedProfile = new ProfileBuilder()
                .withId(connectedProfileId)
                .withName("User2")
                .withAccountId("account456")
                .withBio("Something about User2")
                .build();
        Optional<Profile> optionalConnectedProfile = Optional.of(connectedProfile);
        Account connectedAccount = new AccountBuilder()
                .withId(connectedProfile.getAccountId())
                .withName(connectedProfile.getName())
                .withEmail("connectedUser@email.com")
                .build();

        when(mockProfileRepository.findById(profileId)).thenReturn(optionalProfile);
        when(mockProfileRepository.findById(connectedProfileId)).thenReturn(optionalConnectedProfile);
        when(mockAccountsService.getAccountFromId(connectedProfile.getAccountId())).thenReturn(connectedAccount);

        notificationService.connectionConfirmed(profileId, connectedProfileId);

        verify(mockProfileRepository).findById(profileId);
        verify(mockProfileRepository).findById(connectedProfileId);
        verify(mockAccountsService).getAccountFromId(connectedProfile.getAccountId());
        verify(mockNotificationApi).post(eq("/notification/connection-confirmed"), any(NotificationRequest.class), eq(Void.class));
    }

    @Test
    public void testProfileMessage() {
        MessageView messageView = new MessageViewBuilder()
                .withSenderProfileId("sender123")
                .withSenderProfileName("senderName")
                .withReceiverProfileId("receiver123")
                .withReceiverProfileName("receiverName")
                .withSubject("Message Subject")
                .withBody("Message Body")
                .build();

        String messageReceiverAccountId = "receiverAccount123";

        Account messageReceiverAccount = new AccountBuilder()
                .withId(messageReceiverAccountId)
                .withName("receiverName")
                .withEmail("messageReceiver@email.com")
                .build();

        when(mockProfileRepository.findAccountIdByProfileId(messageView.getReceiverProfileId())).thenReturn(messageReceiverAccountId);
        when(mockAccountsService.getAccountFromId(messageReceiverAccountId)).thenReturn(messageReceiverAccount);

        notificationService.profileMessage(messageView);

        verify(mockProfileRepository).findAccountIdByProfileId(messageView.getReceiverProfileId());
        verify(mockAccountsService).getAccountFromId(messageReceiverAccountId);
        verify(mockNotificationApi).post(eq("/notification/profile-message"), any(NotificationRequest.class), eq(Void.class));
    }

}
