package com.example.cryptoapp.post.report;

public class ReportPostDtoMapper {
    public static Report map(ReportPostDto dto){
        Report report = new Report();
        report.setCause(dto.getCause());
        return report;
    }
}
