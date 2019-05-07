package com.andband.profiles.web.profiles;

import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.profile.Location;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private ProfileRepository profileRepository;
    private ProfileMapper profileMapper;

    public SearchService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    public List<ProfileDTO> searchForProfiles(List<String> searchParams) {
        Set<Profile> foundProfiles = new HashSet<>();
        searchParams.forEach(param -> {
            List<Profile> profiles = profileRepository.findByNameContaining(param.trim());
            foundProfiles.addAll(profiles);
        });
        return profileMapper.entityToDTO(foundProfiles);
    }

    public List<ProfileDTO> searchForProfiles(List<String> searchParams, double rangeInKilometers, String profileId) {
        Location location = getProfileLocation(profileId);
        Location currentLocation = getLocation(location.getLat(), location.getLng());
        Map<Profile, Double> profileRangeMap = new HashMap<>();
        searchParams.forEach(param -> {
            List<Profile> profiles = profileRepository.findByNameContainingAndShowLocationEquals(param.trim(), true);
            Map<Profile, Double> profilesInRange = getProfilesInLocationRange(profiles, currentLocation, rangeInKilometers);
            profileRangeMap.putAll(profilesInRange);
        });
        List<Profile> foundProfiles = orderProfilesByRange(profileRangeMap);
        return profileMapper.entityToDTO(foundProfiles);
    }

    private Location getProfileLocation(String profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (!profile.isPresent()) {
            throw new ApplicationException("profile does not exits with id" + profileId);
        }
        return profile.get().getLocation();
    }

    private List<Profile> orderProfilesByRange(Map<Profile, Double> profileRangeMap) {
        final Map<Profile, Double> sortedProfileRangeMap = profileRangeMap.entrySet()
                .stream()
                .sorted((Map.Entry.<Profile, Double>comparingByValue()))
                .collect(
                        Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (profile1, profile2) -> profile1,
                                LinkedHashMap::new));
        return new ArrayList<>(sortedProfileRangeMap.keySet());
    }

    private Location getLocation(double latitude, double longitude) {
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        return location;
    }

    private Map<Profile, Double> getProfilesInLocationRange(List<Profile> profiles, Location location, double rangeInKilometers) {
        Map<Profile, Double> profileRangeMap = new HashMap<>();
        profiles.forEach(profile -> {
            if (profile.isShowLocation() && hasLocationSet(profile.getLocation())) {
                double distanceBetween = getDistanceBetweenInKilometers(location, profile.getLocation());
                if (distanceBetween < rangeInKilometers) {
                    profileRangeMap.put(profile, distanceBetween);
                }
            }
        });
        return profileRangeMap;
    }

    private boolean hasLocationSet(Location location) {
        return location != null && location.getLat() != 0 && location.getLng() != 0;
    }

    private double getDistanceBetweenInKilometers(Location location1, Location location2) {
        double r = 6371;
        double latitude1 = Math.toRadians(location1.getLat());
        double latitude2 = Math.toRadians(location2.getLat());
        double latitudeDistance = Math.toRadians(location2.getLat() - location1.getLat());
        double longitudeDistance = Math.toRadians(location2.getLng() - location1.getLng());

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2) +
                Math.cos(latitude1) * Math.cos(latitude2) *
                        Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return r * c;
    }

}
