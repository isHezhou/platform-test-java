package com.jc.test11.platform.utils;

import com.jc.platform.common.model.UserInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * ClassName PlatofrmTest11Application
 * Description {this.desc!''}
 *
 * @author 平台管理员
 * @version 7.0
 * @date 2020-09-10 08:27:11
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {

    /**
     * Gets login user.
     *
     * @return the login user
     */
    public static UserInfo getLoginUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1L);
        userInfo.setUserName("管理员");
        return userInfo;
    }
}
