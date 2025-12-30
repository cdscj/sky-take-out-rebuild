<template>
	<div class="labelDialog" v-if="labelShow">
		<div class="mask" @click="closelabelMask"></div>
		<div class="label_box">
			<div class="label_top">
				<div class="dialog_header">
					<div class="header_title">选择标签</div>
					<i class="el-icon-close" @click="closelabelMask"></i>
				</div>
				<div class="selected_box">
					<div class="input_bar">
						<div class="labels">
							<div
								:class="{ labelItem: true, child: item.type == 0 }"
								v-for="(item, i) in selectedlabels"
								:key="i"
								@click="labelItemClick(item)"
							>
								<img v-if="item.labelImg" :src="item.labelImg" width="16" style="margin-right: 2px;" alt="">
								<i v-else :class="item.icon"></i>
								{{ item.label }}
							</div>
						</div>
						<!-- <input type="text" v-model="searchText"> -->
					</div>
					<div class="subBtn">
						<el-button type="primary" @click="saveLabels">确定</el-button>
					</div>
				</div>
			</div>
			<div class="label_main">
				<div class="labelList">
					<el-scrollbar>
						<div
							:class="{ labelItem: true, child: item.type == 0 }"
							v-for="(item, i) in labelDomList"
							:key="i"
							@click="labelItemClick(item)"
							v-show="
								item.type != 0 || selectedlabels.findIndex((el) => item.parentId == el.id) > -1
							"
						>
							<template v-if="item.type">
								<img
									v-if="item.labelImg&&selectedlabels.findIndex((el) => item.id == el.id) == -1"
									:src="item.labelImg"
									width="20"
									alt=""
								>
								<i
									v-else
									:class="
											selectedlabels.findIndex((el) => item.id == el.id) > -1
											? 'el-icon-check'
											: item.icon
									"
								></i>
							</template>
							<div class="checkBox" v-else>
								<i
									class="el-icon-check"
									v-if="selectedlabels.findIndex((el) => item.id == el.id) > -1"
								></i>
							</div>
							<div class="labelText">
								<div class="labelText_m">{{ item.label }}</div>
								<div v-if="item.tips">({{ item.tips }})</div>
							</div>
						</div>
					</el-scrollbar>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { getLabelList } from "@/api/main";
export default {
	data() {
		return {
			form: {},
			labelShow: false,
			labeloutShow: false,
			modelType: 1,
			searchText: "",
			labelDomList: [
				// {
				// 	id: 1,
				// 	label: "购物",
				// 	icon: "el-icon-s-goods",
				// 	tips: "今晚去购物",
				// 	type: 1,  // 0为二级
				// },
			],
			selectedlabels: [],
		};
	},

	created() {},

	methods: {
		saveLabels() {
			this.$emit("saveLabels", [...this.selectedlabels]);
			this.closelabelMask();
		},
		labelItemClick(item) {
			let i = this.selectedlabels.findIndex((el) => el.id == item.id);
			if (i > -1) {
				this.selectedlabels = this.selectedlabels.filter(
					(el) => item.id != el.id && item.id != el.parentId
				);
			} else {
				this.selectedlabels.push(item);
			}
		},
		setLabelList() {
			getLabelList({}).then((res) => {
				let mainArr = res.data.map((el) => {
					return {
						id: el.id,
						label: el.labelName,
						icon: el.parentId ? "" : "el-icon-collection-tag",
						// tips: "今晚去购物",
						type: el.parentId ? 0 : 1,
						labelImg: el.labelImg,
						parentId: el.parentId,
					};
				});
				// 排序-二级跟在一级后面
				let arr = [];
				mainArr.filter(el=>!el.parentId).forEach(el=>{
					arr.push({...el});
					arr.push(...mainArr.filter(item=>el.id==item.parentId));
				});
				this.labelDomList = arr;
			});
		},
		show(row) {
			this.setLabelList();
			this.selectedlabels = row ? [...row] : [];
			this.labelShow = true;
			document.body.classList.add("ovh");
		},
		closelabelMask() {
			this.labelShow = false;
			document.body.classList.remove("ovh");
		},
	},
};
</script>

<style lang="less" scoped>
.labelDialog {
	position: fixed;
	top: 0;
	left: 0;
	z-index: 1000;
	.mask {
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		background-color: #336f8f9c;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.label_box {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		width: 600px;
		// height: 500px;
		background-color: #ffffff;
		border-radius: 4px;
		color: #5c5c5c;
		// border: 1px solid #d8d8d8;
		box-shadow: 0 7px 15px #6c6c6c;
		.label_top {
			width: 100%;
			display: flex;
			flex-direction: column;
			align-items: center;
			background-color: #e4edf6;
			padding: 20px;
			.dialog_header {
				width: 100%;
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 20px;
				.header_title {
					font-size: 18px;
				}
				i {
					cursor: pointer;
					font-size: 22px;
					font-weight: 700;
				}
			}
			.selected_box {
				width: 100%;
				display: flex;
				.input_bar {
					flex: 1;
					border: 2px solid #5c5c5c;
					border-radius: 6px;
					padding: 2px 4px;
					min-height: 34px;
					background-color: #fff;
					position: relative;
					text-align: left;
					display: flex;
					flex-wrap: wrap;
					.labels {
						display: flex;
						flex-wrap: wrap;
						.labelItem {
							margin: 4px;
							background-color: #4b93d1;
							color: #fff;
							padding: 2px 6px;
							border-radius: 4px;
							cursor: no-drop;
							display: flex;
							justify-content: center;
							align-items: center;
							&.child {
								color: #667f99;
								background-color: #e4edf6;
							}
						}
					}
					input {
						width: 0ch;
						min-width: 1ch;
						border: 0;
						outline: none;
						font-size: 16px;
						display: inline-block;
						margin: 6px;
					}
				}
				.subBtn {
					margin-left: 16px;
				}
			}
		}
		.label_main {
			text-align: left;
			margin-top: 2px;
			.labelList {
				height: calc(100vh - 460px);
				min-height: 300px;
				/deep/ .el-scrollbar {
					height: 100%;
				}
				/deep/ .el-scrollbar__wrap {
					overflow-x: hidden;
				}
			}
			.labelItem {
				padding: 14px 24px;
				display: flex;
				align-items: center;
				cursor: pointer;
				&:hover {
					background-color: #e4edf6;
				}
				.el-icon-check {
					font-weight: 900;
				}
				.checkBox {
					border-radius: 4px;
					width: 20px;
					height: 20px;
					display: flex;
					justify-content: center;
					align-items: center;
					background-color: #e4edf6;
				}
				&.child {
					padding: 6px;
					padding-left: 60px;
					.labelText .labelText_m {
						font-size: 14px;
					}
				}
				i {
					font-size: 18px;
					margin-right: 2px;
				}
				.labelText {
					display: flex;
					align-items: center;
					margin-left: 10px;

					.labelText_m {
						font-size: 16px;
						margin-right: 10px;
					}
					div {
						display: inline-block;
					}
				}
			}
		}
	}
}
</style>
