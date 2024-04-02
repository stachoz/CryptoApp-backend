package com.example.cryptoapp.web;

import com.example.cryptoapp.exception.GlobalExceptionHandler;
import com.example.cryptoapp.post.PostService;
import com.example.cryptoapp.post.dto.AddPostDto;
import com.example.cryptoapp.post.post_comment.CommentPostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;
    private AddPostDto addPostDto;

    @BeforeEach
    public void init() {
        addPostDto = new AddPostDto( "lorem ipsum");
    }

    @Test
    public void shouldReturnOkOn_EmptyPosts() throws Exception {
        String url = "/post/list";
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOk_getSpecificPost() throws Exception {
        String url = "/post/1";
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandle_PostNotFound() throws Exception {
        String url = "/post/1000";
        willThrow(new NoSuchElementException()).given(postService).getPostById(any());
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatusCreatedAfterAddingPost() throws Exception {
        String url = "/post";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addPostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldHandleInvalidRequestBody_BlankFields() throws Exception {
        String url = "/post";
        addPostDto.setContent("");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addPostDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleInvalidRequestBody_NullRequestBody() throws Exception {
        String url = "/post";
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleInvalidRequestBody_NullFields() throws Exception {
        String url = "/post";
        addPostDto.setContent(null);
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkAfterSuccessfulDeleting() throws Exception {
        String url = "/post/1";
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandleDeletingNotExistingPost() throws Exception {
        String url = "/post/1000";
        willThrow(new NoSuchElementException()).given(postService).deletePost(any());
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnCreatedAfterSuccessfulComment() throws Exception {
        String url = "/post/1/comment";
        CommentPostDto commentPostDto = new CommentPostDto("lorem ipsum");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldHandleCommentingNotExistingPost() throws Exception {
        String url = "/post/10000/comment";
        CommentPostDto commentPostDto = new CommentPostDto("lorem ipsum");
        willThrow(new NoSuchElementException()).given(postService).commentPost(any(),any());
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPostDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldHandleInvalidComment_BlankComment() throws Exception {
        String url = "/post/1/comment";
        CommentPostDto commentPostDto = new CommentPostDto("");
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPostDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleInvalidComment_NullComment() throws Exception {
        String url = "/post/1/comment";
        CommentPostDto commentPostDto = new CommentPostDto(null);
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPostDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetComments() throws Exception {
        String url = "/post/1/comment/list";
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandleExceptionWithNoComments() throws Exception {
        String url = "/post/1/comment/list";
        willThrow(new NoSuchElementException()).given(postService).getPostComments(any(),any());
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
