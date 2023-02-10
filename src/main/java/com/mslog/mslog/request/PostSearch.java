package com.mslog.mslog.request;

import lombok.*;

import static java.lang.Math.*;

@Getter
@Setter
public class PostSearch {

    public static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Builder
    public PostSearch(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public long getOffset() {
        return (long) (max(1,page) -1 ) * min(size, MAX_SIZE);
    }
}
