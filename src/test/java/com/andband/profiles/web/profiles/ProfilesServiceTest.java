package com.andband.profiles.web.profiles;

import com.andband.profiles.exception.ApplicationException;
import com.andband.profiles.persistence.profile.Profile;
import com.andband.profiles.persistence.profile.ProfileRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProfilesServiceTest {

    @InjectMocks
    private ProfilesService profilesService;

    @Mock
    private ProfileRepository mockProfileRepository;

    @Mock
    private ProfileMapper mockProfileMapper;

    @Mock
    private ImageService mockImageService;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void testCreateProfile() {
        String accountId = "account1234";
        String name = "User1";

        ProfileDTO expectedProfile = new ProfileDTO();

        when(mockImageService.createProfileImagePlaceholder()).thenReturn("image123");
        when(mockProfileMapper.entityToDTO(any(Profile.class))).thenReturn(expectedProfile);

        ProfileDTO actualProfile = profilesService.createProfile(accountId, name);

        assertThat(actualProfile).isEqualTo(expectedProfile);

        verify(mockImageService).createProfileImagePlaceholder();
        verify(mockProfileRepository).save(any(Profile.class));
        verify(mockProfileMapper).entityToDTO(any(Profile.class));
    }

    @Test
    public void testUpdateProfile() {
        ProfileDTO profileDTO = new ProfileDTO();
        Profile profile = new Profile();

        when(mockProfileMapper.dtoToEntity(profileDTO)).thenReturn(profile);

        profilesService.updateProfile(profileDTO);

        verify(mockProfileMapper).dtoToEntity(profileDTO);
        verify(mockProfileRepository).save(profile);
    }

    @Test
    public void testGetProfileById() {
        String profileId = "profile123";

        Optional<Profile> profileEntity = Optional.of(new Profile());
        ProfileDTO expectedProfile = new ProfileDTO();

        when(mockProfileRepository.findById(profileId)).thenReturn(profileEntity);
        when(mockProfileMapper.entityToDTO(profileEntity.get())).thenReturn(expectedProfile);

        ProfileDTO actualProfile = profilesService.getProfileById(profileId);

        assertThat(actualProfile).isEqualTo(expectedProfile);

        verify(mockProfileRepository).findById(profileId);
        verify(mockProfileMapper).entityToDTO(profileEntity.get());
    }

    @Test(expectedExceptions = ApplicationException.class)
    public void testGetProfileByIdNoFound() {
        String profileId = "profile123";

        Optional<Profile> profileEntity = Optional.empty();

        when(mockProfileRepository.findById(profileId)).thenReturn(profileEntity);

        profilesService.getProfileById(profileId);
    }

    @Test
    public void testGetProfileByAccountId() {
        String accountId = "account123";

        Profile profileEntity = new Profile();
        ProfileDTO expectedProfile = new ProfileDTO();

        when(mockProfileRepository.findByAccountId(accountId)).thenReturn(profileEntity);
        when(mockProfileMapper.entityToDTO(profileEntity)).thenReturn(expectedProfile);

        ProfileDTO actualProfile = profilesService.getProfileByAccountId(accountId);

        assertThat(actualProfile).isEqualTo(expectedProfile);

        verify(mockProfileRepository).findByAccountId(accountId);
        verify(mockProfileMapper).entityToDTO(profileEntity);
    }

    @Test
    public void testUpdateProfileImage() {
        String profileId = "profileId";
        MultipartFile multipartFile = null;
        String imageId = "image123";

        when(mockProfileRepository.findImageIdByProfileId(profileId)).thenReturn(imageId);

        profilesService.updateProfileImage(profileId, multipartFile);

        verify(mockProfileRepository).findImageIdByProfileId(profileId);
        verify(mockImageService).uploadImage(multipartFile, imageId);
    }

    @Test
    public void testValidateProfileOwner() {
        String profileId = "profile123";
        String accountId = "accountId";

        when(mockProfileRepository.findProfileIdByAccountId(accountId)).thenReturn(profileId);

        profilesService.validateProfileOwner(profileId, accountId);

        verify(mockProfileRepository).findProfileIdByAccountId(accountId);
    }

    @Test(expectedExceptions = ApplicationException.class)
    public void testValidateProfileOwnerInvalid() {
        String profileId = "profile123";
        String accountId = "accountId";

        when(mockProfileRepository.findProfileIdByAccountId(accountId)).thenReturn("abc123");

        profilesService.validateProfileOwner(profileId, accountId);
    }

}
