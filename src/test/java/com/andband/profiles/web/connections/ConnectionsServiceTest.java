package com.andband.profiles.web.connections;

import com.andband.profiles.client.notification.NotificationService;
import com.andband.profiles.persistence.connection.Connection;
import com.andband.profiles.persistence.connection.ConnectionRepository;
import com.andband.profiles.persistence.connection.pending.PendingConnection;
import com.andband.profiles.persistence.connection.pending.PendingConnectionRepository;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import com.andband.profiles.presistence.profile.ProfileBuilder;
import com.andband.profiles.web.profiles.ProfileDTO;
import com.andband.profiles.web.profiles.ProfileDTOBuilder;
import com.andband.profiles.web.profiles.ProfileMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectionsServiceTest {

    @InjectMocks
    private ConnectionsService connectionsService;

    @Mock
    private PendingConnectionRepository mockPendingConnectionRepository;

    @Mock
    private ConnectionRepository mockConnectionRepository;

    @Mock
    private ProfileRepository mockProfileRepository;

    @Mock
    private ProfileMapper mockProfileMapper;

    @Mock
    private NotificationService mockNotificationService;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void testGetConnections() {
        String profileId = "profile1";

        List<String> connectedProfileIds = Arrays.asList("profile2", "profile3");
        Iterable<Profile> connectedProfiles = Arrays.asList(
                new ProfileBuilder()
                        .withId("profile2")
                        .withName("User2")
                        .build(),
                new ProfileBuilder()
                        .withId("profile3")
                        .withName("User3")
                        .build()
        );
        List<ProfileDTO> expectedConnectedProfileDTOs = Arrays.asList(
                new ProfileDTOBuilder()
                        .withId("profile2")
                        .withName("User2")
                        .build(),
                new ProfileDTOBuilder()
                        .withId("profile3")
                        .withName("User3")
                        .build()
        );

        when(mockConnectionRepository.findConnectedProfileIds(profileId)).thenReturn(connectedProfileIds);
        when(mockProfileRepository.findAllById(connectedProfileIds)).thenReturn(connectedProfiles);
        when(mockProfileMapper.entityToDTO(connectedProfiles)).thenReturn(expectedConnectedProfileDTOs);

        List<ProfileDTO> actualConnectedProfiles = connectionsService.getConnections(profileId);

        assertThat(actualConnectedProfiles).isSameAs(expectedConnectedProfileDTOs);

        verify(mockConnectionRepository).findConnectedProfileIds(profileId);
        verify(mockProfileRepository).findAllById(connectedProfileIds);
        verify(mockProfileMapper).entityToDTO(connectedProfiles);
    }

    @Test
    public void testAddConnectionPending() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        when(mockPendingConnectionRepository.findConnection(connectedProfileId, profileId)).thenReturn(null);

        ConnectionStatus connectionStatus = connectionsService.addConnection(profileId, connectedProfileId);

        assertThat(connectionStatus).isEqualTo(ConnectionStatus.PENDING);

        verify(mockPendingConnectionRepository).findConnection(connectedProfileId, profileId);
        verify(mockPendingConnectionRepository).save(any(PendingConnection.class));
        verify(mockNotificationService).connectionRequest(profileId, connectedProfileId);
    }

    @Test
    public void testAddConnectionConfirmed() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        PendingConnection pendingConnection = new PendingConnection();
        pendingConnection.setProfileId(connectedProfileId);
        pendingConnection.setConnectedProfileId(profileId);

        when(mockPendingConnectionRepository.findConnection(connectedProfileId, profileId)).thenReturn(pendingConnection);

        ConnectionStatus connectionStatus = connectionsService.addConnection(profileId, connectedProfileId);

        assertThat(connectionStatus).isEqualTo(ConnectionStatus.CONNECTED);

        verify(mockPendingConnectionRepository).findConnection(connectedProfileId, profileId);
        verify(mockConnectionRepository, times(2)).save(any(Connection.class));
        verify(mockNotificationService).connectionConfirmed(profileId, connectedProfileId);
        verify(mockNotificationService).connectionConfirmed(connectedProfileId, profileId);
        verify(mockPendingConnectionRepository).delete(pendingConnection);
    }

    @Test
    public void testRemoveConnection() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        connectionsService.removeConnection(profileId, connectedProfileId);

        verify(mockPendingConnectionRepository).deleteConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).deleteConnection(connectedProfileId, profileId);
        verify(mockPendingConnectionRepository).deleteConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).deleteConnection(connectedProfileId, profileId);
    }

    @Test
    public void testGetConnectionStatusConnected() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        when(mockConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(new Connection());

        ConnectionStatus connectionStatus = connectionsService.getConnectionStatus(profileId, connectedProfileId);

        assertThat(connectionStatus).isEqualTo(ConnectionStatus.CONNECTED);

        verify(mockConnectionRepository).findConnection(profileId, connectedProfileId);
    }

    @Test
    public void testGetConnectionStatusPending() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        when(mockConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(null);
        when(mockPendingConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(new PendingConnection());

        ConnectionStatus connectionStatus = connectionsService.getConnectionStatus(profileId, connectedProfileId);

        assertThat(connectionStatus).isEqualTo(ConnectionStatus.PENDING);

        verify(mockConnectionRepository).findConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).findConnection(profileId, connectedProfileId);
    }

    @Test
    public void testGetConnectionStatusNeedsConfirmation() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        when(mockConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(null);
        when(mockPendingConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(null);
        when(mockPendingConnectionRepository.findConnection(connectedProfileId, profileId)).thenReturn(new PendingConnection());

        ConnectionStatus connectionStatus = connectionsService.getConnectionStatus(profileId, connectedProfileId);

        assertThat(connectionStatus).isEqualTo(ConnectionStatus.NEEDS_CONFIRMATION);

        verify(mockConnectionRepository).findConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).findConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).findConnection(connectedProfileId, profileId);
    }

    @Test
    public void testGetConnectionStatusNotConnected() {
        String profileId = "profile1";
        String connectedProfileId = "profile2";

        when(mockConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(null);
        when(mockPendingConnectionRepository.findConnection(profileId, connectedProfileId)).thenReturn(null);
        when(mockPendingConnectionRepository.findConnection(connectedProfileId, profileId)).thenReturn(null);

        ConnectionStatus connectionStatus = connectionsService.getConnectionStatus(profileId, connectedProfileId);

        assertThat(connectionStatus).isEqualTo(ConnectionStatus.NOT_CONNECTED);

        verify(mockConnectionRepository).findConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).findConnection(profileId, connectedProfileId);
        verify(mockPendingConnectionRepository).findConnection(connectedProfileId, profileId);
    }

}
