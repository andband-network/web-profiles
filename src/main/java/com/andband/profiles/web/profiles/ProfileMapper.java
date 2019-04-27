package com.andband.profiles.web.profiles;

import com.andband.profiles.persistence.profile.Profile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProfileMapper {

    ProfileDTO entityToDTO(Profile profile);

    Profile dtoToEntity(ProfileDTO profileDTO);

    List<ProfileDTO> entityToDTO(Iterable<Profile> profiles);

}
