package com.mslog.mslog.request;

import com.mslog.mslog.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Setter
@Getter
public class PostCreateDto {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    /**
     * 빌더의 장점
     * 1. 가독성이 좋다
     * 2. 값 생성의 대한 유연함
     * 3. 필요한 값만 받을 수 있다. -> (오버로딩 가능한 조건 찾아보기)
     * 4. 객체의 불변성 -> 가장 중요한 이유
     * */
    @Builder
    public PostCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보")){
            throw new InvalidRequest("title", "제목에 바보를 입력할 수 없습니다");
        }
    }


}
