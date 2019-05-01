package com.andband.profiles.client.notification;

import com.andband.profiles.client.accounts.Account;
import com.andband.profiles.client.accounts.AccountsService;
import com.andband.profiles.persistence.message.view.MessageView;
import com.andband.profiles.persistence.profile.ProfileRepository;
import com.andband.profiles.util.RestApiTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

    public void profileMessage(MessageView message) {
        String messageReceiverAccountId = profileRepository.findAccountIdByProfileId(message.getReceiverProfileId());
        Account messageReceiverAccount = accountsService.getAccountFromId(messageReceiverAccountId);

        NotificationRequest request = new NotificationRequest();
        request.setEmail(messageReceiverAccount.getEmail());
        request.setUserTo(message.getReceiverProfileName());
        request.setUserFrom(message.getSenderProfileName());
        request.setText(formatMessageForNotification(message));

        notificationApi.post("/notification/profile-message", request, Void.class);
    }

    private String formatMessageForNotification(MessageView message) {
        return message.getSubject() +
                "\n\n" +
                message.getBody();
    }


}
