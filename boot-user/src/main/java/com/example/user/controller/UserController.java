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
package com.example.user.controller;

import com.example.core.wrapper.WrapMapper;
import com.example.core.wrapper.Wrapper;
import com.example.core.entity.User;
import com.example.user.entity.vo.UserVo;
import com.example.user.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 控制器
 *
 * @author ljy
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IUserService userService;

	/**
	 * 查询单条
	 */
	@PostMapping("/getUserById/{id}")
	@ApiOperation(httpMethod = "POST", value = "查询单个用户")
	public Wrapper getUserById(@PathVariable(value="id") Long id) {
		logger.info("createOrderDoc - 查询单个用户. id={}", id);
		UserVo orderDoc = userService.getUserById(id);
		return WrapMapper.ok(orderDoc);
	}

}
