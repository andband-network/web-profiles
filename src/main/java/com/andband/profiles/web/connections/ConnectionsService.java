package com.andband.profiles.web.connections;

import com.andband.profiles.client.notification.NotificationService;
import com.andband.profiles.persistence.connection.Connection;
import com.andband.profiles.persistence.connection.ConnectionRepository;
import com.andband.profiles.persistence.connection.pending.PendingConnection;
import com.andband.profiles.persistence.connection.pending.PendingConnectionRepository;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import com.andband.profiles.web.profiles.ProfileDTO;
import com.andband.profiles.web.profiles.ProfileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConnectionsService {

    private PendingConnectionRepository pendingConnectionRepository;
    private ConnectionRepository connectionRepository;
    private ProfileRepository profileRepository;
    private ProfileMapper profileMapper;
    private NotificationService notificationService;

    public ConnectionsService(PendingConnectionRepository pendingConnectionRepository,
                              ConnectionRepository connectionRepository,
                              ProfileRepository profileRepository,
                              ProfileMapper profileMapper,
                              NotificationService notificationService) {
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.connectionRepository = connectionRepository;
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.notificationService = notificationService;
    }

    public List<ProfileDTO> getConnections(String profileId) {
        List<String> connectedProfileIds = connectionRepository.findConnectedProfileIds(profileId);
        Iterable<Profile> connectedProfiles = profileRepository.findAllById(connectedProfileIds);
        return profileMapper.entityToDTO(connectedProfiles);
    }

    public ConnectionStatus addConnection(String profileId, String connectedProfileId) {
        ConnectionStatus connectionStatus;
        PendingConnection pendingConnection = pendingConnectionRepository.findConnection(connectedProfileId, profileId);
        if (pendingConnection == null) {
            createConnectionRequest(profileId, connectedProfileId);
            connectionStatus = ConnectionStatus.PENDING;
        } else {
            confirmConnectionRequest(pendingConnection);
            connectionStatus = ConnectionStatus.CONNECTED;
        }
        return connectionStatus;
    }

    @Transactional
    public void removeConnection(String profileId, String connectedProfileId) {
        pendingConnectionRepository.deleteConnection(profileId, connectedProfileId);
        pendingConnectionRepository.deleteConnection(connectedProfileId, profileId);
        connectionRepository.deleteConnection(profileId, connectedProfileId);
        connectionRepository.deleteConnection(connectedProfileId, profileId);
    }

    public ConnectionStatus getConnectionStatus(String profileId, String connectedProfileId) {
        ConnectionStatus connectionStatus;
        Connection connection = connectionRepository.findConnection(profileId, connectedProfileId);
        if (connection != null) {
            connectionStatus = ConnectionStatus.CONNECTED;
        } else {
            connectionStatus = getPendingConnectionStatus(profileId, connectedProfileId);
        }
        return connectionStatus;
    }

    private ConnectionStatus getPendingConnectionStatus(String profileId, String connectedProfileId) {
        ConnectionStatus connectionStatus = ConnectionStatus.NOT_CONNECTED;
        PendingConnection connectionRequest = pendingConnectionRepository.findConnection(profileId, connectedProfileId);
        if (connectionRequest != null) {
            connectionStatus = ConnectionStatus.PENDING;
        } else {
            PendingConnection connectionConfirmation = pendingConnectionRepository.findConnection(connectedProfileId, profileId);
            if (connectionConfirmation != null) {
                connectionStatus = ConnectionStatus.NEEDS_CONFIRMATION;
            }
        }
        return connectionStatus;
    }

    private void confirmConnectionRequest(PendingConnection pendingConnection) {
        createConnection(pendingConnection.getProfileId(), pendingConnection.getConnectedProfileId());
        createConnection(pendingConnection.getConnectedProfileId(), pendingConnection.getProfileId());
        pendingConnectionRepository.delete(pendingConnection);
    }

    private void createConnection(String profileId1, String profileId2) {
        Connection connectionFrom = new Connection();
        connectionFrom.setProfileId(profileId1);
        connectionFrom.setConnectedProfileId(profileId2);
        connectionRepository.save(connectionFrom);
        notificationService.connectionConfirmed(profileId1, profileId2);
    }

    private void createConnectionRequest(String profileId, String connectedProfileId) {
        PendingConnection pendingConnection = new PendingConnection();
        pendingConnection.setProfileId(profileId);
        pendingConnection.setConnectedProfileId(connectedProfileId);
        pendingConnectionRepository.save(pendingConnection);
        notificationService.connectionRequest(profileId, connectedProfileId);
    }

}
