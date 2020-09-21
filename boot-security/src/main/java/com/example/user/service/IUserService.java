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
package com.example.user.service;


import com.example.core.base.IService;
import com.example.core.entity.User;
import com.example.user.entity.vo.UserVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 服务类
 *
 * @author Chill
 */
public interface IUserService extends IService<User>, UserDetailsService {

	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 */
	UserVo getUserById(Long id);

	/**
	 * 根据name查询用户
	 * @param name
	 * @return
	 */
    UserVo getUserByName(String name);

	/**
	 * 新增用户
	 * @param user
	 */
	void addUser(User user);

	/**
	 * 更新用户信息
	 * @param user
	 */
    void updateUserById(User user);
}
