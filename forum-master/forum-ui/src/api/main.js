import request from "@/utils/request";

// 标签列表
export function getLabelList(data) {
	return request({
		url: "/forum/label/list",
		method: "post",
		data: data,
	});
}

// 发布主题
export function topicPublish(data) {
	return request({
		url: "/topic/publish",
		method: "post",
		data: data,
	});
}

// 顶部搜索
export function getTopsearchList(data) {
	return request({
		url: "/forum/topic/searchList",
		method: "get",
		params: data,
	});
}

// 主题列表
export function getTopicList(data) {
	return request({
		url: "/forum/topic/list",
		method: "get",
		params: data,
	});
}

// 主题详情
export function getTopicDetails(data) {
	return request({
		url: "/forum/topic/detail",
		method: "get",
		params: data,
	});
}

// 获取主题回复
export function getMainReply(data) {
	return request({
		url: "/forum/reply/main/list",
		method: "get",
		params: data,
	});
}

// 获取二级回复
export function getTwoReply(data) {
	return request({
		url: "/forum/reply/list",
		method: "get",
		params: data,
	});
}

// 回复
export function topicReply(data) {
	return request({
		url: "/topic/reply",
		method: "post",
		data: data,
	});
}

// 点赞
export function praiseReply(data) {
	return request({
		url: "/topic/reply/praise",
		method: "post",
		data: data,
	});
}

// 举报
export function reportReply(data) {
	return request({
		url: "/topic/reply/report",
		method: "post",
		data: data,
	});
}

// 关注主题
export function topicFollow(data) {
	return request({
		url: "/topic/follow",
		method: "post",
		data: data,
	});
}

// 取消关注主题
export function topicUnfollow(data) {
	return request({
		url: "/topic/unfollow",
		method: "post",
		data: data,
	});
}
