package com.example.cryptoapp.post.report;

import com.example.cryptoapp.post.post_comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportDto {
    private Long id;
    private String cause;
    private CommentDto comment;
}
