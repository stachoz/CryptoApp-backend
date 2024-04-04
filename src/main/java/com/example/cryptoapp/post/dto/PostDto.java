package com.example.cryptoapp.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private Date timeAdded;
    private String author;
    private boolean isVerified;
    private int comments;
}
