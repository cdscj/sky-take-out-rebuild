import request from "@/utils/request";

// 获取用户信息
export function getUserInfo(data) {
	return request({
		url: "/personal/userInfo",
		method: "get",
		params: data,
	});
}

// 修改用户信息
export function updateUser(data) {
	return request({
		url: "/personal/updateUser",
		method: "post",
		data: data,
	});
}

// 修改密码
export function changePwd(data) {
	return request({
		url: "/personal/changePwd",
		method: "post",
		data: data,
	});
}

// 发送邮箱验证码
export function sendEmail(data) {
	return request({
		url: "/personal/sendEmail",
		method: "post",
		data: data,
	});
}

// 发送手机验证码
export function sendSms(data) {
	return request({
		url: "/personal/sendSms",
		method: "post",
		data: data,
	});
}

// 修改邮箱
export function changeEmail(data) {
	return request({
		url: "/personal/changeEmail",
		method: "post",
		data: data,
	});
}

// 修改手机
export function changePhone(data) {
	return request({
		url: "/personal/changePhone",
		method: "post",
		data: data,
	});
}

// 上传图片
export function updateAvatar(data) {
	return request({
		url: "/personal/upload/avatar",
		method: "post",
		data: data,
	});
}
