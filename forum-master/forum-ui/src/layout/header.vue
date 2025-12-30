<template>
	<div style="height: 52px;">
		<div :class="{ pageHeader: true, scrolled }">
			<div class="container headContent">
				<div class="headLeft">
					<div class="logo">
						<el-link type="primary" @click="goIndex">文心论坛</el-link>
					</div>
					<!-- <div class="headNavs">
						<div class="navItem active" @click="goIndex">主页</div>
						<div class="navItem">文档</div>
					</div> -->
				</div>
				<div class="headRight">
					<div class="searchBar">
						<i class="el-icon-search searchIcon"></i>
						<input
							ref="searchInput"
							type="text"
							v-model="searchText"
							placeholder="搜索"
							@focus="searchFocus"
							@blur="searchBlur"
							@keyup="searchKeyup"
						/>
						<i
							class="el-icon-circle-close clearIcon"
							v-if="searchText"
							@click="clearSearchText"
						></i>
						<div class="searchList" v-if="isFocus&&this.searchText" :style="{ opacity: isopacity ? 1 : 0 }">
							<h3>主题</h3>
							<div class="searchText" @click="searchgo">
								<i class="el-icon-search"></i>
								<span>搜索"{{this.searchText}}"</span>
							</div>
							<div class="topicList">
								<div class="topicItem" v-for="(item,i) in dataList" :key="i" @click="itemClick(item.id)">
									<h4>{{ item.title }}</h4>
									<div>{{ getPlainText(item.topicContent) }}</div>
								</div>
							</div>
						</div>
					</div>
					<el-dropdown @command="handleCommand" v-if="username">
						<div class="userInfo">
							<el-avatar :size="26" :src="avatar"></el-avatar>
							<span>{{ nickname }}</span>
						</div>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item command="a">个人中心</el-dropdown-item>
							<el-dropdown-item command="b">退出登录</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
					<div class="loginBox" v-else>
						<div class="registerBtn" @click="loginBoxShow(2)">注册</div>
						<div class="loginBtn" @click="loginBoxShow(1)">登录</div>
					</div>
				</div>
			</div>
		</div>
		<loginAndregister ref="loginAndregister" @loginSucc="loginSucc"></loginAndregister>
	</div>
</template>

<script>
import { mapGetters } from "vuex";
import { eventBus } from "@/main";
import { getTopsearchList } from "@/api/main";
import { getPlainText } from "@/utils/auth";
export default {
	data() {
		return {
			searchText: "",
			userName: "",
			getPlainText,
			isopacity: false,
			isFocus: false,
			scrolled: false,
			searchLoading: false,
			timeId: null,
			userAvater: require("@/assets/u111.png"),
			dataList: [],
		};
	},

	computed: {
		...mapGetters(["nickname", "avatar", "token", "username"]),
	},

	created() {
		if (this.token) this.$store.dispatch("GetInfo");
		this.scrolled = window.pageYOffset ? true : false;
		window.addEventListener("scroll", this.handleScrollbox, true);
		this.$nextTick(() => {
			eventBus.$on("needLogin", () => {
				this.loginBoxShow(1);
			});
		});
	},

	methods: {
		searchgo(){
			this.$router.replace({path: '/',query: {q: this.searchText}});
		},
		itemClick(id){
			this.$router.push({
				path: "/d",
				query: { id },
			});
		},
		searchKeyup(e){
			clearTimeout(this.timeId);
			if(e.keyCode==13) return this.searchgo();
			this.timeId = setTimeout(()=>{
				this.searchList();
			}, 500)
		},
		searchList(){
			this.searchLoading = true;
			getTopsearchList({name: this.searchText}).then(res=>{
				this.dataList = res.data;
			}).finally(() => this.searchLoading = false);
		},
		clearSearchText() {
			this.searchText = "";
			let query = this.$route.query;
			this.$router.replace({path: '/', query: {...query,q: undefined}});
		},
		goIndex() {
			this.$router.push("/").catch(() => {});
		},
		handleCommand(v) {
			if (v == "a") {
				this.$router
					.push({
						path: "/u",
						query: { userId: this.username },
					})
					.catch(() => {});
			}
			if (v == "b") {
				// 退出登录
				this.$confirm("确定退出登录吗？", "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning",
				})
					.then(() => {
						this.$store.dispatch("LogOut").then(() => {
							location.href = "/web/";
						});
					})
					.catch(() => {});
			}
		},
		loginSucc(userName) {
			this.userName = userName;
		},
		loginBoxShow(type) {
			this.$refs.loginAndregister.show(type);
		},
		handleScrollbox() {
			this.scrolled = window.pageYOffset ? true : false;
		},
		searchFocus() {
			this.isopacity = true;
			this.isFocus = true;
			this.$refs.searchInput.style.width = "300px";
		},
		searchBlur() {
			this.isopacity = false;
			setTimeout(()=>{
				this.isFocus = false;
			},500);
			this.$refs.searchInput.style.width = "225px";
		},
	},
};
</script>

<style lang="less" scoped>
.pageHeader {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 52px;
	padding: 0 20px;
	background-color: #fff;
	border-bottom: 1px solid #e4edf6;
	z-index: 100;
	&.scrolled {
		box-shadow: 0 2px 6px #e3e3e3;
	}
	.headContent {
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: space-between;
		.headLeft {
			height: 100%;
			display: flex;
			align-items: center;
			.logo .el-link {
				font-size: 20px;
			}
			.headNavs {
				height: 100%;
				display: flex;
				margin-left: 20px;
				.navItem {
					height: 100%;
					padding: 0 20px;
					cursor: pointer;
					display: flex;
					align-items: center;
					// &.active{
					// 	color: #409eff;
					//   background-color: #f6f6f6;
					// }
					&:hover {
						background-color: #f6f6f6;
						color: #409eff;
					}
				}
			}
		}
		.headRight {
			height: 100%;
			display: flex;
			align-items: center;
			.searchBar {
				position: relative;
				margin-right: 20px;
				input {
					width: 100%;
					height: 100%;
					width: 225px;
					height: 36px;
					box-sizing: border-box;
					background-color: #e4edf6;
					padding: 0 32px;
					border: 0;
					border-radius: 4px;
					outline-color: #409eff;
					transition: width 0.5s;
					&:focus {
						background-color: #ffffff;
					}
				}
				.searchIcon {
					position: absolute;
					left: 8px;
					top: 48%;
					transform: translateY(-50%);
					font-size: 16px;
					font-weight: 700;
				}
				.clearIcon {
					position: absolute;
					right: 8px;
					top: 48%;
					transform: translateY(-50%);
					cursor: pointer;
					font-size: 16px;
					font-weight: 700;
				}
				.searchList{
					position: absolute;
					top: 46px;
					left: 0;
					width: 300px;
					min-height: 40px;
					background-color: #fff;
					box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
					border-radius: 4px;
					text-align: left;
					h3{
						padding: 8px 10px;
						background-color: #f0f7ff;
					}
					.searchText{
						padding: 8px 12px;
						cursor: pointer;
						i{
							margin-right: 4px;
							font-weight: 700;
						}
						&:hover{
							background-color: #cee6ff;
						}
					}
					.topicList{
						// padding-bottom: 10px;
						max-height: 500px;
						overflow-y: auto;

						.topicItem{
							padding: 6px 16px;
							cursor: pointer;
							&:hover{
								background-color: #cee6ff;
							}
							div{
								font-size: 12px;
								color: #205387;
								display: -webkit-box;
								-webkit-line-clamp: 2; /* 显示的行数 */
								-webkit-box-orient: vertical;
								overflow: hidden;
								text-overflow: ellipsis;
								margin-bottom: 8px;
							}
						}
					}
				}
			}
			.userInfo {
				display: flex;
				align-items: center;
				padding: 4px 8px;
				border-radius: 16px;
				cursor: pointer;
				&:hover {
					background-color: #d5d5d5;
				}
				.el-avatar {
					margin-right: 8px;
				}
			}
			.loginBox {
				height: 100%;
				display: flex;
				div {
					height: 100%;
					padding: 0 20px;
					cursor: pointer;
					display: flex;
					align-items: center;
					&:hover {
						background-color: #f6f6f6;
						color: #409eff;
					}
				}
			}
		}
	}
}
</style>
