package com.example.cryptoapp.post.report;

import com.example.cryptoapp.post.Post;

public class ReportDtoAdminMapper {
    public static ReportDtoAdmin map(Report report){
        ReportDtoAdmin dto = new ReportDtoAdmin();
        Post reportedPost = report.getPost();
        dto.setId(report.getId());
        dto.setCause(report.getCause());
        dto.setAuthorUsername(reportedPost.getUser().getUsername());
        dto.setPostId(reportedPost.getId());
        return dto;
    }
}
