package com.andband.profiles.web.profiles;

import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public ProfileDTO createProfile(String accountId, String name) {
        Profile profile = new Profile();
        profile.setAccountId(accountId);
        profile.setName(name);
        String imageId = imageService.createProfileImagePlaceholder();
        profile.setImageId(imageId);
        profileRepository.save(profile);
        return profileMapper.entityToDTO(profile);
    }

    public void updateProfile(ProfileDTO profileDTO) {
        Profile profile = profileMapper.dtoToEntity(profileDTO);
        profileRepository.save(profile);
    }

    public ProfileDTO getProfileById(String profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (!optionalProfile.isPresent()) {
            throw new ApplicationException("no profile exists with id: " + profileId);
        }
        return profileMapper.entityToDTO(optionalProfile.get());
    }

    public ProfileDTO getProfileByAccountId(String accountId) {
        Profile profile = profileRepository.findByAccountId(accountId);
        if (profile == null) {
            throw new ApplicationException("no profile exists with account id: " + accountId);
        }
        return profileMapper.entityToDTO(profile);
    }

    public void updateProfileImage(MultipartFile multipartFile, String profileId) {
        String imageId = profileRepository.findImageIdByProfileId(profileId);
        imageService.uploadImage(multipartFile, imageId);
    }

    public List<ProfileDTO> searchForProfiles(List<String> searchParams) {
        Set<Profile> foundProfiles = new HashSet<>();
        searchParams.forEach(param -> {
            List<Profile> profiles = profileRepository.findByNameContaining(param.trim());
            foundProfiles.addAll(profiles);
        });
        return profileMapper.entityToDTO(foundProfiles);
    }

    public String getProfileIdFromAccountId(String accountId) {
        return profileRepository.findProfileIdByAccountId(accountId);
    }

    public void validateProfileOwner(String profileId, String accountId) {
        String userProfileId = profileRepository.findProfileIdByAccountId(accountId);
        if (!userProfileId.equals(profileId)) {
            throw new ApplicationException("profile: " + profileId + " is not owned by account: " + accountId);
        }
    }

}