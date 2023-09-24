package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.user.UserAdRequestDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRegisterDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRequestDTO;
import de.hs.da.hskleinanzeigen.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class UserMapperTest {

    UserMapper userMapper = new UserMapperImpl();

    @Test
    void toUser_ReturnsNullOnNullParam(){
        UserAdRequestDTO dtoX = null;
        UserRegisterDTO dtoY = null;
        assertThat(userMapper.toUser(dtoX)).isNull();
        assertThat(userMapper.toUser(dtoY)).isNull();
    }

    @Test
    void toUser_ReturnsCorrectUser(){
        UserAdRequestDTO dtoX = new UserAdRequestDTO();
        dtoX.setId(0);
        User user = new User();
        user.setId(dtoX.getId());
        assertThat(userMapper.toUser(dtoX).getId()).isEqualTo(user.getId());

        UserRegisterDTO dtoY = new UserRegisterDTO();
        dtoY.setEmail("mail");
        dtoY.setFirstName("firstname");
        dtoY.setLocation("location");
        dtoY.setLastName("lastname");
        dtoY.setPassword("password");
        dtoY.setPhone("phone");

        User userX = new User();
        userX.setEmail(dtoY.getEmail());
        userX.setPassword(dtoY.getPassword());
        userX.setLocation(dtoY.getLocation());
        userX.setPhone(dtoY.getPhone());
        userX.setFirstName(dtoY.getFirstName());
        userX.setLastName(dtoY.getLastName());

        assertThat(userMapper.toUser(dtoY).getEmail()).isEqualTo(userX.getEmail());
        assertThat(userMapper.toUser(dtoY).getFirstName()).isEqualTo(userX.getFirstName());
        assertThat(userMapper.toUser(dtoY).getLocation()).isEqualTo(userX.getLocation());
        assertThat(userMapper.toUser(dtoY).getPassword()).isEqualTo(userX.getPassword());
        assertThat(userMapper.toUser(dtoY).getPhone()).isEqualTo(userX.getPhone());
        assertThat(userMapper.toUser(dtoY).getLastName()).isEqualTo(userX.getLastName());
        assertThat(userMapper.toUser(dtoY).getId()).isEqualTo(userX.getId());
    }

    @Test
    void toUserRequestDTO_ReturnsNullOnNullParam(){
        assertThat(userMapper.toUserResponse(null)).isNull();
    }

    @Test
    void toUserRequestDTO_ReturnsCorrectDTO(){
        User existingUser = new User();
        existingUser.setId(0);
        existingUser.setEmail("email");
        existingUser.setFirstName("firstName");
        existingUser.setLastName("lastName");
        existingUser.setPhone("phone");
        existingUser.setLocation("location");

        UserRequestDTO dto = new UserRequestDTO();
        dto.setId(existingUser.getId());
        dto.setEmail(existingUser.getEmail());
        dto.setFirstName(existingUser.getFirstName());
        dto.setLastName(existingUser.getLastName());
        dto.setPhone(existingUser.getPhone());
        dto.setLocation(existingUser.getLocation());

        assertThat(userMapper.toUserResponse(existingUser)).isEqualTo(dto);
    }
}
