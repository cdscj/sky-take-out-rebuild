<template>
	<div class="indexPage">
		<div class="selectedLabel" v-if="selectedLabel.id">
			<div class="labelContent">
				<img v-if="selectedLabel.labelImg" :src="selectedLabel.labelImg" width="30" style="margin-right: 10px;" alt="">
				<i v-else :class="selectedLabel.icon" style="margin-right: 10px;"></i>
				{{ selectedLabel.label }}
			</div>
			<div class="labelTips" v-if="selectedLabel.tips">{{ selectedLabel.tips }}</div>
		</div>
		<div class="indexContent container">
			<div class="leftSlide">
				<el-button type="primary" @click="openMenuEditor">发布主题</el-button>
				<div class="labelContent">
					<div class="labelList" v-for="(list, i) in labelList" :key="i">
						<div class="labelItem" v-for="(el, j) in list" :key="j">
							<div
								:class="{ itemText: true, active: selectedLabel.id == el.id }"
								@click="labelClick(el, list)"
								v-if="el.id != 'foll' || username"
							>
								<img v-if="el.labelImg" :src="el.labelImg" width="18" style="margin-right: 8px;" alt="">
								<i v-else :class="el.icon"></i>
								{{ el.label }}
							</div>
							<div
								class="Labels_level2"
								v-if="
									(el.children && el.childrenShow) ||
										selectedLabel.parentId == el.id ||
										selectedLabel.id == el.id
								"
							>
								<div class="level2LabelItem" v-for="(s, k) in el.children" :key="k">
									<div
										:class="{ itemText: true, active: selectedLabel.id == s.id }"
										@click="labelClick(s)"
									>
										{{ s.label }}
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="rightContent" v-loading="themeLoading">
				<el-dropdown @command="sortClick">
					<el-button> {{ sortLabel }}<i class="el-icon-arrow-down el-icon--right"></i> </el-button>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item v-for="(item, i) in sortList" :key="i" :command="item.id">
							{{ item.label }}
						</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
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
										<img v-if="el.labelImg" :src="el.labelImg" width="16" style="margin-right: 2px;" alt="">
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
					<!-- <el-button type="info" size="small">加载更多</el-button> -->
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
		</div>
		<menuEditor ref="menuEditor" @subSuccess="search"></menuEditor>
	</div>
</template>

<script>
import menuEditor from "@/components/menuEditor/index.vue";
import { getTopicList, getLabelList } from "@/api/main";
import { mapGetters } from "vuex";
import { getPlainText } from "@/utils/auth";
export default {
	name: "Index",
	components: { menuEditor },
	inject: ["reload"],
	data() {
		return {
			layout: "total, prev, pager, next",
			formData: {
				orderIn: 1,
				pageNum: 1,
				pageSize: 20,
			},
			total: 0,
			selectedLabel: {},
			themeLoading: false,
			sortLabel: "最新回复",
			sortList: [
				{
					id: "",
					label: "最新回复",
				},
				{
					id: 2,
					label: "热门主题",
				},
				{
					id: 3,
					label: "新鲜出炉",
				},
				{
					id: 4,
					label: "精华",
				},
			],
			baseLabels: [
				{
					icon: "el-icon-chat-line-round",
					label: "全部主题",
				},
				{
					id: "foll",
					icon: "el-icon-star-on",
					label: "我的关注",
					// tips: "我自己点的关注",
				},
			],
			labelList: [],
			themeList: [],
		};
	},

	computed: {
		...mapGetters(["username"]),
	},

	watch: {
		$route: {
			handler() {
				this.setLabelList();
				this.search();
			},
			immediate: false,
		},
	},

	created() {
		// 回显排序
		let query = this.$route.query;
		if (query.sortId) {
			this.formData.orderIn = query.sortId;
			this.sortLabel = this.sortList.find((el) => el.id == query.sortId).label;
		}
		this.setLabelList();
		this.search();
	},

	methods: {
		setLabelList() {
			getLabelList({}).then((res) => {
				let arr = [];
				let child = [];
				res.data.forEach((el) => {
					if (!el.parentId) {
						child = res.data.filter((v) => v.parentId == el.id);
						child = child.map((v) => {
							return {
								id: v.id,
								label: v.labelName,
								parentId: v.parentId,
							};
						});
						arr.push({
							id: el.id,
							label: el.labelName,
							icon: "el-icon-collection-tag",
							parentId: el.parentId,
							labelImg: el.labelImg,
							children: [...child],
						});
					}
				});
				arr.push({ label: "更多标签...", id: -1 });
				this.labelList = [[...this.baseLabels], arr];
				// 回显标签选择
				this.echoLabelSelect(res.data);
			});
		},
		echoLabelSelect(arr) {
			// 回显标签选择
			let query = this.$route.query;
			this.selectedLabel = {};
			if (query.labelId) {
				if (query.labelId == "foll") {
					this.selectedLabel = {
						id: "foll",
						icon: "el-icon-star-on",
						label: "我的关注",
						// tips: "我自己点的关注",
					};
					return;
				}
				let obj = arr.find((el) => el.id == query.labelId);
				this.selectedLabel = {
					...obj,
					icon: obj.parentId ? "" : "el-icon-collection-tag",
					label: obj.labelName,
				};
			}
		},
		openMenuEditor() {
			this.$refs.menuEditor.show();
		},
		search() {
			this.themeLoading = true;
			let params = { ...this.formData };
			params.labelId = this.$route.query.labelId;
			params.name = this.$route.query.q;
			if (params.labelId == "foll") {
				// 我的关注
				params.labelId = undefined;
				params.type = 1;
			}
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
		labelClick(el, list) {
			if (el.label == "更多标签...") {
				this.$router.push({
					path: "/tags",
				});
				return;
			}
			// if (el.label == "全部主题") {
			// 	this.$router
			// 		.push(
			// 			{
			// 				path: "/",
			// 			}
			// 			// () => {
			// 			// 	this.reload();
			// 			// }
			// 		)
			// 		.catch(() => {});
			// 	return;
			// }
			// 显示二级
			if (list) {
				list.forEach((v) => this.$set(v, "childrenShow", false));
				this.$set(el, "childrenShow", true);
			}
			this.selectedLabel = el;
			let query = this.$route.query;
			this.$router
				.push(
					{
						path: "/",
						query: { ...query, labelId: el.id },
					}
					// () => {
					// 	this.reload();
					// }
				)
				.catch(() => {});
		},
		itemClick(item) {
			this.$router.push({
				path: "/d",
				query: { id: item.id },
			});
		},
		sortClick(val) {
			this.formData.orderIn = val;
			this.sortLabel = this.sortList.find((el) => el.id == val).label;
			let query = this.$route.query;
			this.$router
				.push(
					{
						path: "/",
						query: { ...query, sortId: val || undefined },
					}
					// () => {
					// 	this.reload();
					// }
				)
				.catch(() => {});
		},
	},
};
</script>

<style lang="less" scoped>
.indexPage {
	min-height: calc(100vh - 172px);
	.selectedLabel {
		width: 100%;
		padding: 40px 0;
		background-color: #667f99;
		color: #fff;
		.labelContent {
			font-size: 24px;
			display: flex;
			align-items: center;
			justify-content: center;
			i{
				font-size: 32px;
			}
		}
		.labelTips {
			margin-top: 20px;
			font-size: 16px;
		}
	}
	.indexContent {
		display: flex;
		padding: 35px 0;
		.leftSlide {
			margin-right: 35px;
			.labelContent {
				margin-top: 30px;
				.labelList {
					margin-bottom: 14px;
					.labelItem {
						.itemText {
							display: flex;
							align-items: center;
							padding: 10px 0;
							cursor: pointer;
							&:hover {
								color: #409eff;
							}
							i {
								margin-right: 8px;
								font-size: 18px;
							}
							div {
								color: #667f99;
							}
							&.active {
								color: #409eff;
								font-weight: 700;
								& > i {
									font-weight: 700;
								}
							}
						}
						.Labels_level2 {
							padding-left: 36px;
						}
					}
				}
			}
		}
		.rightContent {
			flex: 1;
			text-align: left;
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
		}
	}
}
</style>
