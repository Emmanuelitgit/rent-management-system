package com.rent_management_system.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final User user = new User();
    private final UserDTO userDTO = new UserDTO();


    @BeforeEach
    public void initialization(){
        // user initialization
        user.setId(1L);
        user.setRole("USER");
        user.setEmail("eyidana003@gmail.com");
        user.setPassword("1234");
        user.setFirstName("Emmanuel");
        user.setLastName("Yidana");
        user.setPhone(89380L);

        // userDTO initialization
        userDTO.setId(1L);
        userDTO.setRole("USER");
        userDTO.setEmail("eyidana003@gmail.com");
        userDTO.setFirstName("Emmanuel");
        userDTO.setLastName("Yidana");
        userDTO.setPhone(89380L);
    }


    @Test
    @Order(1)
    @DisplayName("createUserController Test")
    public void createUserControllerShouldRunSuccessfully() throws Exception {
        given(userService.createUser(user)).willReturn(userDTO);

        ResultActions response = mockMvc.perform(post("/api/create-user", user));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(userService.createUser(user), times(1));
    }
}