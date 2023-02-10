package com.mslog.mslog.repository;

import com.mslog.mslog.domain.Post;
import com.mslog.mslog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
