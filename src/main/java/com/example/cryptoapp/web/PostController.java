package com.example.cryptoapp.web;


import com.example.cryptoapp.post.PostService;
import com.example.cryptoapp.post.dto.AddPostDto;
import com.example.cryptoapp.post.dto.PagedResponse;
import com.example.cryptoapp.post.dto.PostDto;
import com.example.cryptoapp.post.post_comment.CommentDto;
import com.example.cryptoapp.post.post_comment.CommentPostDto;
import com.example.cryptoapp.post.report.CommentReportDto;
import com.example.cryptoapp.post.report.PostReportDto;
import com.example.cryptoapp.post.report.ReportDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("")
    public ResponseEntity<?> addPost(@Valid @RequestBody AddPostDto addPostDto){
        PostDto postDto = postService.addPost(addPostDto);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size){
        PagedResponse<PostDto> posts = postService.getPosts(PageRequest.of(page, size));
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
        PostDto post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/report/list")
    public ResponseEntity<?> getPostReports(){
        List<PostReportDto> postReports = postService.getPostReports();
        return new ResponseEntity<>(postReports, HttpStatus.OK);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> commentPost(@Valid @RequestBody CommentPostDto dto, @PathVariable Long postId){
        postService.commentPost(dto, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comment/list")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId, @RequestParam(name = "size", defaultValue = "10") int size,
                                             @RequestParam(name = "page", defaultValue = "0") int page){
        PagedResponse<CommentDto> postComments = postService.getPostComments(PageRequest.of(page, size), postId);
        return new ResponseEntity<>(postComments, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<?> removePostComment(@PathVariable Long postId, @PathVariable Long commentId){
        postService.deletePostComment(postId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/report")
    public ResponseEntity<?> reportPost(@Valid @RequestBody ReportDto reportDto, @PathVariable Long postId){
        postService.reportPost(reportDto, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/verify")
    public ResponseEntity<?> verifyPost(@PathVariable Long postId){
        postService.verifyPost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/report/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable Long postId, @PathVariable Long reportId){
        postService.deletePostReportById(reportId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comment/{commentId}/report")
    public ResponseEntity<?> reportComment(@Valid @RequestBody ReportDto reportDto, @PathVariable Long commentId){
        postService.reportComment(commentId, reportDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/comment/report/list")
    public ResponseEntity<List<CommentReportDto>> getCommentReports(){
        List<CommentReportDto> commentReports = postService.getCommentReports();
        return new ResponseEntity<>(commentReports, HttpStatus.OK);
    }

}
