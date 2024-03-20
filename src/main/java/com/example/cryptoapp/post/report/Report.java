package com.example.cryptoapp.post.report;

import com.example.cryptoapp.post.Post;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 300)
    @Nullable
    private String cause;
    @ManyToOne()
    @JoinColumn(
            name = "post_id",
            referencedColumnName = "id"
    )
    private Post post;
}
