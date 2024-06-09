package com.example.cryptoapp.post.post_comment;

import org.springframework.stereotype.Service;

@Service
public class CommentDtoMapper {
    public CommentDto map(Comment comment){
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setContent(comment.getContent());
        dto.setTimeAdded(comment.getTimeAdded());
        dto.setAuthorUsername(comment.getUser().getUsername());
        return dto;
    }
}
