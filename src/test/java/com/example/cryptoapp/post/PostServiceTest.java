package com.example.cryptoapp.post;

import com.example.cryptoapp.post.dto.PostDto;
import com.example.cryptoapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostService postService;
    private Post post1;
    private User user;

    @BeforeEach
    public void init(){
        user = User.builder().username("test").build();
        post1 = Post.builder()
                .id(1L)
                .content("lorem ipsum")
                .timeAdded(new Date())
                .isVerified(true)
                .user(user)
                .build();
    }

    @Test
    public void shouldReturnListOfPosts(){
        List<Post> posts = List.of(post1, post1);
        Page<Post> mockPage = new PageImpl<>(posts, PageRequest.of(1, 10), 1);
        when(postRepository.findAllByIsVerified(eq(true), any())).thenReturn(mockPage);
        List<PostDto> allUsers = postService.getPosts(PageRequest.of(1, 10));
        assertThat(allUsers.size()).isEqualTo(2);
    }

    @Test
    public void shouldThrowExceptionWhenPageOutOfBounds(){
        List<Post> posts = List.of(post1, post1, post1, post1, post1, post1);
        Page<Post> mockPage = new PageImpl<>(posts, PageRequest.of(0, 3), 2);
        when(postRepository.findAllByIsVerified(eq(true),any())).thenReturn(mockPage);
        assertThrows(NoSuchElementException.class, () -> postService.getPosts(PageRequest.of(3, 3)));
    }

    @Test
    public void shouldReturnEmptyList_WhenThereIsNoUser(){
        PageImpl<Post> mockPage = new PageImpl<>(List.of());
        when(postRepository.findAllByIsVerified(eq(true), any())).thenReturn(mockPage);
        List<PostDto> users = postService.getPosts(PageRequest.of(0, 3));
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void shouldThrowException_PageOutOfBounds_WhenThereIsNoUserAndRequestNotFirstPage(){
        PageImpl<Post> mockPage = new PageImpl<>(List.of(), PageRequest.of(0, 3), 0);
        when(postRepository.findAllByIsVerified(eq(true),any())).thenReturn(mockPage);
        assertThrows(NoSuchElementException.class, () -> postService.getPosts(PageRequest.of(1, 3)));
    }
}