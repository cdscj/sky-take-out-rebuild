export function getToken() {
	return localStorage.getItem("token");
}

export function setToken(token) {
	return localStorage.setItem("token", token);
}

export function removeToken() {
	return localStorage.removeItem("token");
}

//转意符换成普通字符
function convertIdeogramToNormalCharacter(val) {
	const arrEntities = { lt: "<", gt: ">", nbsp: " ", amp: "&", quot: '"' };
	return val.replace(/&(lt|gt|nbsp|amp|quot);/gi, function(all, t) {
		return arrEntities[t];
	});
}

// 获取富文本的纯文字内容
export const getPlainText = (richCont) => {
	const str = richCont;
	let value = richCont;
	if (richCont) {
		// 方法一：
		value = value.replace(/\s*/g, ""); //去掉空格
		value = value.replace(/<[^>]+>/g, ""); //去掉所有的html标记
		value = value.replace(/↵/g, ""); //去掉所有的↵符号
		value = value.replace(/[\r\n]/g, ""); //去掉回车换行
		value = value.replace(/&nbsp;/g, ""); //去掉空格
		value = convertIdeogramToNormalCharacter(value);
		value = value.replace(/<[^>]+>/g, ""); //去掉所有的html标记
		return value;

		// 方法二：
		// value = value.replace(/(\n)/g, "");
		// value = value.replace(/(\t)/g, "");
		// value = value.replace(/(\r)/g, "");
		// value = value.replace(/<\/?[^>]*>/g, "");
		// value = value.replace(/\s*/g, "");
		// value = convertIdeogramToNormalCharacter(value);
		// value = value.replace(/<\/?[^>]*>/g, "");
		// return value;
	} else {
		return null;
	}
};
