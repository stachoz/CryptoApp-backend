package com.example.cryptoapp.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsVerified(boolean is_verified, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    @Query(nativeQuery = true, value = "select * from post where id = :postId and is_verified = true")
    Optional<Post> findVerifiedPostById(@Param("postId") Long postId);

    boolean existsByIdAndIsVerified(Long postId, boolean isVerified);
}
