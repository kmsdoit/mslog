package com.mslog.mslog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mslog.mslog.domain.Post;
import com.mslog.mslog.repository.PostRepository;
import com.mslog.mslog.request.PostCreateDto;
import com.mslog.mslog.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

//    @Test
//    @DisplayName("/posts 요청시 Hello World를 출력한다")
//    void getTest() throws Exception {
//        //expects
//        mockMvc.perform(get("/posts"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello World"))
//                .andDo(print());
//    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void postSuccessTest() throws Exception {
        //given
        PostCreateDto request = PostCreateDto.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request); // String -> Json 자료 처리 하는 법

        System.out.println(json);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

//    @Test
//    @DisplayName("/posts 요청시 title 값은 필수다.")
//    void postTest() throws Exception {
//
//        //given
//        PostCreateDto request = PostCreateDto.builder()
//                .content("내용입니다")
//                .build();
//
//        String json = objectMapper.writeValueAsString(request); // String -> Json 자료 처리 하는 법
//        //expects
//        mockMvc.perform(post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
//                .andDo(print());
//    }

    @Test
    @DisplayName("/posts 요청시 DB에 값을 저장한다")
    void postSaveDB() throws Exception {

        //given
        PostCreateDto request = PostCreateDto.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request); // String -> Json 자료 처리 하는 법

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Assertions.assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void postOneById() throws Exception {
        //given
        Post post = Post.builder()
                .title("12345")
                .content("bar")
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("12345"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getPostManyById() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i ->
                        Post.builder()
                                .title("foo"+ i)
                                .content("bar"+ i)
                                .build()).collect(Collectors.toList());

        //when
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andExpect(jsonPath("$[0].content").value("bar19"))

                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void updatePostByPostId() throws Exception {
        //given
        Post post = Post.builder()
                .title("mslog_title")
                .content("mslog_content")
                .build();

        postRepository.save(post);
        //when

        PostEdit postEdit = PostEdit.builder()
                .title("mslog_title_2")
                .content("mslog_content")
                .build();
        //expected

        mockMvc.perform(patch("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())

                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePostById() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("mslog_title1")
                .content("mslog_content1")
                .build();

        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("mslog_title2")
                .content("mslog_content2")
                .build();

        postRepository.save(post2);

        mockMvc.perform(delete("/posts/{postId}",post1.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void NotFoundPostGetPost() throws Exception{

        mockMvc.perform(delete("/posts/{postId}", 1L)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void NotFoundPostUpdatePost() throws Exception{
        //when

        PostEdit postEdit = PostEdit.builder()
                .title("mslog_title_2")
                .content("mslog_content")
                .build();

        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에는 '바보'는 포함할 수 없다")
    void postSaveDBThrowInvalidRequest() throws Exception {

        //given
        PostCreateDto request = PostCreateDto.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request); // String -> Json 자료 처리 하는 법

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
//        Assertions.assertEquals(1L,postRepository.count());
//
//        Post post = postRepository.findAll().get(0);
//        assertEquals("제목입니다.",post.getTitle());
//        assertEquals("내용입니다.",post.getContent());
    }
}