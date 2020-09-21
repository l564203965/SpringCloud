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
package com.example.core.entity;

import com.example.core.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实体类
 *
 * @author Chill
 */
@Data
@Table(name="user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 账号
	 */
	@Column(name="account")
	private String account;
	/**
	 * 密码
	 */
	@Column(name="password")
	@Length(min = 6, message = "密码长度至少6位")
	private String password;
	/**
	 * 昵称
	 */
	@Column(name="name")
	@NotBlank(message = "name不能为空")
	private String name;

}
