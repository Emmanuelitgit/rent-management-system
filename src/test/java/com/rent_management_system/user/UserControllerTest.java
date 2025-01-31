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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@ExtendWith(MockitoExtension.class) // Use Mockito JUnit Extension
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Spy
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
        given(userService.createUser(user)).willReturn(userDTO);

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

    @Test
    @Order(2)
    @DisplayName("findAllUsers Test")
    public void findAllUsersShouldReturnListOfUsers() throws Exception {
        given(userService.getUsers()).willReturn(List.of(userDTO));

        ResultActions response = mockMvc.perform(get("/api/users"))
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).getUsers();
    }

    @Test
    @Order(3)
    @DisplayName("findUserById Test")
    public void findAllUserByIdShouldReturnUserObject() throws Exception {
        given(userService.getUserById(1L)).willReturn(userDTO);

        mockMvc.perform(get("/api/user/"+1L))
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @Order(5)
    @DisplayName("removeUserById Test")
    public void removeUserByIdSuccessfully() throws Exception {
        doNothing().when(userService).removeUserById(1L);

        mockMvc.perform(delete("/api/remove-user/"+1L))
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).removeUserById(1L);
    }

    @Test
    @Order(6)
    @DisplayName("updateUserById Test")
    public void updateUserShouldReturnUserObject() throws Exception {
        given(userService.updateUserById(1L, user)).willReturn(userDTO);

        mockMvc.perform(put("/api/update-user/"+1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).updateUserById(1L, user);
    }
}