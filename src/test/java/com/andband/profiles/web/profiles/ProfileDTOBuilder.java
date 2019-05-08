package com.andband.profiles.web.profiles;

import com.andband.profiles.persistence.profile.Location;

public class ProfileDTOBuilder {

    private ProfileDTO profile;

    public ProfileDTOBuilder() {
        profile = new ProfileDTO();
    }

    public ProfileDTOBuilder withId(String id) {
        profile.setId(id);
        return this;
    }

    public ProfileDTOBuilder withName(String name) {
        profile.setName(name);
        return this;
    }

    public ProfileDTOBuilder withImageId(String imageId) {
        profile.setImageId(imageId);
        return this;
    }

    public ProfileDTOBuilder withBio(String bio) {
        profile.setBio(bio);
        return this;
    }

    public ProfileDTOBuilder withShowLocation(boolean showLocation) {
        profile.setShowLocation(showLocation);
        return this;
    }

    public ProfileDTOBuilder withLocation(double latitude, double longitude) {
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        return this;
    }

    public ProfileDTO build() {
        return profile;
    }

}
