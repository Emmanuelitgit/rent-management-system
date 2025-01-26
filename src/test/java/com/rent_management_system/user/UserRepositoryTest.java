package com.rent_management_system.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ⬅ Prevents replacing DB
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("Test 1")
    @Rollback()
    public void createUserShouldReturnUserObject(){
        User newUser = new User();
        newUser.setRole("USER");
        newUser.setEmail("eyidana003@gmail.com");
        newUser.setPassword("1234");
        newUser.setFirstName("Emmanuel");
        newUser.setLastName("Yidana");
        newUser.setPhone(0256444L);

        User saveUser = userRepository.save(newUser);

        Assertions.assertNotNull(saveUser);
    }
}