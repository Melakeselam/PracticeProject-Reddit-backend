package com.example.redditclone.service;

import com.example.redditclone.dto.PostRequest;
import com.example.redditclone.exception.SubredditNotFoundException;
import com.example.redditclone.mapper.PostMapper;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

@Transactional
@Slf4j
@AllArgsConstructor
@Service
public class PostService {
    
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    private final PostMapper postMapper;

    public Post save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()-> new SubredditNotFoundException("Subreddit by this name : "+ postRequest.getSubredditName() + " does not exist."));

        User currentUser = authService.getCurrentUser();
        return postRepository.save(postMapper.mapFromDto(postRequest,subreddit,currentUser));
    }
}
