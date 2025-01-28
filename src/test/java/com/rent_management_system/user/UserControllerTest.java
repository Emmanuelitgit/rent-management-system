package com.rent_management_system.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rent_management_system.authentication.OTPComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@ExtendWith(MockitoExtension.class) // Use Mockito JUnit Extension
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Spy
    private UserService userService;
    @Mock
    private OTPComponent otpComponent;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final User user = new User();
    private final UserDTO userDTO = new UserDTO();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); // Initialize MockMvc manually

        // user initialization
        user.setId(1L);
        user.setRole("USER");
        user.setEmail("eyidana001@gmail.com");
        user.setPassword("1234");
        user.setFirstName("Emmanuel");
        user.setLastName("Yidana");
        user.setPhone(89380L);

        // userDTO initialization
        userDTO.setId(1L);
        userDTO.setRole("USER");
        userDTO.setEmail("eyidana001@gmail.com");
        userDTO.setFirstName("Emmanuel");
        userDTO.setLastName("Yidana");
        userDTO.setPhone(89380L);
    }

    @Test
    @Order(1)
    @DisplayName("createUserController Test")
    public void createUserControllerShouldRunSuccessfully() throws Exception {
//        given(userService.createUser(user)).willReturn(userDTO);
        given(userRepository.findUserByEmail("eyidana001@gmail.com")).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        mockMvc.perform(post("/api/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.data.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.data.email", is(user.getEmail())));

        verify(userService, times(1)).createUser(any(User.class));
    }
}