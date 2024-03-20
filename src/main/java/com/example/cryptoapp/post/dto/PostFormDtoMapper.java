package com.example.cryptoapp.post.dto;

import com.example.cryptoapp.post.Post;

public class PostFormDtoMapper {
    public static Post map(AddPostDto addPostDto){
        Post post = new Post();
        post.setTitle(addPostDto.getTitle());
        post.setContent(addPostDto.getContent());
        return post;
    }
}
