package com.example.cryptoapp.post.post_comment;

public class CommentDtoMapper {
    public static CommentDto map(Comment comment){
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setTimeAdded(comment.getTimeAdded());
        dto.setAuthorUsername(comment.getUser().getUsername());
        return dto;
    }
}
