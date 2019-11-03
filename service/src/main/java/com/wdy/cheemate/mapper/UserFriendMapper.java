package com.wdy.cheemate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wdy.cheemate.common.constant.TableConstant;
import com.wdy.cheemate.entity.UserFriend;
import com.wdy.cheemate.entity.UserToFocus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:28
 * @description:
 */
public interface UserFriendMapper extends BaseMapper<UserFriend> {
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_USER_FRIEND + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_USER_FRIEND + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;

    // 分页获取数据开始
    @Select(value = GET_PAGE_ALL)
    List<UserFriend> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);
    // 分页获取数据结束
}