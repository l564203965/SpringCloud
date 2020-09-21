/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.user.service.impl;

import com.example.core.base.BaseService;
import com.example.core.entity.User;
import com.example.core.utils.PublicUtil;
import com.example.user.entity.vo.UserVo;
import com.example.user.mapper.UserMapper;
import com.example.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class UserServiceImpl extends BaseService<User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserVo getUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return conversionVo(user);
    }

    @Override
    public UserVo getUserByName(String name) {
        logger.info("name=========>"+name);
        User user = userMapper.getUserByName(name);
        return conversionVo(user);
    }

    private UserVo conversionVo(User user) {
        UserVo userVo = null;
        if (PublicUtil.isNotEmpty(user)) {
            userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
        }
        return userVo;
    }
}
