import Vue from "vue";
import Vuex from "vuex";
import { login, logout } from "@/api/login";
import { getUserInfo } from "@/api/user";
import { getToken, setToken, removeToken } from "@/utils/auth";

Vue.use(Vuex);

export default new Vuex.Store({
	state: { token: getToken(), username: "", nickname: "", avatar: "" },
	mutations: {
		SET_TOKEN: (state, token) => {
			state.token = token;
		},
		SET_USERNAME: (state, username) => {
			state.username = username;
		},
		SET_NICKNAME: (state, nickname) => {
			state.nickname = nickname;
		},
		SET_AVATAR: (state, avatar) => {
			state.avatar = avatar;
		},
	},
	actions: {
		// 登录
		Login({ commit }, userInfo) {
			return new Promise((resolve, reject) => {
				login(userInfo)
					.then((res) => {
						setToken(res.token);
						commit("SET_TOKEN", res.token);
						resolve();
					})
					.catch((error) => {
						reject(error);
					});
			});
		},

		// 获取用户信息
		GetInfo({ commit, state }) {
			return new Promise((resolve, reject) => {
				getUserInfo()
					.then((res) => {
						const user = res.data;
						const avatar =
							user.avatar == "" || user.avatar == null ? require("@/assets/u111.png") : user.avatar;
						// : process.env.VUE_APP_BASE_API + user.avatar;
						commit("SET_USERNAME", user.userName);
						commit("SET_NICKNAME", user.nickName);
						commit("SET_AVATAR", avatar);
						resolve(res);
					})
					.catch((error) => {
						reject(error);
					});
			});
		},

		// 退出系统
		LogOut({ commit, state }) {
			return new Promise((resolve, reject) => {
				logout(state.token)
					.then(() => {
						commit("SET_TOKEN", "");
						removeToken();
						resolve();
					})
					.catch((error) => {
						reject(error);
					});
			});
		},

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_USERNAME', '')
        removeToken()
        resolve()
      })
    }
	},
	modules: {},
	getters: {
		token: (state) => state.token,
		avatar: (state) => state.avatar,
		nickname: (state) => state.nickname,
		username: (state) => state.username,
	},
});
