package com.mslog.mslog.controller;

import com.mslog.mslog.domain.Post;
import com.mslog.mslog.domain.PostEditor;
import com.mslog.mslog.exception.InvalidRequest;
import com.mslog.mslog.request.PostCreateDto;
import com.mslog.mslog.request.PostEdit;
import com.mslog.mslog.request.PostSearch;
import com.mslog.mslog.response.PostResponse;
import com.mslog.mslog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController // 데이터 기반으로 가용할 떄는 RestController 사용
@RequiredArgsConstructor
public class PostController {
        // SSR -> jsp, thymeleaf, mustache ...
        // SPA ->
        // vue, nuxt
        // react, next

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT

    // 데이터를 검증하는 이유

    // 1. client 개발자가 깜빡할 수도 있다. 실수로 값을 안보낼 수도 있다
    // 2. client Bug로 값이 누락될 수도 있다
    // 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
    // 4. DB에 값을 저장할 떄 의도치 않는 오류가 발생할 수 있다.

    private final PostService postService;

    // 글이 너무 많은 경우 -> 비용이 많이 든다
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    // 글 등록
    // POST Method
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreateDto request){
        // POST -> 200, 201
        request.validate();
        postService.write(request);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId}
     * */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        // 서비스 정책에 맞는 응답 클래스 분리
        return postService.get(id);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        return postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
