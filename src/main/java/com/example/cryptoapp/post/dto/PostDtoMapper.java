package com.example.cryptoapp.post.dto;

import com.example.cryptoapp.post.Post;

public class PostDtoMapper {
    public static PostDto map(Post post){
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setTimeAdded(post.getTimeAdded());
        dto.setAuthor(post.getUser().getUsername());
        dto.setVerified(post.isVerified());
        return dto;
    }
}
