package com.example.cryptoapp.post;

import com.example.cryptoapp.post.post_comment.Comment;
import com.example.cryptoapp.post.report.Report;
import com.example.cryptoapp.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 2000)
    @NotEmpty
    private String content;

    @NotNull
    private boolean isVerified = true;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeAdded;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report){
        reports.add(report);
    }
}