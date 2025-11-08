package by.Shelden.api;

import by.Shelden.dto.UserDto;
import by.Shelden.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApi userApi;

    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUser(){
        openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userApi)
                .setControllerAdvice(new by.Shelden.exeptions.GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        userDto = new UserDto(
                1L,
                "Jhon",
                "sometestmail@gmail.com",
                30,
                LocalDate.of(2024, 5, 12)
        );
    }

    @Test
    void createUserTest() throws Exception {
        var requestUser = new UserDto(
                null,
                "Jhon",
                "sometestmail@gmail.com",
                30,
                null
        );

        when(userService.createUser(requestUser)).thenReturn(userDto);

        mockMvc.perform(post("/User_Service/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jhon"))
                .andExpect(jsonPath("$.email").value("sometestmail@gmail.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void createUserConflictTest() throws Exception {
        var duplicateUser = new UserDto(
                null,
                "Jhon",
                "sometestmail@gmail.com",
                30,
                null
        );

        when(userService.createUser(duplicateUser)).thenThrow(new DataIntegrityViolationException("Email already exists"));

        mockMvc.perform(post("/User_Service/create_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Conflict"))
                .andExpect(jsonPath("$.detailMessage").value("Record already exists or violates unique constraint"));
    }

    @Test
    void getUserTest() throws Exception {
        when(userService.getUser(1L)).thenReturn(userDto);

        mockMvc.perform(get("/User_Service/get_user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jhon"))
                .andExpect(jsonPath("$.email").value("sometestmail@gmail.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void getNonExistentUserTest() throws Exception {
        when(userService.getUser(2L)).thenThrow(new EntityNotFoundException("User with ID 2 not found"));

        mockMvc.perform(get("/User_Service/get_user/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity not found"))
                .andExpect(jsonPath("$.detailMessage").value("User with ID 2 not found"));
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/User_Service/get_all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Jhon"))
                .andExpect(jsonPath("$[0].email").value("sometestmail@gmail.com"));

    }

    @Test
    void updateUserSuccessTest() throws Exception {
        UserDto updatedUser = new UserDto(
                null,
                "Jhon Updated",
                "sometestmail@gmail.com",
                26,
                null
                );

        when(userService.updateUser(1L, updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(post("/User_Service/update_user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jhon Updated"))
                .andExpect(jsonPath("$.email").value("sometestmail@gmail.com"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void updateUserNotFoundTest() throws Exception {
        UserDto updatedUser = new UserDto(
                null,
                "Jhon Updated",
                "sometestmail@gmail.com",
                26,
                null
        );

        when(userService.updateUser(2L, updatedUser)).thenThrow(new EntityNotFoundException("User with ID 2 not found"));

        mockMvc.perform(post("/User_Service/update_user/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity not found"))
                .andExpect(jsonPath("$.detailMessage").value("User with ID 2 not found"));
    }

    @Test
    void deleteUserSuccessTest() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/User_Service/delete/1"))
                .andExpect(status().isOk());
    }



}