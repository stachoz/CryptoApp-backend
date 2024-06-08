package com.example.cryptoapp.post.report;

import com.example.cryptoapp.post.Post;
import com.example.cryptoapp.post.post_comment.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Length(min = 2, max = 200)
    private String cause;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Comment comment;
}
