package com.example.cryptoapp.post;

import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.post.dto.*;
import com.example.cryptoapp.post.post_comment.*;
import com.example.cryptoapp.post.report.*;
import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final UserService userService;
    private final ReportDtoMapper reportDtoMapper;
    private final CommentDtoMapper commentDtoMapper;
    private final PostDtoMapper postDtoMapper;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, ReportRepository reportRepository, UserService userService, ReportDtoMapper reportDtoMapper, CommentDtoMapper commentDtoMapper, PostDtoMapper postDtoMapper){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.reportDtoMapper = reportDtoMapper;
        this.commentDtoMapper = commentDtoMapper;
        this.postDtoMapper = postDtoMapper;
    }

    public PagedResponse<PostDto> getPosts(PageRequest pr){
        PageRequest sortedPr = pr.withSort(Sort.by("timeAdded").descending());
        Page<Post> all = postRepository.findAllByIsVerified(true,sortedPr);
        int pageNumber = pr.getPageNumber();
        isPageOutOfBounds(pageNumber, all.getTotalPages());
        List<PostDto> dtos = all.stream()
                .map(post -> postDtoMapper.map(post))
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
        User currentUser = userService.getCurrentUser();
        Post post = PostFormDtoMapper.map(addPostDto);
        post.setUser(currentUser);
        if(currentUser.isPostVerification()) post.setVerified(false);
        Post savedPost = postRepository.save(post);
        return postDtoMapper.map(savedPost);
    }

    public PostDto getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(p -> postDtoMapper.map(p))
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
        User currentUser = userService.getCurrentUser();
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

    public PagedResponse<CommentDto> getPostComments(PageRequest pr, Long postId){
        if(!postRepository.existsByIdAndIsVerified(postId, true)) throw new NoSuchElementException("post with id (" + postId + ") not found");
        PageRequest sortedPr = pr.withSort(Sort.by("timeAdded").descending());
        Page<Comment> all = commentRepository.findAllByPost_Id(postId, sortedPr);
        int pageNumber = pr.getPageNumber();
        isPageOutOfBounds(pageNumber, all.getTotalPages());
        List<CommentDto> dtos = all.stream()
                .map(comment -> commentDtoMapper.map(comment))
                .collect(Collectors.toList());
        return new PagedResponse<>(
                all.getTotalPages(),
                all.getTotalElements(),
                all.getNumber(),
                all.getSize(),
                dtos
        );
    }


    public void reportPost(ReportDto reportFormDto, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("post with id (" + postId + ") not found"));
        Report report = reportDtoMapper.mapToPostReportDto(reportFormDto);
        post.addReport(report);
        postRepository.save(post);
    }

    public List<PostReportDto> getPostReports(){
        return reportRepository.getAllPostReports()
                .stream()
                .map(report -> reportDtoMapper.mapToPostReportDto(report))
                .collect(Collectors.toList());
    }

    public List<CommentReportDto> getCommentReports(){
        return reportRepository.getAllCommentReports()
                .stream()
                .map(report -> reportDtoMapper.mapToCommentReportDto(report))
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

    public void reportComment(Long commentId, ReportDto reportDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("comment with id (" + commentId + ") not found"));
        Report report = reportDtoMapper.mapToPostReportDto(reportDto);
        comment.addReport(report);
        commentRepository.save(comment);
    }

    private void isPageOutOfBounds(int pageNumber, int totalPages){
        if(pageNumber != 0 && pageNumber + 1 > totalPages) throw new NoSuchElementException("page " + pageNumber + " is out of bounds");
    }
}
