package com.example.redditclone.controller;

import com.example.redditclone.dto.PostRequest;
import com.example.redditclone.dto.PostResponse;
import com.example.redditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

//    public PostResponse getPost(Long id){
//        return PostService.getPostById(id);
//    }
//
//    public List<PostResponse> getAllPosts(){
//        return PostService.getAllPosts();
//    }
//
//    public List<PostResponse> getPostsBySubreddit(Long id){
//        return PostService.getPostsBySubreddit(id);
//    }
//
//    public List<PostResponse> getPostsByUsername(String username){
//        return PostService.getPostsByUsername(username);
//    }
}
