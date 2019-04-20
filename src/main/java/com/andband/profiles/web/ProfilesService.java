package com.andband.profiles.web;

import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.Profile;
import com.andband.profiles.persistence.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ProfilesService {

    private ProfileRepository profileRepository;
    private ProfileMapper profileMapper;
    private ImageService imageService;

    ProfilesService(ProfileRepository profileRepository, ProfileMapper profileMapper, ImageService imageService) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.imageService = imageService;
    }

    ProfileDTO createProfile(String accountId, String name) {
        Profile profile = new Profile();
        profile.setAccountId(accountId);
        profile.setName(name);
        String imageId = imageService.createProfileImagePlaceholder();
        profile.setImageId(imageId);
        profileRepository.save(profile);
        return profileMapper.entityToDTO(profile);
    }

    void updateProfile(ProfileDTO profileDTO) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileDTO.getId());
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            profile.setName(profileDTO.getName());
            profile.setBio(profileDTO.getBio());
            profileRepository.save(profile);
        }

    }

    ProfileDTO getProfileById(String profileId) {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        if (profile == null) {
            throw new ApplicationException("no profile exists with id: " + profileId);
        }
        return profileMapper.entityToDTO(profile);
    }

    ProfileDTO getProfileByAccountId(String accountId) {
        Profile profile = profileRepository.findByAccountId(accountId);
        if (profile == null) {
            throw new ApplicationException("no profile exists with account id: " + accountId);
        }
        return profileMapper.entityToDTO(profile);
    }

    void updateProfileImage(MultipartFile multipartFile, String profileId) {
        String imageId = profileRepository.findImageIdByProfileId(profileId);
        imageService.uploadImage(multipartFile, imageId);
    }

    void validateProfileOwner(String profileId, String accountId) {
        String userProfileId = profileRepository.findProfileIdByAccountId(accountId);
        if (!userProfileId.equals(profileId)) {
            throw new ApplicationException("profile: " + profileId + " is not owned by account: " + accountId);
        }
    }

}
