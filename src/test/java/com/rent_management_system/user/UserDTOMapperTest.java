package com.rent_management_system.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class UserDTOMapperTest {

    @BeforeEach
    void setUp() {
        log.info("runs before each method");
    }

    @AfterEach
    void tearDown() {
        log.info("Runs after each method");
    }

    @Test
    public void testStudentDTOMapper(){
        log.info("jus testing");
    }
}