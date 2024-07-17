package com.onevoker.blogapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onevoker.blogapi.domain.services.interfaces.UserService;
import com.onevoker.blogapi.dto.requests.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc api;

    private ObjectMapper objectMapper;

    private static final String USERS_ENDPOINT = "/users";
    private static final String ID_PARAM = "/{id}";
    private static final String POSTS_ENDPOINT = "/posts";
    private static final String BETWEEN_DATES_PARAM = "/between-dates";
    private static final String START_DATE_PARAM = "startDate";
    private static final String END_DATE_PARAM = "endDate";

    @BeforeEach
    void setUp() {
        api = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testGetAllUsers() throws Exception {
        api.perform(get(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        int userId = 1;

        api.perform(get(USERS_ENDPOINT + ID_PARAM, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @Test
    void testGetUserPosts() throws Exception {
        int userId = 1;

        api.perform(get(USERS_ENDPOINT + ID_PARAM + POSTS_ENDPOINT, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUserPosts(userId);
    }

    @Test
    void testGetUserPostsBetweenDates() throws Exception {
        int userId = 1;
        String startDate = String.valueOf(OffsetDateTime.now());
        String endDate = String.valueOf(OffsetDateTime.now().plusDays(1));

        api.perform(get(USERS_ENDPOINT + ID_PARAM + POSTS_ENDPOINT + BETWEEN_DATES_PARAM, userId)
                        .param(START_DATE_PARAM, startDate)
                        .param(END_DATE_PARAM, endDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUserPostsBetweenDates(anyInt(), any(OffsetDateTime.class), any(OffsetDateTime.class));
    }

    @Test
    void testCreateUser() throws Exception {
        UserRequest request = new UserRequest("Jame", "1111");

        api.perform(post(USERS_ENDPOINT)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).createUser(request);
    }

    @Test
    void testCreateUserWithUncorrectData() throws Exception {
        UserRequest request = new UserRequest("", "");

        api.perform(post(USERS_ENDPOINT)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(request);
    }

    @Test
    void testUpdateUser() throws Exception {
        int userId = 1;
        UserRequest request = new UserRequest("Wow", "coolPassword");

        api.perform(put(USERS_ENDPOINT + ID_PARAM, userId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).updateUser(userId, request);
    }

    @Test
    void testDeleteUser() throws Exception {
        int userId = 1;

        api.perform(delete(USERS_ENDPOINT + ID_PARAM, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }
}
