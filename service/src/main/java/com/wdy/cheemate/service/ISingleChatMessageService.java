package com.wdy.cheemate.service;

import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.SingleChatMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
public interface ISingleChatMessageService extends IService<SingleChatMessage> {
    ResponseEntity<ResultBean> putSingleChatMessage(SingleChatMessage singleChatMessage);

    SingleChatMessage sendMessage(SingleChatMessage singleChatMessage);

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean,Long receiveId);
}
