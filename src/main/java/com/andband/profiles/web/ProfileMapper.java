package com.andband.profiles.web;

import com.andband.profiles.persistence.Profile;
import org.mapstruct.Mapper;

@Mapper
public interface ProfileMapper {

    ProfileDTO entityToDTO(Profile profile);

    Profile dtoToEntity(ProfileDTO profileDTO);

}
