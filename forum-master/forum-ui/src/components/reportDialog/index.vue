<template>
	<div class="reportDialog" v-if="reportShow">
		<div class="mask" @click="closereportMask"></div>
		<div class="report_box">
			<div class="report_right_qr">
				<div class="closeBtn">
					<i class="el-icon-close" @click="closereportMask"></i>
				</div>
				<div class="reportType_title">
					<span>举报内容</span>
				</div>
				<div class="report_form">
					<div class="formItem">
						<span>举报原因</span>
						<el-input
							type="textarea"
							v-model="form.reportExplain"
							:rows="4"
							placeholder="请输入原因"
							show-word-limit
							maxlength="200"
						/>
					</div>
					<div class="reportSubmit" @click="reportSubmit">
						<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
						提交
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { reportReply } from "@/api/main";
export default {
	data() {
		return {
			form: {},
			reportShow: false,
			subLoading: false,
			dataInfo: {},
		};
	},

	mounted() {},

	// beforeDestroy() {
	// 	document.onkeydown = null;
	// },

	methods: {
		reportSubmit() {
			let params = {
				replyId: this.dataInfo.id || undefined,
				topicId: this.dataInfo.topicId,
				reportExplain: this.form.reportExplain,
			};
			if (!params.reportExplain) return this.$message.warning("请输入举报原因");
			this.subLoading = true;
			reportReply(params)
				.then(() => {
					this.$message.success("举报成功");
					this.closereportMask();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		show(info) {
			this.dataInfo = { ...info };
			this.form = {};
			this.reportShow = true;
			document.body.classList.add("ovh");
			// document.onkeydown = (e) => {
			// 	let ev = document.all ? window.event : e;
			// 	if (ev.keyCode == 13) this.reportSubmit();
			// };
		},
		closereportMask() {
			this.reportShow = false;
			document.body.classList.remove("ovh");
			// document.onkeydown = null;
		},
	},
};
</script>

<style lang="less" scoped>
.reportDialog {
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
	.report_box {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		width: 450px;
		// height: 500px;
		background-color: #ffffff;
		border-radius: 4px;
		display: flex;
		color: #5c5c5c;
		// border: 1px solid #d8d8d8;
		box-shadow: 0 7px 15px #6c6c6c;
		padding-bottom: 60px;
		.report_right_qr {
			width: 450px;
			display: flex;
			flex-direction: column;
			align-items: center;
			.closeBtn {
				width: 100%;
				text-align: right;
				font-size: 22px;
				padding: 4px 10px;
				font-weight: 700;
				box-sizing: border-box;
				i {
					cursor: pointer;
				}
			}
			.reportType_title {
				padding: 10px 0;
				font-size: 26px;
				vertical-align: middle;
				i {
					color: #15c385;
					margin-right: 10px;
					font-size: 30px;
					vertical-align: middle;
				}
			}
			.report_form {
				padding: 20px 0 10px;
				.formItem {
					width: 350px;
					display: flex;
					flex-direction: column;
					align-items: flex-start;
					margin-bottom: 20px;
					span {
						margin-bottom: 10px;
					}
				}
				.reportSubmit {
					margin-top: 30px;
					width: 100%;
					box-sizing: border-box;
					background-color: #15c385;
					color: #ffffff;
					border-radius: 4px;
					padding: 10px 0;
					cursor: pointer;
					user-select: none;
					&:active {
						background-color: #17db97;
					}
				}
			}
		}
	}
}
</style>
