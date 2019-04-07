package com.andband.profiles.web;

import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.Profile;
import com.andband.profiles.persistence.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfilesService {

    private ProfileRepository profileRepository;
    private ProfileMapper profileMapper;

    ProfilesService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    ProfileDTO createProfile(ProfileDTO profileDTO) {
        Profile profile = profileMapper.dtoToEntity(profileDTO);
        profileRepository.save(profile);
        return profileMapper.entityToDTO(profile);
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

}
