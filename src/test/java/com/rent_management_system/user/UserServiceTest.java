package com.rent_management_system.user;

import com.rent_management_system.authentication.OTP;
import com.rent_management_system.authentication.OTPComponent;
import com.rent_management_system.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    UserDTOMapper userDTOMapper;
    @Mock
    private OTPComponent otpComponent;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private final User user = new User();

    @BeforeEach
    public void initialization(){
        user.setId(1L);
        user.setRole("USER");
        user.setEmail("eyidana003@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setFirstName("Emmanuel");
        user.setLastName("Yidana");
        user.setPhone(89380L);
    }

    @Test
    @Order(1)
    @DisplayName("Create user Test")
    public void createUserShouldReturnUserObject(){
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserDTO addedUser = userService.createUser(user);
        log.info("Added user data:{}", addedUser);
        assertSame(user.getId(), addedUser.getId());
        verify(userRepository).save(user);

    }

    @Test
    @Order(2)
    @DisplayName("FindUserById Test")
    public void findUserByIdShouldReturnUserObject(){
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserDTO ExistingUser = userService.getUserById(1L);
        log.info("user:{}", ExistingUser);
        assertEquals(1, (long) ExistingUser.getId());
    }

    @Test
    @Order(3)
    @DisplayName("userNotFoundException Test")
    public void testUserNotFoundAndItShouldThrowNotFoundException(){
        given(userRepository.findById(2L)).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->{
            userService.getUserById(2L);
        });
        verify(userRepository).findById(2L);
    }

    @Test
    @Order(4)
    @DisplayName("fetchAllUsers Test")
    public void findAllUsersShouldReturnListOfUsers(){
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setRole("USER");
        user1.setEmail("eyidana003@gmail.com");
        user1.setFirstName("Emmanuel");
        user1.setLastName("Yidana");
        user1.setPhone(89380L);
        given(userRepository.findAll()).willReturn(List.of(user));
        given(userDTOMapper.userDTOList(List.of(user))).willReturn(List.of(user1));
        List<UserDTO> users = userService.getUsers();
        log.info("users:{}", users);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("updateUser Test")
    public void updateUserByIdShouldRunSuccessfullyAndReturnTheUpdatedUserObject(){
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserDTO updatedUser = userService.updateUserById(1L, user);
        log.info("updated user:{}", updatedUser);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getId());
    }

    @Test
    @Order(6)
    @DisplayName("deleteUserById Test")
    public void removeUserByIdShouldRunSuccessfully(){
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).deleteById(1L);
        userService.removeUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}