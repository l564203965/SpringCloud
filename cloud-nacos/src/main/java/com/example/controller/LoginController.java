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
package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.core.wrapper.WrapMapper;
import com.example.core.wrapper.Wrapper;
import com.example.user.entity.dto.RequestResultDto;
import com.example.user.service.ILoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 控制器
 *
 * @author ljy
 */
@RestController
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ILoginService loginService;

	/**
	 * 登录
	 *
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Wrapper login(String json) {
		logger.info("login - 登录. json={}", json);
		try {
            JSONObject jsonObject = JSON.parseObject(json);
            RequestResultDto result = loginService.login(jsonObject.getString("name"),jsonObject.getString("password"));
			return WrapMapper.ok(result);
		} catch (Exception e) {
			logger.error("登录失败",e);
			return WrapMapper.error();
		}
	}

}
