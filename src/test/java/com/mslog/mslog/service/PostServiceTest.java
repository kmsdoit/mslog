package com.mslog.mslog.service;

import com.mslog.mslog.domain.Post;
import com.mslog.mslog.exception.PostNotFound;
import com.mslog.mslog.repository.PostRepository;
import com.mslog.mslog.request.PostCreateDto;
import com.mslog.mslog.request.PostEdit;
import com.mslog.mslog.request.PostSearch;
import com.mslog.mslog.response.PostResponse;
import com.sun.nio.sctp.IllegalReceiveException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

    @Test
    @DisplayName("글 1페이지 조회")
    void test4() {

        //given
        List<Post> requestPosts = IntStream.range(0,20)
                        .mapToObj(i ->
                            Post.builder()
                                    .title("foo"+ i)
                                    .content("bar"+ i)
                                    .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);
        //when

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();


        List<PostResponse> posts = postService.getList(postSearch);
        //then

        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test5() {

        //given
        Post post = Post.builder()
                .title("mslog_title")
                .content("mslog_content")
                .build();

        postRepository.save(post);
        //when

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("mslog_content")
                .build();

        postService.edit(post.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("mslog_title", changePost.getTitle());
        assertEquals("mslog_content", changePost.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        //given
        Post post = Post.builder()
                .title("mslog_title")
                .content("mslog_content")
                .build();

        postRepository.save(post);
        //when

        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회 실패 케이스")
    void test7() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);

        //then
        assertThrows(PostNotFound.class, () ->{
            postService.get(post.getId() + 1L);
        });

    }

    @Test
    @DisplayName("게시글 삭제 실패 케이스")
    void test8() {
        //given
        Post post = Post.builder()
                .title("mslog_title")
                .content("mslog_content")
                .build();

        postRepository.save(post);
        //when

        //then
        assertThrows(PostNotFound.class, () ->{
            postService.delete(post.getId() + 1L);
        });

    }


    @Test
    @DisplayName("글 제목 수정 실패 케이스")
    void test9() {

        //given
        Post post = Post.builder()
                .title("mslog_title")
                .content("mslog_content")
                .build();

        postRepository.save(post);
        //when

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("mslog_content")
                .build();

        //then
        assertThrows(PostNotFound.class, () ->{
            postService.edit(post.getId() + 1, postEdit);
        });

    }
}