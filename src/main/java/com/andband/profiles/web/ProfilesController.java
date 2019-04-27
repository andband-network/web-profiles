package com.andband.profiles.web;

import com.andband.profiles.config.web.resolver.UserDetails;
import com.andband.profiles.web.connections.ConnectionStatus;
import com.andband.profiles.web.connections.ConnectionsService;
import com.andband.profiles.web.profiles.ProfileDTO;
import com.andband.profiles.web.profiles.ProfilesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    private ProfilesService profilesService;
    private ConnectionsService connectionsService;

    public ProfilesController(ProfilesService profilesService, ConnectionsService connectionsService) {
        this.profilesService = profilesService;
        this.connectionsService = connectionsService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ProfileDTO getProfile(UserDetails userDetails) {
        String accountId = userDetails.getAccountId();
        return profilesService.getProfileByAccountId(accountId);
    }

    @GetMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileDTO getProfile(@PathVariable("profileId") String profileId) {
        return profilesService.getProfileById(profileId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileDTO createProfile(@RequestParam("accountId") String accountId, @RequestParam("name") String name) {
        return profilesService.createProfile(accountId, name);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@RequestBody ProfileDTO profile, UserDetails userDetails) {
        validateProfileOwner(profile.getId(), userDetails.getAccountId());
        profilesService.updateProfile(profile);
    }

    @PutMapping("/{profileId}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfileImage(@PathVariable("profileId") String profileId,
                                   @RequestParam("image") MultipartFile multipartFile,
                                   UserDetails userDetails) {
        validateProfileOwner(profileId, userDetails.getAccountId());
        profilesService.updateProfileImage(multipartFile, profileId);
    }

    @GetMapping("/{profileId}/connections")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileDTO> getConnections(@PathVariable("profileId") String profileId) {
        return connectionsService.getConnections(profileId);
    }

    @GetMapping("/{profileId}/connections/{connectedProfileId}/status")
    @ResponseStatus(HttpStatus.OK)
    public ConnectionStatus getConnectionStatus(@PathVariable("profileId") String profileId,
                                                @PathVariable("connectedProfileId") String connectedProfileId,
                                                UserDetails userDetails) {
        validateProfileOwner(profileId, userDetails.getAccountId());
        return connectionsService.getConnectionStatus(profileId, connectedProfileId);
    }

    @PostMapping("/{profileId}/connections/{connectedProfileId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionStatus addConnection(@PathVariable("profileId") String profileId,
                                          @PathVariable("connectedProfileId") String connectedProfileId,
                                          UserDetails userDetails) {
        validateProfileOwner(profileId, userDetails.getAccountId());
        return connectionsService.addConnection(profileId, connectedProfileId);
    }

    @DeleteMapping("/{profileId}/connections/{connectedProfileId}")
    @ResponseStatus(HttpStatus.OK)
    public ConnectionStatus removeConnection(@PathVariable("profileId") String profileId,
                                             @PathVariable("connectedProfileId") String connectedProfileId,
                                             UserDetails userDetails) {
        validateProfileOwner(profileId, userDetails.getAccountId());
        connectionsService.removeConnection(profileId, connectedProfileId);
        return ConnectionStatus.NOT_CONNECTED;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileDTO> updateProfileImage(@RequestParam("searchParams") List<String> searchParams) {
        return profilesService.searchForProfiles(searchParams);
    }

    private void validateProfileOwner(String profileId, String accountId) {
        profilesService.validateProfileOwner(profileId, accountId);
    }

}
