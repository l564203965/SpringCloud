package com.example.user.entity.vo;

import lombok.Data;

/**
 * @author: ljy  Date: 2020/1/17.
 */
@Data
public class UserVo {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String account;

    private String password;

    private String name;
}
