package com.example.cryptoapp.post.report;


import com.example.cryptoapp.post.dto.PostDtoMapper;
import com.example.cryptoapp.post.post_comment.CommentDtoMapper;
import org.springframework.stereotype.Service;

@Service
public class ReportDtoMapper {
    private final PostDtoMapper postDtoMapper;
    private final CommentDtoMapper commentDtoMapper;

    public ReportDtoMapper(PostDtoMapper postDtoMapper, CommentDtoMapper commentDtoMapper) {
        this.postDtoMapper = postDtoMapper;
        this.commentDtoMapper = commentDtoMapper;
    }

    public PostReportDto mapToPostReportDto(Report report){
        return new PostReportDto(
                report.getId(),
                report.getCause(),
                postDtoMapper.map(report.getPost())
        );
    }

    public CommentReportDto mapToCommentReportDto(Report report){
        return new CommentReportDto(
                report.getId(),
                report.getCause(),
                commentDtoMapper.map(report.getComment())
        );
    }

    public Report mapToPostReportDto(ReportDto reportDto) {
        Report report = new Report();
        report.setCause(reportDto.getCause());
        return report;
    }

}
