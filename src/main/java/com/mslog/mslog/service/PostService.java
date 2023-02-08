package com.mslog.mslog.service;

import com.mslog.mslog.domain.Post;
import com.mslog.mslog.repository.PostRepository;
import com.mslog.mslog.request.PostCreateDto;
import com.mslog.mslog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        /**
         * postController -> WebService -> Repository
         *                  PostService
         * */

        return response;
    }
}
