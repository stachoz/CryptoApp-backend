package com.example.cryptoapp.user.repos;

import com.example.cryptoapp.post.Post;
import com.example.cryptoapp.post.PostRepository;
import com.example.cryptoapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    private User user1;
    private Post post1;
    private Post post2;

    @BeforeEach
    void init() {
        user1 = User.builder()
                .username("john")
                .email("john@wp.pl")
                .password("john123").build();
        post1 = Post.builder()
                .content("lorem ipsum").build();
        post2 = Post.builder()
                .content("lorem ipsum 2").build();
    }

    @Test
    void shouldReturnZeroForNewUser() {
        userRepository.save(user1);
        int posts = userRepository.countUserPosts(user1.getUsername());
        assertThat(posts).isEqualTo(0L);
    }

    @Test
    void shouldReturnOne() {
        userRepository.save(user1);
        post1.setUser(user1);
        postRepository.save(post1);
        int posts = userRepository.countUserPosts(user1.getUsername());
        assertThat(posts).isEqualTo(1L);
    }

    @Test
    void shouldReturnTwo() {
        userRepository.save(user1);
        post1.setUser(user1);
        postRepository.save(post1);
        post2.setUser(user1);
        postRepository.save(post2);
        int posts = userRepository.countUserPosts(user1.getUsername());
        assertThat(posts).isEqualTo(2);
    }
}