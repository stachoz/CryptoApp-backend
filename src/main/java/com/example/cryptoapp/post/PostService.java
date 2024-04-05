package com.example.cryptoapp.post;

import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.post.dto.*;
import com.example.cryptoapp.post.post_comment.*;
import com.example.cryptoapp.post.report.*;
import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.repos.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       CommentRepository commentRepository, ReportRepository reportRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
    }

    public PagedResponse<PostDto> getPosts(PageRequest pr){
        PageRequest sortedPr = pr.withSort(Sort.by("timeAdded").descending());
        Page<Post> all = postRepository.findAllByIsVerified(true,sortedPr);
        int pageNumber = pr.getPageNumber();
        isPageOutOfBounds(pageNumber, all.getTotalPages());
        List<PostDto> dtos = all.stream()
                .map(PostDtoMapper::map)
                .collect(Collectors.toList());
        return new PagedResponse<>(
                all.getTotalPages(),
                all.getTotalElements(),
                all.getNumber(),
                all.getSize(),
                dtos
        );
    }

    public PostDto addPost(AddPostDto addPostDto){
        User currentUser = getCurrentUser();
        Post post = PostFormDtoMapper.map(addPostDto);
        post.setUser(currentUser);
        if(currentUser.isPostVerification()) post.setVerified(false);
        Post savedPost = postRepository.save(post);
        return PostDtoMapper.map(savedPost);
    }

    public PostDto getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(PostDtoMapper::map)
                .orElseThrow(() -> new NoSuchElementException("post with id (" + id + ") not found"));
    }

    public void deletePost(Long postId){
        try {
            postRepository.deleteById(postId);
        } catch (EmptyResultDataAccessException e){
            throw new NoSuchElementException("post with id (" + postId + ") does not exists");
        }
    }

    public void commentPost(CommentPostDto dto, Long postId){
        Post post = postRepository.findVerifiedPostById(postId).orElseThrow(() -> new NoSuchElementException("post with id (" + postId + ") not found"));
        User currentUser = getCurrentUser();
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(currentUser);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void deletePostComment(Long postId, Long commentId){
        Post post = postRepository.findVerifiedPostById(postId).orElseThrow(() -> new NoSuchElementException("post with id (" + postId + ") not found"));
        if(!post.getComments().removeIf(comment -> comment.getId().equals(commentId))){
            throw new NoSuchElementException("comment with id (" + commentId + ") not found");
        }
    }

    public List<CommentDto> getPostComments(PageRequest pr, Long postId){
        if(!postRepository.existsByIdAndIsVerified(postId, true)) throw new NoSuchElementException("post with id (" + postId + ") not found");
        PageRequest sortedPr = pr.withSort(Sort.by("timeAdded").descending());
        Page<Comment> all = commentRepository.findAllByPost_Id(postId, sortedPr);
        int pageNumber = pr.getPageNumber();
        isPageOutOfBounds(pageNumber, all.getTotalPages());
        return all.stream()
                .map(CommentDtoMapper::map)
                .collect(Collectors.toList());
    }


    public void reportPost(ReportPostDto reportFormDto, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("post with id (" + postId + ") not found"));
        Report report = ReportPostDtoMapper.map(reportFormDto);
        report.setPost(post);
        reportRepository.save(report);
    }

    public List<ReportDtoAdmin> getPostReports(Long postId, PageRequest pageRequest){
        if(!postRepository.existsById(postId)) throw  new NoSuchElementException("post with id (" + postId + ") not found");
        Page<Report> pageOfReports = reportRepository.findAllByPost_Id(pageRequest, postId);
        int pageNumber = pageRequest.getPageNumber();
//        if(pageNumber != 0 && pageNumber + 1 > pageOfReports.getTotalPages()) throw new NoSuchElementException("page " + pageNumber + " is out of bounds");
        isPageOutOfBounds(pageNumber, pageOfReports.getTotalPages());
        return pageOfReports.stream()
                .map(ReportDtoAdminMapper::map)
                .collect(Collectors.toList());
    }

    public void deletePostReportById(Long reportId, Long postId){
        if(!postRepository.existsById(postId)) throw  new NoSuchElementException("post with id (" + postId + ") not found");
        try {
            reportRepository.deleteById(reportId);
        } catch (EmptyResultDataAccessException exception){
            throw new NoSuchElementException("post report with id (" + reportId + ") not found");
        }
    }

    public void verifyPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("post with id (" + postId + ") not found"));
        if(post.isVerified()) throw new OperationConflictException("post is already verified");
        post.setVerified(true);
        postRepository.save(post);
    }

    private User getCurrentUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(name).orElseThrow(NoSuchElementException::new);
    }

    private void isPageOutOfBounds(int pageNumber, int totalPages){
        if(pageNumber != 0 && pageNumber + 1 > totalPages) throw new NoSuchElementException("page " + pageNumber + " is out of bounds");
    }

}
