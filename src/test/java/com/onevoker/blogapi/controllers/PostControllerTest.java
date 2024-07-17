package com.onevoker.blogapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onevoker.blogapi.domain.services.interfaces.PostService;
import com.onevoker.blogapi.dto.requests.PostRequest;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc api;

    private ObjectMapper objectMapper;

    private static final String POSTS_ENDPOINT = "/posts";
    private static final String USERS_WITH_ID_ENDPOINT = "/users/{userId}";
    private static final String ID_PARAM = "/{id}";
    private static final String BETWEEN_DATES_PARAM = "/between-dates";
    private static final String START_DATE_PARAM = "startDate";
    private static final String END_DATE_PARAM = "endDate";

    @BeforeEach
    void setUp() {
        api = MockMvcBuilders.standaloneSetup(postController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testGetAllPosts() throws Exception {
        api.perform(get(POSTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService).getAllPosts();
    }

    @Test
    void testGetAllPostsBetweenDates() throws Exception {
        String startDate = String.valueOf(OffsetDateTime.now());
        String endDate = String.valueOf(OffsetDateTime.now().plusDays(1));

        api.perform(get(POSTS_ENDPOINT + BETWEEN_DATES_PARAM)
                        .param(START_DATE_PARAM, startDate)
                        .param(END_DATE_PARAM, endDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService).getAllPostsBetweenDates(any(OffsetDateTime.class), any(OffsetDateTime.class));
    }

    @Test
    void testGetPostById() throws Exception {
        int postId = 1;

        api.perform(get(POSTS_ENDPOINT + ID_PARAM, postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService).getPostById(postId);
    }

    @Test
    void testCreatePost() throws Exception {
        int userId = 1;
        PostRequest postRequest = new PostRequest("S", "M");

        api.perform(post(POSTS_ENDPOINT + USERS_WITH_ID_ENDPOINT, userId)
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService).createPost(userId, postRequest);
    }

    @Test
    void testCreatePostWithUncorrectData() throws Exception {
        int userId = 1;
        PostRequest badPostRequest = new PostRequest("", "");

        api.perform(post(POSTS_ENDPOINT + USERS_WITH_ID_ENDPOINT, userId)
                        .content(objectMapper.writeValueAsString(badPostRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(postService, never()).createPost(userId, badPostRequest);
    }

    @Test
    void testUpdatePost() throws Exception {
        int postId = 1;
        PostRequest postRequest = new PostRequest("Title", "Content");

        api.perform(put(POSTS_ENDPOINT + ID_PARAM, postId)
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService).updatePost(postId, postRequest);
    }

    @Test
    void testDeletePost() throws Exception {
        int postId = 1;

        api.perform(delete(POSTS_ENDPOINT + ID_PARAM, postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(postService).deletePost(postId);
    }
}
