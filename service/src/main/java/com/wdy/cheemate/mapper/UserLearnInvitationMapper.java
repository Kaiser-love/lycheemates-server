package com.wdy.cheemate.mapper;

import com.wdy.cheemate.common.constant.TableConstant;
import com.wdy.cheemate.entity.UserLearnInvitation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
public interface UserLearnInvitationMapper extends BaseMapper<UserLearnInvitation> {
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.USER_LEARN_INVITATION + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.USER_LEARN_INVITATION + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;

    // 分页获取数据开始
    @Select(value = GET_PAGE_ALL)
    List<UserLearnInvitation> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);
    // 分页获取数据结束
}
