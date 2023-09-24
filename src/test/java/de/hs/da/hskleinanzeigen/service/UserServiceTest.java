package de.hs.da.hskleinanzeigen.service;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
@Testcontainers
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void insertUser_saveUser() {
        User user = new User();
        user.setId(1);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User resUser = userService.insertUser(user);
        assertThat(resUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void isEmailTaken_validResult() {
        User user = arrangeUser();
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User user2 = arrangeUser();
        user2.setEmail("notTaken@email.com");

        boolean isTaken = userService.isEmailTaken(user.getEmail());
        boolean isNotTaken = userService.isEmailTaken(user2.getEmail());

        assertThat(isTaken).isTrue();
        assertThat(isNotTaken).isFalse();
    }

    @Test
    void findById_validRequest() {
        User user = arrangeUser();
        user.setId(1);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User res = userService.findById(1);

        assertThat(res.getId()).isEqualTo(1);
    }

    @Test
    void findById_invalidRequest() {
        User user = arrangeUser();
        user.setId(1);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

        User res = userService.findById(1);

        assertThat(res).isEqualTo(null);
    }

    @Test
    void findAllSortedAscending_validResult() {
        User user = arrangeUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        PageImpl<User> page = new PageImpl<>(users);
        int pageStart = 1;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(pageStart, pageSize, Sort.by("created").ascending());
        Mockito.when(userRepository.findAll(pageRequest)).thenReturn(page);

        List<User> resUsers = userService.findAllSortedAscending(pageStart, pageSize);

        assertThat(resUsers.size()).isEqualTo(1);
        assertThat(resUsers.get(0).getId()).isEqualTo(1);
    }

    @Test
    void isNotComplete_validateAllCases() {
        User user = new User();
        user.setId(1);
        assertThat(userService.isNotComplete(user)).isTrue();
        user.setEmail("user@email.com");
        assertThat(userService.isNotComplete(user)).isTrue();
        user.setFirstName("firstname");
        assertThat(userService.isNotComplete(user)).isTrue();
        user.setLastName("lastname");
        assertThat(userService.isNotComplete(user)).isTrue();
        user.setPassword("password123");
        assertThat(userService.isNotComplete(user)).isTrue();
        user.setPhone("015783729374");
        assertThat(userService.isNotComplete(user)).isTrue();
        user.setLocation("Darmstadt");
        assertThat(userService.isNotComplete(user)).isFalse();
    }

    @Test
    void validateUser_validUser() {
        User user = arrangeUser();
        boolean res = userService.validateUser(user);
        assertThat(res).isTrue();
    }

    @Test
    void validateUser_invalidUserPassword() {
        User user = arrangeUser();
        user.setPassword("123");
        assertThat(userService.validateUser(user)).isFalse();
    }

    @Test
    void validateUser_invalidUserFirstName() {
        User user = arrangeUser();
        user.setFirstName("aa".repeat(257));
        assertThat(userService.validateUser(user)).isFalse();
    }

    @Test
    void validateUser_invalidUserLastName() {
        User user = arrangeUser();
        user.setLastName("aa".repeat(257));
        assertThat(userService.validateUser(user)).isFalse();
    }

    @Test
    void validateUser_invalidUserValuesNotSet() {
        User user = new User();
        assertThatThrownBy(() -> userService.validateUser(user)).isInstanceOf(NullPointerException.class);
        user.setEmail("validEmail@email.com");
        assertThatThrownBy(() -> userService.validateUser(user)).isInstanceOf(NullPointerException.class);
        user.setPassword("validPassword123");
        assertThatThrownBy(() -> userService.validateUser(user)).isInstanceOf(NullPointerException.class);
        user.setFirstName("validFirstName");
        assertThatThrownBy(() -> userService.validateUser(user)).isInstanceOf(NullPointerException.class);
        user.setLastName("validLastName");
        assertThat(userService.validateUser(user)).isTrue();
    }

    private User arrangeUser() {
        User user = new User();
        user.setId(1);
        user.setPassword("Password123");
        user.setFirstName("firsname");
        user.setLastName("lastname");
        user.setLocation("Darmstadt");
        user.setEmail("user@email.com");
        return user;
    }
}
