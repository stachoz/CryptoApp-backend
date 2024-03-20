package com.example.cryptoapp.post.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDtoAdmin {
    private Long id;
    private String cause;
    private String postTitle;
    private String authorUsername;
    private Long postId;
}
