<template>
	<div :class="{ editorPage: true, fullScreen: isFullScreen }" v-show="dialogShow">
		<div class="editorHeader">
			<div class="headerLeft">
				<el-avatar :size="80" :src="avatar" v-if="modelType != 3"></el-avatar>
				<el-avatar :size="40" :src="avatar" v-else></el-avatar>
			</div>
			<div class="headerCenter">
				<template v-if="modelType != 3">
					<div class="labels">
						<div
							:class="{ labelItem: true, child: item.type == 0 }"
							v-for="(item, i) in selectLabelList"
							:key="i"
							@click="changeLabelList"
						>
							<img v-if="item.labelImg" :src="item.labelImg" width="18" style="margin-right: 2px;" alt="">
							<i v-else :class="item.icon"></i>
							{{ item.label }}
						</div>
						<div class="addLabel" @click="addLabels" v-if="!selectLabelList.length">选择标签</div>
					</div>
					<div class="menuTitle">
						<el-input v-model="menuTitle" placeholder="标题" style="width: 80%"></el-input>
					</div>
				</template>
				<div class="replyTitle" v-else>回复: {{ topicName }}</div>
			</div>
			<div class="headerRight">
				<i class="el-icon-minus" @click="resetScreen" v-if="isFullScreen"></i>
				<i class="el-icon-full-screen" @click="fullScreen" v-else></i>
				<i class="el-icon-close" @click="closeDialog"></i>
			</div>
		</div>
		<div class="editorMain">
			<Editor
				:id="tinymceId"
				v-model.trim="myValue"
				:init="init"
				v-bind="$attrs"
				v-on="inputListeners"
			>
			</Editor>
		</div>
		<div class="footerBtns">
			<el-button type="primary" :loading="subLoading" @click="subMenu" v-if="modelType != 3"
				>发布</el-button
			>
			<el-button type="primary" :loading="subLoading" @click="subReply" v-else>发布</el-button>
		</div>
		<selectLabels ref="selectLabels" @saveLabels="saveLabels"></selectLabels>
	</div>
</template>
<script>
// 引入组件
import tinymce from "tinymce/tinymce"; // tinymce默认hidden，不引入不显示
import Editor from "@tinymce/tinymce-vue";
// 引入富文本编辑器主题的js和css
import "tinymce/skins/content/default/content.css";
import "tinymce/themes/silver";
import "tinymce/icons/default/icons.min.js"; // 解决了icons.js 报错Unexpected token '<'
// 编辑器插件plugins
// 更多插件参考：https://www.tiny.cloud/docs/plugins/
import "tinymce/plugins/code/plugin.min.js"; // 解决了plugins.js 报错Unexpected token '<'
import "tinymce/plugins/image"; // 插入上传图片插件
import "tinymce/plugins/table"; // 插入表格插件
import "tinymce/plugins/lists"; // 列表插件
import "tinymce/plugins/wordcount"; // 字数统计插件
import "tinymce/plugins/link";
import "tinymce/plugins/preview";
import "tinymce/plugins/help";

import selectLabels from "@/components/selectLabels";
import { topicPublish, topicReply } from "@/api/main";
import { updateAvatar } from "@/api/user";
import { mapGetters } from "vuex";
import { eventBus } from "@/main";
export default {
	inheritAttrs: false,
	components: {
		Editor,
		selectLabels,
	},
	name: "Tinymce",
	props: {
		id: {
			type: String,
			default: function() {
				return new Date().getTime() + "";
			},
		},
		topicId: {
			type: Number,
			default: function() {
				return 0;
			},
		},
		topicName: {
			type: String,
			default: function() {
				return "";
			},
		},
		modelType: {
			type: Number,
			default: function() {
				// 1 发表,2 编辑,3 回复
				return 1;
			},
		},
		plugins: {
			type: [String, Array],
			default: "link lists image code table wordcount preview help",
		},
		toolbar: {
			type: [String, Array],
			default:
				"bold italic underline strikethrough | fontsizeselect | formatselect | forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent blockquote | undo redo | link unlink lists table image media | removeformat | fullscreen preview",
		},
	},
	data() {
		const that = this;
		return {
			menuTitle: "",
			selectLabelList: [],
			dialogShow: false,
			subLoading: false,
			// 自动生成的id
			tinymceId: this.id,
			init: {
				selector: `#${this.tinymceId}`,
				// fixed_toolbar_container: '.editorPage',
				// toolbar_location: "bottom",
				toolbar_mode: "sliding",
				language_url: "/web/static/js/tinymce/langs/zh-Hans.js", // 语言包的路径
				language: "zh-Hans", //语言
				skin_url: "/web/static/js/tinymce/skins/ui/oxide", // skin路径
				content_css: "/web/static/js/tinymce/skins/content/default/content.css", // css路径
				height: "100%", //编辑器高度
				branding: false, //是否禁用“Powered by TinyMCE”
				menubar: false, //顶部菜单栏显示
				statusbar: false, //是否显示底部的状态栏
				plugins: this.plugins,
				toolbar: this.toolbar, // （自定义工具栏）
				// 此处为图片上传处理函数
				images_upload_handler: (blobInfo, success, failure) => {
					let params = new FormData();
					let file = blobInfo.blob();
					params.append("file", file, file.name);
					updateAvatar(params)
						.then((res) => {
							success(res.url);
						})
						.catch(() => {
							failure("上传失败");
						});
				},
				setup: (editor) => {
					// 初次化编辑器
					editor.on("init", () => {
						that.isInit = true; //告知是初始化\
						if (that.value) {
							editor.setContent(that.value);
							that.isInit = false;
						}
						editor.on("input change undo redo", () => {
							// this.value = editor.getContent()
							//此处设置为false很好解决光标前置问题
							that.isInit = false;
						});
					});
				},
			},
			myValue: "",
			isInit: false, //是否初始化
			isFullScreen: false,
		};
	},
	computed: {
		...mapGetters(["avatar", "username"]),
		inputListeners() {
			const newListeners = {
				...this.$listeners,
				input: (event) => {
					this.$emit("input", event);
				},
			};
			return newListeners;
		},
	},
	mounted() {
		// tinymce.init({});
	},
	beforeDestroy: function() {
		tinymce.remove(this.tinymceId);
	},
	methods: {
		subReply() {
			if (!this.myValue) return this.$message.warning("请输入回复内容");
			let params = {
				topicId: this.topicId,
				replyContent: this.myValue,
			};
			this.subLoading = true;
			topicReply(params)
				.then(() => {
					this.$message.success("发布成功");
					this.$emit("subSuccess");
					this.closeDialog();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		subMenu() {
			if (!this.menuTitle) return this.$message.warning("请输入标题");
			if (!this.myValue) return this.$message.warning("请输入内容");
			let params = {
				title: this.menuTitle,
				topicContent: this.myValue,
				labelIds: this.selectLabelList.map((el) => el.id),
			};
			this.subLoading = true;
			topicPublish(params)
				.then(() => {
					this.$message.success("发布成功");
					this.$emit("subSuccess");
					this.closeDialog();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		changeLabelList() {
			this.$refs.selectLabels.show(this.selectLabelList);
		},
		saveLabels(arr) {
			this.selectLabelList = [...arr];
		},
		addLabels() {
			this.$refs.selectLabels.show();
		},
		fullScreen() {
			this.isFullScreen = true;
			document.body.classList.add("ovh");
		},
		resetScreen() {
			this.isFullScreen = false;
			document.body.classList.remove("ovh");
		},
		/**
		 * 获取富文本text类型内容
		 */
		getTextContent() {
			const editor = tinymce.get(this.tinymceId);
			return editor.getContent({ format: "text" });
		},
		show() {
			if (!this.username) {
				eventBus.$emit("needLogin");
				return;
			}
			this.reset();
			this.dialogShow = true;
		},
		reset() {
			this.menuTitle = "";
			this.selectLabelList = [];
			this.myValue = "";
		},
		closeDialog() {
			this.dialogShow = false;
			document.body.classList.remove("ovh");
		},
	},
	watch: {
		value(val) {
			//用户vue绑定回显
			if (this.isInit) {
				this.isInit = false;
				this.$nextTick(() => {
					const editor = tinymce.get(this.tinymceId);
					editor.setContent(val);
				});
			}
		},
	},
};
</script>

<style lang="less" scoped>
.editorPage {
	// width: 50%;
	// min-width: 800px;
	// max-width: 1000px;
	width: 900px;
	position: fixed;
	bottom: 0;
	left: calc(50% - 450px);
	// transform: translateX(-50%);
	background-color: #fff;
	// height: 300px;
	border: 2px solid #409eff;
	border-bottom: none;
	border-radius: 4px 4px 0 0;
	padding: 16px 20px;
	z-index: 101;
	.editorHeader {
		display: flex;
		justify-content: space-between;
		margin-bottom: 10px;
		.headerLeft {
			margin-right: 10px;
		}
		.headerCenter {
			flex: 1;
			.replyTitle {
				height: 100%;
				display: flex;
				align-items: center;
				text-align: left;
				font-size: 20px;
				// white-space: nowrap;
				// overflow: hidden;
				// text-overflow: ellipsis;
			}
			.labels {
				text-align: left;
				margin-bottom: 10px;
				display: flex;
				flex-wrap: wrap;
				.labelItem {
					margin: 4px;
					background-color: #4b93d1;
					color: #fff;
					padding: 2px 6px;
					border-radius: 4px;
					cursor: pointer;
					display: flex;
					justify-content: center;
					align-items: center;
					&.child {
						color: #667f99;
						background-color: #e4edf6;
					}
				}
				.addLabel {
					padding: 2px 6px;
					border: 1px dashed #409eff;
					cursor: pointer;
					border-radius: 4px;
					display: inline-block;
					color: #409eff;
					&:hover {
						background-color: #409eff;
						color: #fff;
					}
				}
			}
			.menuTitle {
				text-align: left;
			}
		}
		.headerRight {
			font-size: 20px;
			i {
				font-weight: 900;
				margin: 0 6px;
				cursor: pointer;
				&:hover {
					color: #409eff;
				}
			}
		}
	}
	.editorMain {
		// padding-left: 90px;
		// padding-right: 65px;
		height: 200px;
		/deep/ .tox-tinymce {
			border: none;
			border-bottom: 1px solid #ccc;
			// border-bottom: 1px solid #409eff;
			// .tox-toolbar__primary{
			// 	background-color: #e2f0ff;
			// }
			.tox-toolbar__group {
				border: none;
			}
			// .tox-toolbar__primary{
			// 	position: relative;
			// 	&::after{
			// 		content: "";
			// 		width: 100%;
			// 		height: 1px;
			// 		background-color: #409eff;
			// 		position: absolute;
			// 		left: 0;
			// 		bottom: 0;
			// 	}
			// }
		}
	}
	.footerBtns {
		// position: relative;
		text-align: right;
		padding-top: 10px;
	}
}
// 全屏
.fullScreen {
	// width: 100%;
	height: 100vh;
	// max-width: 1200px;
	width: 1200px;
	left: calc(50% - 600px);
	z-index: 101;
	.editorMain {
		height: calc(100% - 140px);
	}
}
</style>
