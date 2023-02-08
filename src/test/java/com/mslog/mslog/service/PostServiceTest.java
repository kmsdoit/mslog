package com.mslog.mslog.service;

import com.mslog.mslog.domain.Post;
import com.mslog.mslog.repository.PostRepository;
import com.mslog.mslog.request.PostCreateDto;
import com.mslog.mslog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        postService.write(postCreateDto);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("123456")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("123456",post.getTitle());
        assertEquals("bar",post.getContent());
    }

}