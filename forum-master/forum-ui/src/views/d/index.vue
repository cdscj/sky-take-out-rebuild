<template>
	<div class="detailPage">
		<div class="pageHead">
			<div class="container themeTitle">
				<h2>
					<i class="el-icon-star-on" v-if="topicData.follow"></i>
					{{ topicData.title }}
				</h2>
				<div class="themeLabels">
					<div
						v-for="(item, i) in topicData.topicLabelVoList"
						:key="i"
						:class="{ parentItem: !item.parentId, childItem: item.parentId }"
						@click="labelClick(item)"
					>
						<template v-if="!item.parentId">
							<img v-if="item.labelImg" :src="item.labelImg" width="20" style="margin-right: 4px;" alt="">
							<i v-else class="el-icon-collection-tag"></i>
						</template>
						<span>{{ item.labelName }}</span>
					</div>
				</div>
			</div>
		</div>
		<div class="pageContent container">
			<div class="contentLeft">
				<div class="themeMain_content">
					<div class="themeMain">
						<div class="mainTop">
							<!-- <div class="editHis">
								<i class="el-icon-time"></i>
								<span>已编辑</span>
							</div> -->
							<el-popover trigger="click">
								<div class="editHis_info">
									<div class="editHis_info_title">
										编辑了{{ topicData.versionVos.length }}次,排序从新到旧
									</div>
									<div class="editHis_item" v-for="(item, i) in topicData.versionVos" :key="i">
										<div class="editHis_item_userInfo">
											<el-avatar :size="26" :src="item.avatar"></el-avatar>
											<span>{{ item.nickName }}</span>
										</div>
										<span>编辑于 {{ item.createTime }}</span>
									</div>
								</div>

								<div class="editHis" slot="reference">
									<i class="el-icon-time"></i>
									<span>已编辑</span>
								</div>
							</el-popover>
							<div class="releaseTime">{{ topicData.updateTime }}</div>
							<div class="userInfo" @mouseenter="userInfoHover" @mouseleave="userInfoOut">
								<el-avatar :size="26" :src="topicData.createUser.avatar"></el-avatar>
								<span>{{ topicData.createUser.nickName }}</span>
								<!-- <i class="el-icon-s-check"></i> -->
								<transition name="el-fade-in">
									<div class="userInfoDetails" v-show="userDetailsShow">
										<div class="details_left">
											<el-avatar :size="100" :src="topicData.createUser.avatar"></el-avatar>
										</div>
										<div class="details_right">
											<div class="d_right_name">{{ topicData.createUser.nickName }}</div>
											<div class="d_right_times">
												<span
													><i class="el-icon-time"></i>{{ topicData.createUser.loginDate }}</span
												>
												<span>注册于 {{ topicData.createUser.createTime }}</span>
											</div>
											<div class="d_right_simple">{{ topicData.createUser.remark }}</div>
										</div>
									</div>
								</transition>
							</div>
						</div>
						<div class="mainBtm">
							<div class="mainHtml" v-html="topicData.topicContent"></div>
							<!-- <div class="mainHtml">
								<p>
									我不敢苟同他的观点，我个人认为这个意大利面就应该拌42号混凝土，因为这个螺丝钉的长度，它很容易会直接影响到挖掘机的扭矩，你知道吧?你往里砸的时候，一瞬间它就会产生大量的高能蛋白，俗称UFO，会严重影响经济的发展，甚至对这个太平洋以及充电器都会造成一定的核污染。
								</p>
							</div> -->
							<!-- <div class="replyUser">
								<i class="el-icon-share"></i>
								<el-link>土拨鼠</el-link>
								<span>和</span>
								<el-link>小叮当</el-link>
								<span>回复了此贴</span>
							</div> -->
						</div>
						<div class="replyBtn">
							<el-button type="text" @click="replyTopic">回复</el-button>
							<el-button type="text" v-if="topicData.praise" @click="unPraiseReply"
								>取消赞</el-button
							>
							<el-button type="text" v-else @click="setPraiseReply({ topicId: rowId })"
								>赞</el-button
							>
							<el-button type="text" @click="openReport({ topicId: rowId })">举报</el-button>
						</div>
					</div>
				</div>
				<div class="reply_content">
					<div class="replyList">
						<replyItem
							v-for="(item, i) in replyList"
							:key="i"
							:data="item"
							:rowId="+rowId"
							@openReport="openReport"
							@setPraiseReply="setPraiseReply"
							@unPraiseReply="unPraiseReply"
						></replyItem>
						<div ref="moreLoading" v-if="replyList.length!=total">加载中...</div>
						<div class="reply_area" @click="replyTopic">说点什么吧...</div>
					</div>
				</div>
			</div>
			<div class="contentRight">
				<div class="actionBtns">
					<el-button type="primary" @click="replyTopic">回复</el-button>
					<template v-if="username">
						<el-button
							v-if="topicData.follow"
							type="warning"
							style="margin-top: 10px;"
							icon="el-icon-star-on"
							@click="topicUnfollow"
							>已关注</el-button
						>
						<el-button v-else style="margin-top: 10px;" icon="el-icon-star-off" @click="topicFollow"
							>关注</el-button
						>
					</template>
				</div>
				<div class="scrubber">
					<div class="poreScrubber">
						<div class="dropMenu">
							<div class="dropMenu_first" @click="scrollToTop">
								<i class="el-icon-caret-top"></i>
								<span>最早内容</span>
							</div>
							<div class="dropMenu_scrollbar" ref="dropMenu_scrollbar" @click.self="clickMenu">
								<div class="scrollHeadle" ref="scrollHeadle" @mousedown.prevent="dropSlideBar">
									<div class="scroll_slideBar"></div>
									<div class="scroll_info">
										<strong>{{ nowIndex }}/{{ total + 1 }}条</strong>
										<div>{{ slideTime }}</div>
									</div>
								</div>
							</div>
							<div class="dropMenu_last" @click="scrollToBtm">
								<i class="el-icon-caret-bottom"></i>
								<span>最新回复</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<reportDialog ref="reportDialog"></reportDialog>
		<menuEditor
			ref="menuEditor"
			:topicId="+rowId"
			:toolbar="toolbar"
			:modelType="3"
			:topicName="topicData.title"
			@subSuccess="subSuccess"
		></menuEditor>
	</div>
</template>

<script>
import { scrollTo } from "@/utils/scroll-to.js";
import { getTopicDetails, getMainReply, topicFollow, topicUnfollow, praiseReply } from "@/api/main";
import replyItem from "@/components/replyItem/index.vue";
import reportDialog from "@/components/reportDialog/index.vue";
import menuEditor from "@/components/menuEditor/index.vue";
import { mapGetters } from "vuex";
import { eventBus } from "@/main";
export default {
	components: { replyItem, reportDialog, menuEditor },
	inject: ['reload'],
	data() {
		return {
			isEdit: false,
			userDetailsShow: false,
			dropScroll: false,
			showFlag: null,
			slideTime: null,
			nowIndex: 1,
			moreLoadingOB: null,
			loading: false,
			total: 1,
			pageNum: 1,
			rowId: 0,
			topicData: {
				versionVos: [],
				createUser: {},
			},
			userAvater: require("@/assets/u111.png"),
			replyList: [],
			toolbar:
				"bold italic underline strikethrough | forecolor | alignleft aligncenter alignright alignjustify | bullist numlist blockquote | undo redo | link unlink lists table image media | removeformat | fullscreen preview",
		};
	},

	watch: {
		$route: {
			handler() {
				this.reload();
			},
			immediate: false,
		},
	},

	computed: {
		...mapGetters(["username"]),
	},
	created() {
		this.rowId = this.$route.query.id || 0;
		this.getDetails();
		this.getMainReply();
	},
	mounted() {
		this.setLoadingMore();
		// 页面滚动监听
		document.onscroll = () => {
			let doms = document.querySelectorAll(".replyMain");
			let gap = 0;
			doms.forEach((el, i) => {
				// 元素与顶部的距离
				gap = el.getBoundingClientRect().top;
				if (!i && gap > 100) {
					// 主题
					this.nowIndex = 1;
					this.slideTime = this.getTime(this.topicData.updateTime);
					return;
				}
				if (gap <= 100) {
					// +2是因为主题算一条
					this.nowIndex = i + 2;
					this.slideTime = this.getTime(this.replyList[i].updateTime);
				}
			});
			if (this.dropScroll) return;
			let dom = this.$refs.scrollHeadle;
			// 滑块最大滚动高度
			let maxTop = 300 - dom.clientHeight;
			// 文档滚动高度
			let scrollTop = document.documentElement.scrollTop;
			// top = 文档滚动高度 * 滑块最大滚动高度 / 文档最大滚动高度(文档高度-视口高度)
			let top = (scrollTop * maxTop) / (document.body.clientHeight - window.innerHeight);
			dom.style.top = top + "px";
		};
	},
	beforeDestroy() {
		document.onscroll = null;
	},
	methods: {
		// 设置加载更多
		setLoadingMore(){
			let dom = this.$refs.moreLoading;
			this.moreLoadingOB = new IntersectionObserver(
				async (ent) => {
					if (!ent[0].isIntersecting || this.loading) return;
					await this.moreLoading();
				},
				{
					root: null,
					threshold: 0,
				}
			);
			this.moreLoadingOB.observe(dom);
		},
		// 加载更多
		moreLoading(){
			this.pageNum++;
			this.getMainReply();
		},
		// 回复成功
		subSuccess(){
			// 翻页加载
			if(this.total % 10 == 0) return this.moreLoading();
			// 当前页加载最后一条
			this.getMainReply(true);
		},
		scrollToTop() {
			scrollTo(0);
		},
		scrollToBtm() {
			scrollTo(document.body.clientHeight);
		},
		labelClick(item) {
			this.$router.push({
				path: "/",
				query: { labelId: item.labelId },
			});
		},
		replyTopic() {
			this.$refs.menuEditor.show();
		},
		setPraiseReply(data, cb) {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			let params = {
				topicId: data.topicId,
				replyId: data.id || undefined,
			};
			praiseReply(params).then(() => {
				if (!cb) this.$set(this.topicData, "praise", 1);
				cb && cb();
			});
		},
		unPraiseReply() {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
		},
		openReport(data) {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			this.$refs.reportDialog.show(data);
		},
		topicFollow() {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			topicFollow({ topicId: this.rowId }).then(() => {
				this.topicData.follow = 1;
			});
		},
		topicUnfollow() {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			topicUnfollow({ topicId: this.rowId }).then(() => {
				this.topicData.follow = 0;
			});
		},
		getMainReply(getLast) {
			this.loading = true;
			getMainReply({ topicId: this.rowId, pageSize: 10, pageNum: this.pageNum }).then((res) => {
				let data = res.rows;
				// 当前页获取最新一条
				if(getLast) data = [res.rows[res.rows.length - 1]];
				this.replyList.push(...data);
				this.total = res.total;
				this.$refs.scrollHeadle.style.height = this.replyList.length ? "16%" : "100%";
				// 滚动+1以刷新右边滑块位置
				scrollTo(document.documentElement.scrollTop+1);
			}).finally(()=> this.loading = false);
		},
		getDetails() {
			getTopicDetails({ topicId: this.rowId }).then((res) => {
				this.topicData = res.data;
				if (!this.slideTime) this.slideTime = this.getTime(res.data.updateTime);
			});
		},
		// 格式化日期的年月
		getTime(time) {
			let date = new Date(time);
			let month = date.getMonth() + 1;
			return `${date.getFullYear()}-${month < 10 ? "0" + month : month}`;
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
		// 轨道点击事件
		clickMenu(e) {
			let domB = this.$refs.dropMenu_scrollbar;
			let domH = this.$refs.scrollHeadle;
			let rect = domB.getBoundingClientRect();
			// top = 点击位置 - 元素距离顶部距离 - 滑块一半高度
			let top = e.clientY - rect.top - domH.clientHeight / 2;
			// 算出top最大值 轨道高度(300)-滑块高度
			let maxTop = 300 - domH.clientHeight;
			if (top > maxTop) top = maxTop;
			if (top < 0) top = 0;
			//滚动页面
			scrollTo((top * (document.body.clientHeight - window.innerHeight)) / maxTop);
		},
		// 滑块拖拽事件
		dropSlideBar(e) {
			if (e === null) return;
			e.preventDefault(); // 禁止默认行为
			e.stopPropagation();
			let dom = this.$refs.scrollHeadle; //获取滑块元素
			//算出鼠标相对元素的位置
			let disY = e.clientY - dom.offsetTop;
			document.onmousemove = (s) => {
				s.preventDefault(); // 禁止默认行为
				s.stopPropagation();
				//鼠标按下并移动的事件
				//用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
				let top = s.clientY - disY;
				// 算出top最大值 轨道高度(300)-滑块高度
				let maxTop = 300 - dom.clientHeight;
				if (top > maxTop || top < 0) return;
				//移动元素
				dom.style.top = top + "px";
				this.setSlideMsg(top, maxTop);
			};
			document.onmouseup = (s) => {
				document.onmousemove = null;
				document.onmouseup = null;
				let top = s.clientY - disY;
				// 算出top最大值 轨道高度(300)-滑块高度
				let maxTop = 300 - dom.clientHeight;
				if (top > maxTop) top = maxTop;
				if (top < 0) top = 0;
				// 得出页面滚动高度
				this.dropScroll = true;
				let duration = 500; // 滚动时间
				// top(滑块滚动高度)*页面最大滚动高度(文档高度-视口高度) / 滑块最大滚动高度
				scrollTo((top * (document.body.clientHeight - window.innerHeight)) / maxTop, duration);
				setTimeout(() => {
					this.dropScroll = false;
				}, duration);
			};
			return false;
		},
		// 拖动滑块时更新滑块信息
		setSlideMsg(top, maxTop) {
			let doms = document.querySelectorAll(".replyMain");
			let arr = [];
			// 获取所有主评论距文档顶部高度
			doms.forEach((el) => arr.push(el.getBoundingClientRect().top + window.pageYOffset));
			let gap = 0;
			arr.forEach((el, i) => {
				// 元素在滑块相对位置 = (元素本身距顶高度 * 滑块最大滚动高度) / 页面最大滚动高度(文档高度-视口高度)
				gap = (el * maxTop) / (document.body.clientHeight - window.innerHeight);
				if (!i && top < gap) {
					// 主题
					this.nowIndex = 1;
					this.slideTime = this.getTime(this.topicData.updateTime);
				}
				if (i<=this.replyList.length-1&&top + 6 >= gap) {
					this.nowIndex = i + 2;
					this.slideTime = this.getTime(this.replyList[i].updateTime);
				}
			});
		},
	},
};
</script>

<style lang="less" scoped>
.detailPage {
	min-height: calc(100vh - 172px);
	.pageHead {
		width: 100%;
		background-color: #e4edf6;
		.themeTitle {
			display: flex;
			align-items: center;
			justify-content: space-between;
			flex-wrap: wrap;
			padding: 20px 10px;
			h2 {
				font-size: 22px;
				margin: 10px 0;
				i {
					font-size: 24px;
					color: #de8e00;
					margin-right: 6px;
				}
			}
			.themeLabels {
				display: flex;
				div {
					padding: 4px 8px;
					border-radius: 4px;
					background-color: #fff;
					margin: 0 2px;
					cursor: pointer;
					display: flex;
					justify-content: center;
					align-items: center;
					&.parentItem {
						background-color: #626c78;
						color: #fff;
					}
					&.childItem {
						background-color: #e4edf6;
						color: #667f99;
					}
					i {
						margin-right: 4px;
					}
				}
			}
		}
	}
	.pageContent {
		margin-top: 10px;
		display: grid;
		grid-gap: 70px;
		grid-template-columns: 1fr 150px;
		grid-template-areas: "stream nav";
		.contentLeft {
			position: relative;
			grid-area: stream;
			max-width: 100%;
			min-width: 0;
			padding: 10px 0;
			&::after {
				content: " ";
				position: absolute;
				top: 36px;
				left: 56px;
				// transform: translateY(-50%);
				width: 2px;
				height: calc(100% - 100px);
				background-color: #e4edf6;
			}
			.themeMain_content {
				padding: 20px 20px 10px;
				&:hover .replyBtn {
					display: flex;
				}
			}
			.themeMain {
				position: relative;
				border-radius: 6px;
				border: 1px solid #e7eff7;
				z-index: 10;
				.mainTop {
					display: flex;
					align-items: center;
					justify-content: space-between;
					background-color: #ffe9cd;
					padding: 6px 16px;
					border-radius: 6px 6px 0 0;
					.editHis {
						display: flex;
						align-items: center;
						cursor: pointer;
						i {
							font-size: 20px;
							margin-right: 6px;
						}
						& > span {
							&:hover {
								text-decoration: underline;
							}
						}
					}
					.releaseTime {
						color: #000000;
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
						i {
							color: #b72a2a;
							font-size: 20px;
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
			.reply_content {
				margin-top: 10px;
				.replyList {
					min-height: 400px;
					position: relative;
					padding-bottom: 300px;
				}
				.reply_area {
					position: absolute;
					z-index: 10;
					width: 100%;
					height: 200px;
					padding: 20px;
					left: 0;
					bottom: 0;
					border: 1px dashed #ccc;
					color: #cfcfcf;
					background-color: #fff;
					border-radius: 6px;
					text-align: left;
					cursor: pointer;
					margin-bottom: 20px;
				}
			}
		}
		.contentRight {
			align-self: start;
			position: sticky;
			grid-area: nav;
			top: 60px;
			padding-top: 32px;
			z-index: 1;
			.actionBtns {
				display: flex;
				flex-direction: column;
				.el-button {
					margin-left: 0;
					width: 100%;
				}
			}
			.scrubber {
				margin-bottom: 10px;
				.poreScrubber {
					position: relative;
					margin: 30px 0;
					.dropMenu {
						width: auto;
						position: static;
						text-align: left;

						.dropMenu_first,
						.dropMenu_last {
							cursor: pointer;
							display: inline-block;
							&:hover {
								color: #409eff;
							}
							i {
								font-size: 16px;
								margin-right: 4px;
							}
						}
						.dropMenu_scrollbar {
							margin: 8px 0 8px 3px;
							height: 300px;
							min-height: 50px;
							position: relative;
							cursor: pointer;
							-webkit-user-select: none;
							user-select: none;
							&::after {
								content: "";
								width: 1px;
								height: 100%;
								position: absolute;
								top: 0;
								left: 5px;
								border-left: 1px solid #e4edf6;
							}
							.scrollHeadle {
								position: relative;
								left: 0;
								top: 0;
								z-index: 1;
								background: transparent;
								width: 100%;
								height: 16%;
								// padding: 5px 0;
								cursor: move;
								// transition: top .1s;
								.scroll_slideBar {
									height: 100%;
									width: 5px;
									background: #409eff;
									border-radius: 4px;
									float: left;
									margin-left: 3px;
								}
								.scroll_info {
									margin-top: -20px;
									position: absolute;
									top: 50%;
									left: 26px;
								}
							}
						}
					}
				}
			}
		}
	}
}
</style>
