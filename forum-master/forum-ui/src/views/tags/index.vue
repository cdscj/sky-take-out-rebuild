<template>
	<div class="tagsPage">
		<div class="pageContent container">
			<div class="actionType">
				<el-button type="primary" style="margin-right: 20px;" @click="openMenuEditor"
					>发布主题</el-button
				>
				<el-button type="text" icon="el-icon-chat-line-round" @click="toIndex()">全部主题</el-button>
				<el-button type="text" icon="el-icon-star-on" v-if="username" @click="toIndex('foll')"
					>我的关注</el-button
				>
				<el-button type="text" icon="el-icon-menu" style="color: #276bb0;">所有标签</el-button>
			</div>
			<div class="tagList">
				<div class="tagItem" @click.stop="labelClick(item)" v-for="(item, i) in labelList" :key="i">
					<div class="mainTitle">
						<img v-if="item.labelImg" :src="item.labelImg" width="30" style="margin-right: 10px;" alt="">
						<i v-else :class="item.icon"></i>
						<span>{{ item.label }}</span>
					</div>
					<div class="mainTips">{{ item.tips }}</div>
					<div class="tagsChildren">
						<div
							class="childrenItem"
							@click.stop="labelClick(el)"
							v-for="(el, j) in item.children"
							:key="j"
						>
							{{ el.label }}
						</div>
					</div>
				</div>
			</div>
		</div>
		<menuEditor ref="menuEditor" @subSuccess="labelClick({})"></menuEditor>
	</div>
</template>

<script>
import { getLabelList } from "@/api/main";
import { mapGetters } from "vuex";
import menuEditor from "@/components/menuEditor/index.vue";
export default {
	components: { menuEditor },
	data() {
		return {
			labelList: [],
		};
	},

	computed: {
		...mapGetters(["username"]),
	},

	created() {
		this.setLabelList();
	},

	methods: {
		openMenuEditor() {
			this.$refs.menuEditor.show();
		},
		labelClick(item) {
			this.$router.push({
				path: "/",
				query: {
					labelId: item.id,
				},
			});
		},
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
				this.labelList = [...arr];
			});
		},
		toIndex(id) {
			this.$router.push({
				path: "/",
				query: { labelId: id },
			});
		},
	},
};
</script>

<style lang="less" scoped>
.tagsPage {
	min-height: calc(100vh - 52px);
	.actionType {
		padding: 30px 0;
		text-align: left;
	}
	.tagList {
		display: flex;
		flex-wrap: wrap;
		border-radius: 4px;
		overflow: hidden;
		.tagItem {
			cursor: pointer;
			width: 33.3333%;
			color: #ffffff;
			padding: 20px 20px 30px;
			text-align: left;
			&:nth-child(1) {
				background-color: #b59e8c;
			}
			&:nth-child(2) {
				background-color: #ef564f;
			}
			&:nth-child(3) {
				background-color: #f3763f;
			}
			&:nth-child(4) {
				background-color: #4b92d0;
			}
			.mainTitle {
				display: flex;
				align-items: center;
				font-size: 22px;
				font-weight: 700;
				i {
					font-size: 28px;
					margin-right: 10px;
				}
			}
			.mainTips {
				margin: 16px 0;
				color: #e9e2dd;
			}
			.childrenItem {
				display: inline-block;
				color: #ffffff;
				margin-right: 16px;
				cursor: pointer;
				&:hover {
					text-decoration: underline;
				}
			}
		}
	}
}
</style>
