<template>
	<div class="userInfoPage">
		<div class="top_content">
			<div class="t_userInfo container">
				<div class="info_left">
					<el-avatar :size="96" :src="userInfo.avatar || avatar"></el-avatar>
					<el-upload
						class="upload"
						action="#"
						:limit="1"
						:show-file-list="false"
						accept=".png,.jpg,.jpeg"
						:before-upload="upUserAvatar"
					>
						<div class="uploadSlot"><i class="el-icon-edit"></i></div>
					</el-upload>
				</div>
				<div class="info_right">
					<div class="info_username">{{ userInfo.nickName }}</div>
					<div class="info_status">
						<span>
							<i class="el-icon-s-opportunity"></i>
							最近在线 {{ userInfo.loginDate }}
						</span>
						<span>注册于 {{ userInfo.createTime }}</span>
					</div>
					<div class="info_simple">
						<el-input
							type="textarea"
							v-model="userInfo.remark"
							@blur="changeSimple"
							placeholder="这个人很懒,什么都没有留下..."
							resize="none"
						></el-input>
					</div>
				</div>
			</div>
		</div>
		<div class="main_content container">
			<div class="slide_left">
				<div class="slideList">
					<div
						:class="{ slideItem: true, active: activeId == item.id }"
						@click="slideClick(item.id)"
						v-for="(item, i) in slideList"
						:key="i"
					>
						<i :class="item.icon">{{ item.iconText }}</i>
						{{ item.label }}
					</div>
				</div>
			</div>
			<div class="content_right">
				<div class="infoSet" v-if="activeId == 6">
					<div class="setType">
						<h4>账户设置</h4>
						<div class="setInfoBtn">
							<el-button type="primary" size="small" @click="changeInfo(1)">更改密码</el-button>
							<el-button type="primary" size="small" @click="changeInfo(4)">更改手机</el-button>
							<el-button type="primary" size="small" @click="changeInfo(2)">更改邮箱</el-button>
							<el-button type="primary" size="small" @click="changeInfo(3)">更改昵称</el-button>
						</div>
					</div>
				</div>
				<div v-else-if="[2, 3, 4, 5].includes(activeId)" v-loading="themeLoading">
					<div class="themeList">
						<div class="notData" v-if="!total && !themeLoading">这里空空如也...</div>
						<div class="themeItem" v-for="(item, i) in themeList" :key="i" @click="itemClick(item)">
							<div class="themeLeft">
								<div class="themeTitle">
									<h4>{{ item.title }}</h4>
									<div class="themeLabels">
										<div
											v-for="(el, j) in item.topicLabelVoList"
											:key="j"
											:class="{ parentItem: !el.parentId, childItem: el.parentId }"
										>
											{{ el.labelName }}
										</div>
										<!-- <div>教程</div> -->
									</div>
								</div>
								<div class="themeContent">
									<div class="themeInfo">
										<div class="themeUser">
											<div>@{{ item.createUser.nickName }}</div>
											<span style="margin: 0 4px;">·</span>
											<div>
												{{ item.latestReplyDateName || item.latestReplyDate || item.createTime }}
											</div>
										</div>
										<div class="themeTips">
											<i class="el-icon-star-on" v-if="item.follow"></i>
										</div>
									</div>
									<div class="themeText">{{ item.tips }}</div>
								</div>
							</div>
							<div class="themeRight">
								<div class="replyNum">
									<i class="el-icon-chat-round"></i>
									<span>{{ item.replyNum }}</span>
								</div>
								<div class="browseNum">
									<i class="el-icon-view"></i>
									<span>{{ item.visitNum }}</span>
								</div>
							</div>
						</div>
					</div>
					<div style="text-align: center;">
						<pagination
							v-show="total > 0"
							:total="total"
							:page.sync="formData.pageNum"
							:limit.sync="formData.pageSize"
							:background="false"
							:layout="layout"
							@pagination="search"
						/>
					</div>
				</div>
				<div class="notContent" v-else>暂无内容</div>
			</div>
		</div>
		<changeUserInfo
			ref="changeUserInfo"
			@changeNickName="changeNickName"
			@changeEmail="changeEmail"
			@changePhone="changePhone"
		></changeUserInfo>
	</div>
</template>

<script>
import changeUserInfo from "@/components/changeUserInfo/index.vue";
import { getUserInfo, updateUser, updateAvatar } from "@/api/user";
import { getTopicList } from "@/api/main";
import store from "@/store/index";
import { getPlainText } from "@/utils/auth";
import { scrollTo } from "@/utils/scroll-to";
export default {
	components: { changeUserInfo },
	data() {
		return {
			layout: "total, prev, pager, next",
			total: 0,
			formData: {
				pageNum: 1,
				pageSize: 20,
			},
			themeLoading: false,
			themeList: [],
			activeId: 1,
			avatar: require("@/assets/u111.png"),
			infoSimple: "",
			userInfo: {},
			slideList: [
				{
					id: 1,
					icon: "el-icon-s-data",
					label: "等级",
				},
				{
					id: 2,
					icon: "el-icon-chat-round",
					label: "回复",
				},
				{
					id: 3,
					icon: "el-icon-s-help",
					label: "主题",
				},
				{
					id: 4,
					icon: "el-icon-s-flag",
					label: "赞",
				},
				{
					id: 5,
					iconText: "@",
					label: "被提及",
				},
				{
					id: 6,
					icon: "el-icon-s-tools",
					label: "设置",
				},
			],
		};
	},

	created() {
		this.getUserInfo();
		let activeId = this.$route.query.activeId || 1;
		this.slideClick(+activeId);
	},

	methods: {
		search() {
			this.themeLoading = true;
			let params = { ...this.formData };
			params.labelId = this.$route.query.labelId;
			params.type = this.activeId;
			getTopicList(params)
				.then((res) => {
					res.rows.forEach((el) => {
						el.tips = getPlainText(el.topicContent);
					});
					this.themeList = res.rows;
					this.total = res.total;
				})
				.finally(() => {
					this.themeLoading = false;
				});
		},
		itemClick(item) {
			this.$router.push({
				path: "/d",
				query: { id: item.id },
			});
		},
		changeEmail(email) {
			this.userInfo.email = email;
		},
		changePhone(phonenumber) {
			this.userInfo.phonenumber = phonenumber;
		},
		changeNickName(name) {
			this.userInfo.nickName = name;
			store.commit("SET_NICKNAME", name);
		},
		upUserAvatar(file) {
			let params = new FormData();
			params.append("file", file);
			updateAvatar(params).then((res) => {
				updateUser({
					id: this.userInfo.id,
					userName: this.userInfo.userName,
					avatar: res.url,
				}).then(() => {
					this.userInfo.avatar = res.url;
					store.commit("SET_AVATAR", res.url);
					this.$message.success("上传成功");
				});
			});
			return false;
		},
		getUserInfo() {
			getUserInfo().then((res) => {
				this.userInfo = res.data;
			});
		},
		changeInfo(val) {
			this.$refs.changeUserInfo.show(val, this.userInfo);
		},
		slideClick(val) {
			this.activeId = val;
			if (val > 1 && val < 6) {
				this.formData.pageNum = 1;
				this.search();
				scrollTo(0, 800);
			}
		},
		changeSimple() {
			let params = {
				id: this.userInfo.id,
				userName: this.userInfo.userName,
				remark: this.userInfo.remark,
			};
			updateUser(params).then(() => {
				// this.$message.success("修改成功");
			});
		},
	},
};
</script>

<style lang="less" scoped>
.userInfoPage {
	min-height: calc(100vh - 52px);
	.top_content {
		width: 100%;
		background-color: #2c4c8e;
	}
	.t_userInfo {
		padding: 30px 0 20px;
		display: flex;
		.info_left {
			margin-right: 30px;
			position: relative;
			height: 104px;
			border: 4px solid #ffffff;
			border-radius: 50%;
			overflow: hidden;
			&:hover .uploadSlot {
				display: block;
			}
			.uploadSlot {
				display: none;
				width: 96px;
				height: 96px;
				background-color: #00000091;
				text-align: center;
				line-height: 96px;
				font-size: 22px;
				color: #ffffff;
				border-radius: 50%;
				position: absolute;
				top: 0;
				left: 0;
			}
		}
		.info_right {
			color: #ffffff;
			text-align: left;
			.info_username {
				font-size: 22px;
			}
			.info_status {
				margin: 20px 0;
				i {
					margin-right: 4px;
				}
				span {
					margin-right: 10px;
				}
			}
			.info_simple {
				width: 680px;
				/deep/ .el-textarea__inner {
					background-color: transparent;
					border-color: transparent;
					border-style: dashed;
					color: #ffffff;
					&:hover {
						border-color: #e4e4e4;
					}
				}
			}
		}
	}
	.main_content {
		display: flex;
		.slide_left {
			text-align: left;
			margin-right: 30px;
			.slideList {
				margin-top: 30px;
				position: sticky;
				top: 80px;
				.slideItem {
					padding: 10px 20px;
					cursor: pointer;
					&:hover,
					&.active {
						color: #409eff;
					}
					i {
						list-style: none;
						margin-right: 10px;
						font-size: 16px;
						font-weight: 700;
					}
				}
			}
		}
		.content_right {
			flex: 1;
			// margin-top: 30px;
			.themeList {
				width: 100%;
				margin-top: 14px;
				margin-bottom: 14px;
				.notData {
					padding: 20px 0;
					text-align: center;
					font-size: 18px;
					color: #9a9a9a;
				}
				.themeItem {
					display: flex;
					padding: 14px 20px;
					cursor: pointer;
					// border-radius: 4px;
					// border-bottom: 1px solid #e3e7ec;
					&:hover {
						background-color: #eff6ff;
					}
					.themeLeft {
						margin-right: 20px;
						flex: 1;
					}
					.themeRight {
						text-align: left;
						color: #667f99;
						div {
							display: flex;
							align-items: center;
						}
						i {
							margin-right: 10px;
							font-size: 16px;
						}
					}
					.themeTitle {
						display: flex;
						align-items: center;
						justify-content: space-between;
						flex-wrap: wrap;
						h4 {
							font-size: 20px;
							color: #000;
							margin-bottom: 10px;
						}
						.themeLabels {
							display: flex;
							align-items: center;
							div {
								padding: 2px 4px;
								font-size: 12px;
								&.parentItem {
									background-color: #626c78;
									color: #fff;
								}
								&.childItem {
									background-color: #e4edf6;
									color: #667f99;
								}
								&:first-child {
									border-radius: 4px 0 0 4px;
								}
								&:last-child {
									border-radius: 0 4px 4px 0;
								}
							}
						}
					}
					.themeContent {
						.themeInfo {
							display: flex;
							align-items: center;
							justify-content: space-between;
							.themeUser {
								display: flex;
								color: #b3b3b3;
								div:first-child {
									color: #667f99;
								}
							}
							.themeTips {
								color: #de8e00;
								font-size: 16px;
							}
						}
						.themeText {
							width: 100%;
							max-height: 50px;
							font-size: 13px;
							overflow: hidden;
							color: #b3b3b3;
							text-align: left;
							-webkit-line-clamp: 3;
							text-overflow: ellipsis;
							display: -webkit-box;
							-webkit-box-orient: vertical;
						}
					}
				}
			}
			.infoSet {
				padding-top: 40px;
				text-align: left;
				.setType {
					h4 {
						font-size: 16px;
						font-weight: 700;
						margin-bottom: 20px;
					}
				}
			}
			.notContent {
				text-align: center;
				font-size: 20px;
				color: #a6a6a6;
				padding-top: 60px;
			}
		}
	}
}
</style>
