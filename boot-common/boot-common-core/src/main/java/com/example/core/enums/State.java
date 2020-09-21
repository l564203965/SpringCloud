package com.example.core.enums;

public enum State {
	// 操作成功
	success(200, "操作成功！"),
	bad_params(400, "缺失参数或参数格式不正确！"),
	bad_token(401, "token验证失败"),
	failure(402, "参数格式正确但是请求失败"),
	error(403, "请求失败"),
	not_found(404,"请求的资源不存在"),
	unauthorized(405, "没有菜单权限"),
	validate_error(406, "账号或密码错误"),
	captcha_error(407, "生成验证码错误"),
	pwd_error(408, "原密码错误，请重新输入"),
	roleName_error(410, "角色名称已经被使用，请重新输入"),
	insert_error(500, "该用户名已经存在，请更换用户名"),
	busi_error(501, "当前区域权限对应的业务不存在"),
	captcha_input_error(408, "验证码错误！");
	private int code;
	private String msg;

	private State(int code, String msg) {
		this.msg = msg;
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "{code:\"" + code + "\", msg:\"" + msg + "\"}";
	}
}
