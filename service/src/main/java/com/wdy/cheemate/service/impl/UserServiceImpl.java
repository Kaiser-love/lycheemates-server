package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdy.cheemate.common.constant.*;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestUtil;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.dto.PageListAndConditions;
import com.wdy.cheemate.dto.WxUser;
import com.wdy.cheemate.entity.output.UserOutputVo;
import com.wdy.cheemate.entity.output.UserRankingOutputVo;
import com.wdy.cheemate.mapper.*;
import com.wdy.cheemate.entity.*;
import com.wdy.cheemate.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @program: UserServiceImpl
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-01 23:24
 */
@Service("UserService")
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private UserMapper userMapper;
    private UserToFocusMapper userToFocusMapper;
    private IUserFriendService userFriendService;
    private UserFriendMapper userFriendMapper;
    private UserChallengeInvitationMapper userChallengeInvitationMapper;

    @Override
    public ResponseEntity<ResultBean> userRegistry(WxUser wxUser) {
        User user = null;
        if (Func.hasEmpty(wxUser.getEncryptedData(), wxUser.getIv()) && !StringUtils.isEmpty(wxUser.getNickName())) {
            user = User.builder().build();
            if (StringUtils.isEmpty(wxUser.getOpenid())) {
                JSONObject apiResult = wxUser.getApiResult();
                String openid = (String) apiResult.getOrDefault("openid", "");
                wxUser.setOpenid(openid);
            }
            BeanUtils.copyProperties(wxUser, user);
            save(user);
        } else {
            JSONObject apiResult = wxUser.getApiResult();
            String session_key = (String) apiResult.getOrDefault("session_key", "");
            String openid = (String) apiResult.getOrDefault("openid", "");
            if (!StringUtils.isEmpty(session_key)) {
                apiResult = JSON.parseObject(WXUtils.getUserInfo(wxUser.getEncryptedData(), session_key, wxUser.getIv()));
                System.out.println(apiResult);
                System.out.println(apiResult.get("phoneNumber"));
                String province = apiResult.get("province").toString();
                String nickName = apiResult.get("nickName").toString();
                String gender = apiResult.get("gender").toString();
                String city = apiResult.get("city").toString();
                String country = apiResult.get("country").toString();
                String avatarUrl = apiResult.get("avatarUrl").toString();
                String appid = JSON.parseObject(apiResult.get("watermark").toString()).get("appid").toString();
                if (!StringUtils.isEmpty(nickName)) {
                    user = User.builder().phone(apiResult.get("phoneNumber") != null ? apiResult.get("phoneNumber").toString() : null).openid(openid).province(province).nickName(nickName).gender(Byte.valueOf(gender)).city(city).country(country).avatarUrl(avatarUrl).appid(appid).build();
                    save(user);
                }
            }
        }
        return ResponseHelper.BooleanResultBean(user, "存储微信新用户失败", user != null);
    }

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean) {
        PageListAndConditions pageListAndConditions = getUser(pageRequestBean);
        List<User> list = pageListAndConditions.getList();
        Integer pageAllSize = userMapper.getPageAllSize(pageListAndConditions.getConditionString());
        List<UserOutputVo> resultList = list.stream().map(user -> {
            UserOutputVo vo = fillUser(user);
            return vo;
        }).collect(Collectors.toList());
        UserMatchIntent userMatchIntent = UserMatchIntent.builder().conditionConnection(pageRequestBean.getConditionConnection()).conditions(pageRequestBean.getConditions()).build();
        User dataBaseUser = SecureUtil.getDataBaseUser();
        assert dataBaseUser != null;
        dataBaseUser.setUserMatchIntent(JSON.toJSONString(userMatchIntent));
        dataBaseUser.insertOrUpdate();
        return ResponseHelper.OK(resultList, pageAllSize);
    }

    @Override
    public PageListAndConditions getUser(PageRequestBean pageRequestBean) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
        List<User> list = userMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        return PageListAndConditions.builder().list(list).conditionString(conditionString).build();
    }

    @Override
    public ResponseEntity<ResultBean> getUserRankings(PageRequestBean pageRequestBean, Integer type) {
        Integer pageSize = 0;
        List<UserRankingOutputVo> resultList = new ArrayList<>();
        Long currentUserId = SecureUtil.getDataBaseUserId();
        // 好友
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            PageListAndConditions pageListAndConditions = userFriendService.getUserFriend(pageRequestBean);
            pageSize = userFriendMapper.getPageAllSize(pageListAndConditions.getConditionString());
            List<UserFriend> list = pageListAndConditions.getList();
            list.forEach(userFriend -> resultList.add(getUserRankingResultItem(userFriend.getFriendUserId())));
        }
        // 全服务器
        else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            PageListAndConditions pageListAndConditions = getUser(pageRequestBean);
            pageSize = userFriendMapper.getPageAllSize(pageListAndConditions.getConditionString());
            List<User> list = pageListAndConditions.getList();
            list.forEach(user -> {
                UserRankingOutputVo userRankingResultItem = getUserRankingResultItem(user.getId());
                if (user.getId().equals(currentUserId)) {
                    userRankingResultItem.setIsOwn(true);
                }
                resultList.add(userRankingResultItem);
            });
        }
        return ResponseHelper.OK(resultList, pageSize);
    }

    @Override
    public ResponseEntity<ResultBean> getConferenceCollection(String userId, String query, String connection, String queryString, Integer page, Integer count, Integer mode) {
//        List<Long> conferenceIds;
//        if (mode == 0) {
//            List<ConferenceCollection> conferenceCollections = conferenceCollectionService.getBaseMapper().selectList(new QueryWrapper<ConferenceCollection>().eq("user_id", SecureUtil.getDataBaseUserId()).orderByDesc("collect_time"));
//            conferenceIds = conferenceCollections.stream().map(ConferenceCollection::getConferenceId).collect(Collectors.toList());
//        } else {
//            List<BrowsingHistory> browsingHistories = browsingHistoryService.getBaseMapper().selectList(new QueryWrapper<BrowsingHistory>().eq("user_id", SecureUtil.getDataBaseUserId()).orderByDesc("browsing_time"));
//            conferenceIds = browsingHistories.stream().map(BrowsingHistory::getConferenceId).collect(Collectors.toList());
//        }
//        if (CollectionUtils.isEmpty(conferenceIds))
//            return ResponseHelper.OK(Collections.emptyList(), 0);
//        String inSqlString = SqlUtil.getInSqlString(conferenceIds);
//        StringBuffer sb = new StringBuffer("from ").append(TableConstant.TABLE_CONFERENCE).append(" where id in ").append(inSqlString);
//        boolean isDimQuery = false;
//        boolean isLikeQuery = false;
//        if (!Func.hasEmpty(query, queryString)) {
//            isDimQuery = true;
//            if (StringConstant.LIKE.equalsIgnoreCase(connection)) {
//                isLikeQuery = true;
//                queryString = "%" + queryString + "%";
//            }
//            sb.append(" and ").append(query + " ").append(connection).append(" \'" + queryString + "\'");
//        }
//        val sizeSql = StringConstant.COUNT_SQL + sb.toString();
//        val size = baseDao.findBySql(sizeSql).get(0);
//        List<Conference> data = new ArrayList<>();
//        // 条数小于需要的数量且不需要模糊查询  全部返回
//        if (conferenceIds.size() < count && !isDimQuery) {
//            conferenceIds.forEach(c -> data.add(conferenceService.getById(c)));
//        }
//        // 条数小于需要的数量且需要模糊查询
//        else if (conferenceIds.size() < count) {
//            String finalQueryString = queryString;
//            boolean finalIsLikeQuery = isLikeQuery;
//            conferenceIds.forEach(c -> {
//                Conference itemConference;
//                if (finalIsLikeQuery)
//                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", c).like(query, finalQueryString));
//                else
//                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", c).eq(query, finalQueryString));
//                if (itemConference != null)
//                    data.add(itemConference);
//            });
//        }
//        // 条数大于需要的数量且不需要模糊查询
//        else if (!isDimQuery) {
//            for (int i = page * count; i < page * count + count; i++) {
//                if (i >= conferenceIds.size())
//                    break;
//                data.add(conferenceService.getById(conferenceIds.get(i)));
//            }
//        }
//        // 条数大于需要的数量且需要模糊查询
//        else {
//            for (int i = page * count; i < conferenceIds.size(); i++) {
//                // 直到找到count数量需要的数据
//                if (data.size() == count)
//                    break;
//                Conference itemConference;
//                String finalQueryString = queryString;
//                boolean finalIsLikeQuery = isLikeQuery;
//                if (finalIsLikeQuery)
//                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", conferenceIds.get(i)).like(query, finalQueryString));
//                else
//                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", conferenceIds.get(i)).eq(query, finalQueryString));
//                if (itemConference != null)
//                    data.add(itemConference);
//            }
//        }
//        return ResponseHelper.OK(data, Integer.valueOf(size.toString()));
        return null;
    }

    @Override
    public UserOutputVo fillUser(User user) {
        UserOutputVo vo = BeanUtil.copy(user, UserOutputVo.class);
        Integer focusNumber = userToFocusMapper.selectCount(new QueryWrapper<UserToFocus>().eq(TableConstant.USER_TO_FOCUS.USER_ID, user.getId()));
        vo.setFocusUserNumber(focusNumber);
        Integer fansNumber = userToFocusMapper.selectCount(new QueryWrapper<UserToFocus>().eq(TableConstant.USER_TO_FOCUS.FOCUS_USER_ID, user.getId()));
        vo.setFansUserNumber(fansNumber);
        return vo;
    }

    private UserRankingOutputVo getUserRankingResultItem(Long userId) {
        UserChallengeInvitation userChallengeInvitation = userChallengeInvitationMapper.selectOne(new QueryWrapper<UserChallengeInvitation>().eq(TableConstant.TABLE_COMMON_COLUMN.SEND_USER_ID, userId).eq(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.IS_RUNNING));
        User user = userMapper.selectById(userId);
        long learningTime = 0L;
        String userType = TypeConstant.CommonType.USER_CHALLENGE_NOT_SIGN;
        // 好友用户发起的挑战
        if (userChallengeInvitation != null) {
            if (userChallengeInvitation.getSendSignTime() != null) {
                learningTime = TimeUtil.currentTimeStamp().getTime() - userChallengeInvitation.getSendSignTime().getTime();
                userType = TypeConstant.CommonType.SEND_USER;
            }
        }
        // 好友用户接受的挑战
        else {
            userChallengeInvitation = userChallengeInvitationMapper.selectOne(new QueryWrapper<UserChallengeInvitation>().eq(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID, userId).eq(TableConstant.TABLE_COMMON_COLUMN.STATE, StateConstant.Invitation.IS_RUNNING));
            if (userChallengeInvitation != null && userChallengeInvitation.getReceiveSignTime() != null) {
                learningTime = TimeUtil.currentTimeStamp().getTime() - userChallengeInvitation.getReceiveSignTime().getTime();
                userType = TypeConstant.CommonType.RECEIVE_USER;
            }
        }
        return UserRankingOutputVo.builder().user(user).userChallengeInvitation(userChallengeInvitation).type(userType).learningTime(TimeUtil.formatTime(learningTime)).build();
    }
}