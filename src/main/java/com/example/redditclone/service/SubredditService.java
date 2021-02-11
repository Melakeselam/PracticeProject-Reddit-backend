package com.example.redditclone.service;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        
        Subreddit saved = subredditRepository.save(mapSubredditFromDto(subredditDto));
        subredditDto.setId(saved.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditFromDto(SubredditDto subredditDto) {

        Subreddit subreddit = new Subreddit();
        return subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream().map(this::mapSubredditToDto)
                .collect(Collectors.toList());

    }

    private SubredditDto mapSubredditToDto(Subreddit subreddit) {

        return new SubredditDto().builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .build();
    }
}
