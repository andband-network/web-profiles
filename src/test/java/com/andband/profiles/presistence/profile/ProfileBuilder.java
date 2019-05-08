package com.andband.profiles.presistence.profile;

import com.andband.profiles.persistence.profile.Location;
import com.andband.profiles.persistence.profile.Profile;

public class ProfileBuilder {

    private Profile profile;

    public ProfileBuilder() {
        profile = new Profile();
    }

    public ProfileBuilder withId(String id) {
        profile.setId(id);
        return this;
    }

    public ProfileBuilder withAccountId(String accountId) {
        profile.setAccountId(accountId);
        return this;
    }

    public ProfileBuilder withName(String name) {
        profile.setName(name);
        return this;
    }

    public ProfileBuilder withImageId(String imageId) {
        profile.setImageId(imageId);
        return this;
    }

    public ProfileBuilder withBio(String bio) {
        profile.setBio(bio);
        return this;
    }

    public ProfileBuilder withShowLocation(boolean showLocation) {
        profile.setShowLocation(showLocation);
        return this;
    }

    public ProfileBuilder withLocation(double latitude, double longitude) {
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        return this;
    }

    public Profile build() {
        return profile;
    }

}
