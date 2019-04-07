package com.andband.profiles.web;

import com.andband.profiles.config.web.resolver.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ProfileDTO createAccount(@RequestBody ProfileDTO profile) {
        return profilesService.createProfile(profile);
    }

}
