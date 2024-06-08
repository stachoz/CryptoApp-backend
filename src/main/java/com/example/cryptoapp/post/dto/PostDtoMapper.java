package com.example.cryptoapp.post.dto;

import com.example.cryptoapp.post.Post;
import org.springframework.stereotype.Service;

@Service
public class PostDtoMapper {
    public PostDto map(Post post){
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setTimeAdded(post.getTimeAdded());
        dto.setAuthor(post.getUser().getUsername());
        dto.setVerified(post.isVerified());
        return dto;
    }
}
