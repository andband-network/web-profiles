package com.andband.profiles.web;

import com.andband.profiles.persistence.Profile;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ProfileMapper {

    ProfileDTO entityToDTO(Profile profile);

    Profile dtoToEntity(ProfileDTO profileDTO);

    List<ProfileDTO> entityToDTO(Collection<Profile> profiles);

}
