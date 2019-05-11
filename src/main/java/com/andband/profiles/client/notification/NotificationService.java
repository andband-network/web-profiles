package com.andband.profiles.client.notification;

import com.andband.profiles.client.accounts.Account;
import com.andband.profiles.client.accounts.AccountsService;
import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.message.view.MessageView;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import com.andband.profiles.util.RestApiTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {

    private RestApiTemplate notificationApi;
    private AccountsService accountsService;
    private ProfileRepository profileRepository;

    public NotificationService(@Qualifier("notificationApi") RestApiTemplate notificationApi, AccountsService accountsService, ProfileRepository profileRepository) {
        this.notificationApi = notificationApi;
        this.accountsService = accountsService;
        this.profileRepository = profileRepository;
    }

    public void connectionRequest(String profileId, String connectedProfileId) {
        NotificationRequest request = createConnectionNotificationRequest(profileId, connectedProfileId);
        notificationApi.post("/notification/connection-request", request, Void.class);
    }

    public void connectionConfirmed(String profileId, String connectedProfileId) {
        NotificationRequest request = createConnectionNotificationRequest(profileId, connectedProfileId);
        notificationApi.post("/notification/connection-confirmed", request, Void.class);
    }

    private NotificationRequest createConnectionNotificationRequest(String profileId, String connectedProfileId) {
        Profile profile = getProfileFromId(profileId);

        Profile connectedProfile = getProfileFromId(connectedProfileId);
        Account connectedAccount = getAccountFromId(connectedProfile.getAccountId());

        NotificationRequest request = new NotificationRequest();
        request.setEmail(connectedAccount.getEmail());
        request.setToProfileName(connectedProfile.getName());
        request.setFromProfileName(profile.getName());

        return request;
    }

    public void profileMessage(MessageView message) {
        String messageReceiverAccountId = profileRepository.findAccountIdByProfileId(message.getReceiverProfileId());
        Account messageReceiverAccount = getAccountFromId(messageReceiverAccountId);

        NotificationRequest request = new NotificationRequest();
        request.setEmail(messageReceiverAccount.getEmail());
        request.setToProfileName(message.getReceiverProfileName());
        request.setFromProfileName(message.getSenderProfileName());
        request.setText(formatMessageForNotification(message));

        notificationApi.post("/notification/profile-message", request, Void.class);
    }

    private Profile getProfileFromId(String profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (!profile.isPresent()) {
            throw new ApplicationException("no profile found with id " + profileId);
        }
        return profile.get();
    }

    private Account getAccountFromId(String accountId) {
        return accountsService.getAccountFromId(accountId);
    }

    private String formatMessageForNotification(MessageView message) {
        return "Subject: " + message.getSubject() +
                "\n\n" +
                message.getBody();
    }

}
