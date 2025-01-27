package com.rent_management_system.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ⬅ Prevents replacing DB
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final User user = new User();

    @BeforeEach
    public void initialization(){
        user.setId(1L);
        user.setRole("USER");
        user.setEmail("eyidana003@gmail.com");
        user.setPassword("1234");
        user.setFirstName("Emmanuel");
        user.setLastName("Yidana");
        user.setPhone(89380L);
    }

    @Test
    @Order(1)
    @DisplayName("Test 1")
    @Rollback()
    public void createUserShouldReturnUserObject(){
        User saveUser = userRepository.save(user);
        log.info("saved user:{}", saveUser);
        Assertions.assertNotNull(saveUser);
    }

    @Test
    @Order(2)
    @DisplayName("findAllUsers Test")
    public void findAllUsersShouldReturnListOfUsers(){
        List<User> users = userRepository.findAll();
        for (User data:users){
            log.info("data:{}", data.getId());
        }
        assertNotNull(users);
    }

    @Test
    @Order(3)
    @DisplayName("findUserById Test")
    public void findUserByIdShouldReturnUserObject(){
        Optional<User>userOptionalData = userRepository.findById(2710L);
        log.info("user:{}", userOptionalData.get());
        assertNotNull(userOptionalData);
        assertEquals(2710L, userOptionalData.get().getId());
    }

    @Test
    @Order(4)
    @DisplayName("removeUserById Test")
    @Rollback()
    public void removeUserByIdShouldRunSuccessfully(){
        userRepository.deleteById(2710L);
//        verify(userRepository.findById(2710L), times(1));
    }
}