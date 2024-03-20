package com.example.cryptoapp.post.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
    Page<Report> findAllByPost_Id(Pageable pageable, Long postId);
}
