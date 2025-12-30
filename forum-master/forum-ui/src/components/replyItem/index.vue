<template>
	<div class="replyMain">
		<div class="replyItem">
			<div class="mainTop">
				<div class="releaseTime">{{ replyData.updateTime }}</div>
				<div
					class="userInfo"
					@mouseenter="userInfoHover($event, replyData)"
					@mouseleave="userInfoOut($event, replyData)"
				>
					<el-avatar :size="26" :src="replyData.userVo.avatar"></el-avatar>
					<span>{{ replyData.userVo.nickName }}</span>
					<transition name="el-fade-in">
						<div class="userInfoDetails" v-show="replyData.userDetailsShow">
							<div class="details_left">
								<el-avatar :size="100" :src="replyData.userVo.avatar"></el-avatar>
							</div>
							<div class="details_right">
								<div class="d_right_name">{{ replyData.userVo.nickName }}</div>
								<div class="d_right_times">
									<span><i class="el-icon-time"></i>{{ replyData.userVo.loginDate }}</span>
									<span>注册于 {{ replyData.userVo.createTime }}</span>
								</div>
								<div class="d_right_simple">{{ replyData.userVo.remark }}</div>
							</div>
						</div>
					</transition>
				</div>
			</div>
			<div class="mainBtm">
				<div class="mainHtml" v-html="replyData.replyContent"></div>
				<!-- <div class="replyUser">
										<i class="el-icon-share"></i>
										<el-link>土拨鼠</el-link>
										<span>和</span>
										<el-link>小叮当</el-link>
										<span>回复了此贴</span>
									</div> -->
				<div class="replyOn_box" ref="replyOn_box" v-if="replyOnShow || isEdit">
					<div class="replyOnList">
						<div class="onLoading" v-if="onLoading">
							<i class="el-icon-loading"></i>
						</div>
						<div class="replyOnItem" v-for="(item, i) in setOnList" :key="i">
							<div class="replyOnItem_content">
								<div class="userInfo">
									<el-avatar :size="20" :src="item.userVo.avatar"></el-avatar>
									<el-link type="warning">{{ item.userVo.nickName }}</el-link>
								</div>
								<template v-if="item.replyUser">
									<span>回复</span>
									<div class="userInfo">
										<el-avatar :size="20" :src="item.replyUser.avatar"></el-avatar>
										<el-link type="warning">{{ item.replyUser.nickName }}</el-link>
									</div>
								</template>
								<strong>:</strong>
								<!-- <div class="replyOn_text">{{ item.replyContent }}</div> -->
								{{ item.replyContent }}
							</div>
							<div class="replyOn_action">
								<el-button type="text" v-if="item.praise" @click="unPraiseReply(item)"
									>取消赞</el-button
								>
								<el-button type="text" v-else @click="setPraiseReply(item)">赞</el-button>
								<el-button type="text" @click="openReport(item)">举报</el-button>
								<span>{{ item.createTime }}</span>
								<el-button type="text" @click="openReplyInput(item)">回复</el-button>
							</div>
						</div>
					</div>
					<div class="loadMore" v-if="total > 4">
						<div style="padding-left: 10px;" v-if="loadMoreShow">
							{{ total > 10 ? "共有" + total : "还有" + (total - 4) }}条回复,
							<el-button type="text" size="mini" @click="showMore">点击查看</el-button>
						</div>
						<el-pagination
							v-if="total > 10 && !loadMoreShow"
							layout="total, prev, pager, next"
							:current-page.sync="formData.pageNum"
							:total="total"
							small
							@current-change="handleCurrentChange"
						></el-pagination>
					</div>
					<div class="replayOn_input" v-if="isEdit">
						<el-input
							type="textarea"
							:rows="2"
							:placeholder="setPlaceholder"
							v-model="textarea"
							show-word-limit
							maxlength="200"
						>
						</el-input>
						<el-button type="primary" size="mini" style="margin-top: 10px;" @click="sendReply"
							>发表</el-button
						>
					</div>
				</div>
			</div>
			<div class="replyBtn">
				<el-button type="text" @click="openReplyInput()">回复</el-button>
				<el-button type="text" v-if="replyData.praise" @click="unPraiseReply(replyData)"
					>取消赞</el-button
				>
				<el-button type="text" v-else @click="setPraiseReply(replyData)">赞</el-button>
				<el-button type="text" @click="openReport(replyData)">举报</el-button>
			</div>
		</div>
	</div>
</template>

<script>
import { getTwoReply, topicReply } from "@/api/main";
import { mapGetters } from "vuex";
import { eventBus } from "@/main";
export default {
	props: {
		data: {
			type: Object,
			default: {
				userVo: {},
			},
		},
		rowId: {
			type: Number,
			default: 0,
		},
	},
	data() {
		return {
			textarea: "",
			userAvater: require("@/assets/u111.png"),
			isEdit: false,
			replyData: this.data,
			onLoading: false,
			loadMoreShow: true,
			replyOnShow: true,
			ob: null,
			replyOnList: [],
			replyObj: null,
			formData: {
				replyId: this.data.id,
				pageNum: 1,
				pageSize: 10,
			},
			total: 0,
		};
	},
	computed: {
		...mapGetters(["username"]),
		setOnList: {
			get() {
				if (this.total > 4 && this.loadMoreShow) return this.replyOnList.filter((el, i) => i < 4);
				return this.replyOnList;
			},
		},
		setPlaceholder: {
			get() {
				if (this.replyObj) {
					return `回复 @${this.replyObj.userVo.nickName} :`;
				}
				return "请输入内容";
			},
		},
	},
	mounted() {
		let dom = this.$refs.replyOn_box;
		this.ob = new IntersectionObserver(
			async (ent) => {
				if (!ent[0].isIntersecting || this.onLoading) return;
				await this.getReplyOn(dom);
			},
			{
				root: null,
				threshold: 0,
			}
		);
		this.ob.observe(dom);
	},

	methods: {
		showMore() {
			this.loadMoreShow = false;
		},
		handleCurrentChange(val) {
			this.formData.pageNum = val;
			this.getReplyOn();
		},
		setPraiseReply(data) {
			this.$emit("setPraiseReply", { ...data }, () => {
				this.$set(data, "praise", 1);
			});
		},
		unPraiseReply(data) {
			this.$emit("unPraiseReply", { ...data }, () => {
				this.$set(data, "praise", 0);
			});
		},
		openReport(data) {
			this.$emit("openReport", { ...data });
		},
		sendReply() {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			if (!this.textarea) return this.$message.warning("请输入回复内容");
			let params = {
				topicId: this.rowId,
				replyMainId: this.replyData.id,
				replyContent: this.textarea,
				replyId: this.replyObj ? this.replyObj.id : undefined,
			};
			topicReply(params).then(() => {
				this.$message.success("发表成功");
				this.getReplyOn();
				this.textarea = "";
				this.isEdit = false;
			});
		},
		openReplyInput(item) {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			if (!item) {
				this.replyObj = null;
				this.isEdit = !this.isEdit;
				return;
			}
			this.replyObj = { ...item };
			this.isEdit = true;
		},
		getReplyOn(dom) {
			this.onLoading = true;
			let params = { ...this.formData };
			getTwoReply(params)
				.then((res) => {
					this.replyOnList = res.rows;
					this.total = res.total;
					if (this.ob) {
						this.ob.unobserve(dom);
						this.ob = null;
					}
					this.replyOnShow = res.total ? true : false;
				})
				.finally(() => {
					this.onLoading = false;
				});
		},
		userInfoHover(e, item) {
			let that = item ? item : this;
			that.showFlag = setInterval(() => {
				this.$set(that, "userDetailsShow", true);
			}, 1000);
		},
		userInfoOut(e, item) {
			let that = item ? item : this;
			this.$set(that, "userDetailsShow", false);
			clearInterval(that.showFlag);
		},
	},
};
</script>

<style lang="less" scoped>
.replyMain {
	padding: 20px 20px 10px;
	&:hover .replyBtn {
		display: flex;
	}
}
.replyItem {
	position: relative;
	border-radius: 6px;
	border: 1px solid #e7eff7;
	z-index: 10;
	.mainTop {
		display: flex;
		align-items: center;
		justify-content: space-between;
		background-color: #e7eff7;
		padding: 6px 16px;
		border-radius: 6px 6px 0 0;
		.releaseTime {
			color: #667f99;
		}
		.userInfo {
			display: flex;
			align-items: center;
			cursor: pointer;
			position: relative;
			& > span {
				margin: 0 4px;
				&:hover {
					text-decoration: underline;
				}
			}

			.userInfoDetails {
				position: absolute;
				left: -120px;
				top: -10px;
				border-radius: 6px;
				display: flex;
				background-color: #7e8892;
				color: #fff;
				min-width: 500px;
				padding: 20px;
				z-index: 20;
				cursor: default;
				.details_right {
					margin-left: 20px;
					text-align: left;
					.d_right_name {
						font-size: 20px;
						cursor: pointer;
						&:hover {
							text-decoration: underline;
						}
					}
					.d_right_times {
						margin: 16px 0 20px;
						display: flex;
						align-items: center;
						i {
							font-size: 14px;
							color: #fff;
							margin-right: 4px;
						}
						span {
							margin-right: 16px;
						}
					}
					.d_right_simple {
						margin-bottom: 20px;
					}
				}
			}
		}
	}
	.mainBtm {
		text-align: left;
		padding: 10px;
		background-color: #fff;
		border-radius: 0 0 6px 6px;
		.mainHtml {
			margin-bottom: 20px;
			/deep/ * {
				max-width: 100%;
				img {
					max-width: 50%;
					height: auto;
				}
			}
		}
		.replyUser {
			display: flex;
			align-items: center;
			i {
				font-size: 16px;
				margin-right: 6px;
			}
			span {
				margin: 0 4px;
			}
		}
		.replyOn_box {
			width: 100%;
			border-radius: 4px;
			background-color: #f7f8fa;
			border: 1px solid #ededed;
			padding-bottom: 10px;
			.replyOnList {
				.onLoading {
					width: 100%;
					text-align: center;
					font-size: 18px;
					padding: 20px 0;
				}
				padding: 10px;
				.replyOnItem {
					margin-top: 10px;
					&:first-child {
						margin: 0;
					}
					.replyOnItem_content {
						// display: flex;
						// align-items: flex-start;
						// justify-content: flex-start;
						.userInfo {
							display: inline-block;
							// display: flex;
							// align-items: center;
							& > .el-avatar {
								vertical-align: top;
							}
							/deep/ .el-link {
								vertical-align: top;
							}
						}
						strong {
							margin-left: 2px;
							margin-right: 8px;
						}
						span {
							margin: 0 2px;
						}
						.replyOn_text {
							flex: 1;
						}
					}
					.replyOn_action {
						height: 20px;
						display: flex;
						align-items: center;
						justify-content: flex-end;
						.el-button,
						span {
							padding: 0;
							margin: 0;
							margin-left: 4px;
							line-height: unset;
						}
					}
				}
			}
			.loadMore {
				div {
					font-size: 12px;
				}
				/deep/ .el-pagination {
					padding: 2px 16px;
					.btn-next,
					.btn-prev {
						background-color: #f7f8fa;
					}
				}
				/deep/ .el-pager li {
					background-color: #f7f8fa;
					line-height: 20px;
				}
			}
			.replayOn_input {
				padding: 10px;
				text-align: right;
			}
		}
	}
	.replyBtn {
		display: none;
		flex-direction: column;
		position: absolute;
		bottom: 6px;
		right: 4px;
		transform: translateX(100%);
		z-index: 10;
		.el-button {
			padding: 4px 0;
			margin-left: 10px;
		}
	}
}
</style>
