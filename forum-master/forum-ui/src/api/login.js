import request from "@/utils/request";

// 登录
export function login(data) {
	return request({
		url: "/cas/login",
		method: "post",
		data: data,
	});
}

// 注册
export function register(data) {
	return request({
		url: "/register/commit",
		method: "post",
		data: data,
	});
}

// 注册验证码
export function registerSms(data) {
	return request({
		url: "/register/registerSms",
		method: "get",
		params: data,
	});
}

// 重置密码
export function resetPwd(data) {
	return request({
		url: "/register/resetPwd",
		method: "post",
		data: data,
	});
}

// 退出登录
export function logout(data) {
	return request({
		url: "/logout",
		method: "post",
		data: data,
	});
}
