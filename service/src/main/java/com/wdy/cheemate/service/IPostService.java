package com.wdy.cheemate.service;

import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wdy.cheemate.entity.output.PostOutputVo;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
public interface IPostService extends IService<Post> {
    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean);


    PostOutputVo fillPost(Post post);
}
