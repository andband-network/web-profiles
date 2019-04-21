package com.andband.profiles.web;

import com.andband.profiles.config.web.resolver.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    private ProfilesService profilesService;

    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
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
    @ResponseStatus(HttpStatus.OK)
    public void updateProfile(@RequestBody ProfileDTO profile, UserDetails userDetails) {
        profilesService.validateProfileOwner(profile.getId(), userDetails.getAccountId());
        profilesService.updateProfile(profile);
    }

    @PutMapping("/{profileId}/image")
    @ResponseStatus(HttpStatus.OK)
    public void updateProfileImage(@PathVariable("profileId") String profileId, @RequestParam("image") MultipartFile multipartFile, UserDetails userDetails) {
        profilesService.validateProfileOwner(profileId, userDetails.getAccountId());
        profilesService.updateProfileImage(multipartFile, profileId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileDTO> updateProfileImage(@RequestParam("searchParams") List<String> searchParams) {
        return profilesService.searchForProfiles(searchParams);
    }

}
