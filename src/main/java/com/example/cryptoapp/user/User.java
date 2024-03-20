package com.example.cryptoapp.user;

import com.example.cryptoapp.post.Post;
import com.example.cryptoapp.post.post_comment.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 5, max = 80)
    @Email
    private String email;

    @NotNull
    @Size(min = 4, max = 200)
    private String password;

    @NotNull
    private boolean postVerification = false;

    @NotNull
    private boolean isLocked = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.REMOVE)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new HashSet<>();
}
