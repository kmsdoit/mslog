package com.mslog.mslog.service;

import com.mslog.mslog.domain.Post;
import com.mslog.mslog.repository.PostRepository;
import com.mslog.mslog.request.PostCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor // Lombok 생성자
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreateDto postCreateDto) {
        // repository.save
        Post post = Post.builder()
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .build();

        postRepository.save(post);
    }
}
