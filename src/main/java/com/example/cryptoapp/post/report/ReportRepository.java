package com.example.cryptoapp.post.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(nativeQuery = true, value = "select r.* from post p join report r on p.id = r.post_id")
    List<Report> getAllPostReports();

    @Query(nativeQuery = true, value = "select r.* from comment com join report r on com.id = r.comment_id")
    List<Report> getAllCommentReports();
}
