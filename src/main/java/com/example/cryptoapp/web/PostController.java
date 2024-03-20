package com.example.cryptoapp.web;


import com.example.cryptoapp.post.PostService;
import com.example.cryptoapp.post.dto.AddPostDto;
import com.example.cryptoapp.post.dto.PostDto;
import com.example.cryptoapp.post.post_comment.CommentDto;
import com.example.cryptoapp.post.post_comment.CommentPostDto;
import com.example.cryptoapp.post.report.ReportDtoAdmin;
import com.example.cryptoapp.post.report.ReportPostDto;
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

    @GetMapping("/list")
    public ResponseEntity<?> getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size){
        List<PostDto> posts = postService.getPosts(PageRequest.of(page, size));
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
        PostDto post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addPost(@Valid @RequestBody AddPostDto addPostDto){
        PostDto postDto = postService.addPost(addPostDto);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> commentPost(@Valid @RequestBody CommentPostDto dto, @PathVariable Long postId){
        postService.commentPost(dto, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comment/list")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId, @RequestParam(name = "size", defaultValue = "10") int size,
                                             @RequestParam(name = "page", defaultValue = "0") int page){
        List<CommentDto> postComments = postService.getPostComments(PageRequest.of(page, size), postId);
        return new ResponseEntity<>(postComments, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<?> removePostComment(@PathVariable Long postId, @PathVariable Long commentId){
        postService.deletePostComment(postId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/report")
    public ResponseEntity<?> reportPost(@Valid @RequestBody ReportPostDto reportPostDto, @PathVariable Long postId){
        postService.reportPost(reportPostDto, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/verify")
    public ResponseEntity<?> verifyPost(@PathVariable Long postId){
        postService.verifyPost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/report/list")
    public ResponseEntity<?> getPostReports(@PathVariable Long postId, @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "page", defaultValue = "0") int page){
        List<ReportDtoAdmin> reports = postService.getPostReports(postId, PageRequest.of(page, size));
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/report/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable Long postId, @PathVariable Long reportId){
        postService.deletePostReportById(reportId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
