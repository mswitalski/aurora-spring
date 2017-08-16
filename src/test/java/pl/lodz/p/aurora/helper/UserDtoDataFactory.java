package pl.lodz.p.aurora.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.aurora.domain.dto.UserDto;
import pl.lodz.p.aurora.domain.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class providing test data for features related to users in form of DTO.
 */
@Component
public class UserDtoDataFactory {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserDataFactory dataFactory;

    /**
     * Provide a single dummy user DTO without saving to database.
     *
     * @return Dummy user DTO saved to database
     */
    public UserDto createSingle() {
        return convertToDto(dataFactory.createSingle());
    }

    /**
     * Provide as many dummy user DTOs as given without saving to database.
     *
     * @return List of dummy user DTOs saved to the database
     */
    public List<UserDto> createMany(Integer howMany) {
        List<UserDto> generatedUsers = new ArrayList<>();
        IntStream.range(0, howMany).forEach(i -> generatedUsers.add(createSingle()));

        return generatedUsers;
    }

    /**
     * Provide a single dummy user DTO, that was saved to the database.
     *
     * @return Dummy user DTO saved to database
     */
    public UserDto createAndSaveSingle() {
        return convertToDto(dataFactory.createAndSaveSingle());
    }

    /**
     * Provide as many dummy user DTOs as given, that were saved to database.
     *
     * @return List of dummy user DTOs saved to the database
     */
    public List<UserDto> createAndSaveMany(Integer howMany) {
        List<UserDto> generatedUsers = new ArrayList<>();
        IntStream.range(0, howMany).forEach(i -> generatedUsers.add(createAndSaveSingle()));

        return generatedUsers;
    }

    /**
     * Convert given User object to UserDto.
     *
     * @param user Object containing data about a user
     * @return UserDto object
     */
    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
