package com.andband.profiles.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    private ProfilesService profilesService;

    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileDTO createAccount(@RequestBody ProfileDTO profile) {
        return profilesService.createProfile(profile);
    }

    @GetMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileDTO getAccount(@PathVariable("profileId") String profileId) {
        return profilesService.getProfile(profileId);
    }

}
