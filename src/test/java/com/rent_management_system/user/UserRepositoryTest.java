package com.rent_management_system.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("Test 1")
    @Rollback()
    public void testCreateUser(){
        User newUser = new User();
        newUser.setRole("USER");
        newUser.setEmail("eyidana003@gmail.com");
        newUser.setPassword("1234");
        newUser.setFirstName("Emmanuel");
        newUser.setLastName("Yidana");
        newUser.setPhone(0256444L);

        userRepository.save(newUser);
        Assertions.assertTrue(newUser.getId()>0);
    }
}
