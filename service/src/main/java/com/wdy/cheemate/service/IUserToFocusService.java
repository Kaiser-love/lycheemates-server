package com.wdy.cheemate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.dto.PageListAndConditions;
import com.wdy.cheemate.entity.UserToFocus;
import com.wdy.cheemate.entity.output.UserToFocusOutputVo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-11 19:39
 */
public interface IUserToFocusService extends IService<UserToFocus> {
    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type);

    ResponseEntity<ResultBean> putOrRemoveFocus(RequestBean requestBean, Integer type);

    PageListAndConditions getUserFocusOrFans(PageRequestBean pageRequestBean, Integer type);

    UserToFocusOutputVo fillUserToFocus(UserToFocus userToFocus);

}