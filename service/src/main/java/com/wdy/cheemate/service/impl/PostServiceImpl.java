package com.wdy.cheemate.service.impl;

import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestUtil;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.BeanUtil;
import com.wdy.cheemate.common.util.DateUtil;
import com.wdy.cheemate.entity.Post;
import com.wdy.cheemate.entity.output.*;
import com.wdy.cheemate.mapper.PostMapper;
import com.wdy.cheemate.mapper.UserMapper;
import com.wdy.cheemate.service.IPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
@Service
@AllArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    private PostMapper postMapper;
    private UserMapper userMapper;

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean) {
        String conditionString = RequestUtil.getDefaultPageBeanByUserId(pageRequestBean);
        List<Post> list = postMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        Map<String, List<PostOutputVo>> resultMap = new TreeMap<>(Comparator.reverseOrder());
        for (int i = list.size() - 1; i >= 0; i--) {
            Post post = list.get(i);
            String s = DateUtil.formatDate(post.getPublishTime());
            PostOutputVo vo = fillPost(post);
            List<PostOutputVo> postOutputVos;
            if (resultMap.containsKey(s)) {
                postOutputVos = resultMap.get(s);
                postOutputVos.add(vo);
            } else {
                postOutputVos = new ArrayList<>();
                postOutputVos.add(vo);
            }
            resultMap.put(s, postOutputVos);
        }
        Integer pageAllSize = postMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultMap, pageAllSize);
    }

    @Override
    public PostOutputVo fillPost(Post post) {
        PostOutputVo vo = BeanUtil.copy(post, PostOutputVo.class);
        vo.setUser(userMapper.selectById(post.getUserId()));
        return vo;
    }
}
