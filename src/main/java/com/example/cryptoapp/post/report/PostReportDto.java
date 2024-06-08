package com.example.cryptoapp.post.report;

import com.example.cryptoapp.post.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostReportDto {
    private Long reportId;
    private String cause;
    @JsonProperty("post")
    private PostDto post;
}
