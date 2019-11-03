package com.wdy.cheemate.controller;


import com.wdy.cheemate.annotation.BeforeLog;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestBean;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.entity.Post;
import com.wdy.cheemate.entity.input.PostInputVo;
import com.wdy.cheemate.mapper.PostMapper;
import com.wdy.cheemate.service.IPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
@RestController
@AllArgsConstructor
@Api(tags = "用户动态接口", description = "获取用户动态")
public class PostController {
    private IPostService postService;
    private PostMapper postMapper;

    @PostMapping("/posts")
    @ApiOperation(value = "分页获取当前用户动态")
    @BeforeLog
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        return postService.listByPage(pageRequestBean);
    }

    @PostMapping("/post")
    @ApiOperation(value = "发表或修改动态")
    public ResponseEntity<ResultBean> insert(@RequestBody PostInputVo postInputVo) {
        Post post = Post.builder().id(postInputVo.getId()).content(postInputVo.getContent()).learningTime(postInputVo.getLearningTime())
                .userId(SecureUtil.getDataBaseUserId()).publishTime(TimeUtil.currentTimeStamp()).build();
        post.insertOrUpdate();
        return ResponseHelper.OK(post);
    }

    @DeleteMapping("/post")
    @ApiOperation(value = "删除动态")
    public ResponseEntity<ResultBean> delete(@RequestBody RequestBean requestBean) {
        requestBean.getItems().forEach(requestItem -> postMapper.deleteByMap(SqlUtil.map(requestItem.getQuery(), requestItem.getQueryString()).build()));
        return ResponseHelper.OK();
    }
}

