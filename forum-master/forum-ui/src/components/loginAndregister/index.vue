<template>
	<div class="loginDialog" v-if="loginShow">
		<div class="mask" @click="closeLoginMask"></div>
		<div class="login_box">
			<div class="login_right_qr">
				<div class="closeBtn">
					<i class="el-icon-close" @click="closeLoginMask"></i>
				</div>
				<template v-if="modelType == 1">
					<div class="loginType_title">
						<i class="el-icon-mobile-phone"></i>
						<span>手机号/邮箱登录</span>
					</div>
					<div class="login_form">
						<div class="formItem">
							<span>手机号/邮箱</span>
							<input type="text" v-model="form.username" />
						</div>
						<div class="formItem">
							<span>密码</span>
							<input type="password" v-model="form.password" />
						</div>
						<div class="loginSubmit" @click="loginSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							登录
						</div>
					</div>
					<div class="changeType">
						<div><span @click="changeType(3)">忘记密码?</span></div>
						<div>还没有账户? <span @click="changeType(2)">立即注册</span></div>
					</div>
				</template>
				<template v-if="modelType == 2">
					<div class="loginType_title">
						<i class="el-icon-mobile-phone"></i>
						<span>注册</span>
					</div>
					<div class="login_form">
						<div class="formItem">
							<span>手机号/邮箱</span>
							<input type="text" v-model="form.username" />
						</div>
						<div class="formItem">
							<span>密码</span>
							<div class="code_box">
								<input ref="psd" :type="inputType" v-model="form.password" />
								<i class="el-icon-view" @mousedown.prevent="psdMouseDown" v-if="form.password"></i>
							</div>
						</div>
						<div class="formItem">
							<span>验证码</span>
							<div class="code_box">
								<input type="text" v-model="form.code" />
								<div
									:class="{ getCode: true, disab: getCodeTime > 0 ? true : false }"
									@click="getSms"
								>
									<i class="el-icon-loading" style="margin-right: 4px;" v-if="smsLoading"></i>
									{{ getCodeTime > 0 ? getCodeTime + "秒后获取" : "获取验证码" }}
								</div>
							</div>
						</div>
						<div class="loginSubmit" @click="registerSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							提交
						</div>
					</div>
					<div class="changeType">
						<div>已有账号?<span @click="changeType(1)">去登录</span></div>
					</div>
				</template>
				<template v-if="modelType == 3">
					<div class="loginType_title">
						<i class="el-icon-mobile-phone"></i>
						<span>重置密码</span>
					</div>
					<div class="login_form">
						<div class="formItem">
							<span>手机号/邮箱</span>
							<input type="text" v-model="form.username" />
						</div>
						<div class="formItem">
							<span>新密码</span>
							<div class="code_box">
								<input ref="psd" :type="inputType" v-model="form.password" />
								<i class="el-icon-view" @mousedown.prevent="psdMouseDown" v-if="form.password"></i>
							</div>
						</div>
						<div class="formItem">
							<span>验证码</span>
							<div class="code_box">
								<input type="text" v-model="form.code" />
								<div
									:class="{ getCode: true, disab: getCodeTime > 0 ? true : false }"
									@click="getSms"
								>
									<i class="el-icon-loading" style="margin-right: 4px;" v-if="smsLoading"></i>
									{{ getCodeTime > 0 ? getCodeTime + "秒后获取" : "获取验证码" }}
								</div>
							</div>
						</div>
						<div class="loginSubmit" @click="resetSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							提交
						</div>
					</div>
					<div class="changeType">
						<div>想起密码?<span @click="changeType(1)">去登录</span></div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script>
import { registerSms, register, resetPwd } from "@/api/login";
export default {
	data() {
		return {
			form: {},
			inputType: "password",
			loginShow: false,
			loginoutShow: false,
			subLoading: false,
			smsLoading: false,
			modelType: 1,
			getCodeTime: 0,
		};
	},

	created() {},

	beforeDestroy() {
		document.onkeydown = null;
	},

	methods: {
		// 回车提交
		submit() {
			if (this.modelType == 1) this.loginSubmit();
			if (this.modelType == 2) this.registerSubmit();
			if (this.modelType == 3) this.resetSubmit();
		},
		// 查看密码
		psdMouseDown() {
			this.inputType = "text";
			let selectionStart = this.$refs.psd.selectionStart;
			document.onmouseup = () => {
				document.onmousemove = null;
				document.onmouseup = null;
				this.inputType = "password";
				this.$refs.psd.focus();
				setTimeout(() => {
					this.$refs.psd.setSelectionRange(selectionStart, selectionStart);
				}, 0);
			};
		},
		// 重置密码提交
		resetSubmit() {
			let params = { ...this.form };
			if (!params.username) return this.$message.warning("请输入手机号/邮箱");
			if (!params.password) return this.$message.warning("请输入新密码");
			if (!params.code) return this.$message.warning("请输入验证码");
			this.subLoading = true;
			resetPwd(params)
				.then(() => {
					this.$message.success("重置密码成功");
					this.form = {};
					this.modelType = 1;
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		// 注册提交
		registerSubmit() {
			let params = { ...this.form };
			if (!params.username) return this.$message.warning("请输入手机号/邮箱");
			if (!params.password) return this.$message.warning("请输入密码");
			if (!params.code) return this.$message.warning("请输入验证码");
			this.subLoading = true;
			register(params)
				.then(() => {
					this.$message.success("注册成功");
					this.form = {};
					this.modelType = 1;
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		// 获取验证码
		getSms() {
			if (this.getCodeTime > 0 || this.smsLoading) return;
			if (!this.form.username) return this.$message.warning("请输入手机号/邮箱");
			this.smsLoading = true;
			registerSms({ username: this.form.username })
				.then(() => {
					this.$message.success("验证码发送成功");
					this.setInterval_codeTime();
				})
				.finally(() => {
					this.smsLoading = false;
				});
		},
		// 验证码倒计时
		setInterval_codeTime() {
			if (this.getCodeTime == 0) this.getCodeTime = 60;
			let timeId = setInterval(() => {
				this.getCodeTime--;
				if (this.getCodeTime < 10) this.getCodeTime = `0${this.getCodeTime}`;
				if (this.getCodeTime <= 0) clearInterval(timeId);
			}, 1000);
		},
		// 登录
		loginSubmit() {
			let params = { ...this.form };
			if (!params.username) return this.$message.warning("请输入手机号/邮箱");
			if (!params.password) return this.$message.warning("请输入密码");
			this.subLoading = true;
			this.$store
				.dispatch("Login", params)
				.then(() => {
					this.$router.go(0);
				})
				.catch(() => {
					this.subLoading = false;
				});
		},
		show(type) {
			this.modelType = type || 1;
			this.loginShow = true;
			document.body.classList.add("ovh");
			document.onkeydown = (e) => {
				let ev = document.all ? window.event : e;
				if (ev.keyCode == 13) this.submit();
			};
		},
		changeType(type) {
			this.modelType = type;
			this.form = {};
		},
		closeLoginMask() {
			this.loginShow = false;
			this.form = {};
			document.body.classList.remove("ovh");
			document.onkeydown = null;
		},
	},
};
</script>

<style lang="less" scoped>
.loginDialog {
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
	.login_box {
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
		.login_right_qr {
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
			.loginType_title {
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
			.qrcode_img {
				padding: 18px 0 14px;
				img {
					width: 250px;
				}
			}
			.login_form {
				padding: 40px 0 10px;
				.formItem {
					width: 350px;
					display: flex;
					flex-direction: column;
					align-items: flex-start;
					margin-bottom: 20px;
					span {
						margin-bottom: 10px;
					}
					input {
						width: 100%;
						padding: 10px 20px;
						border-radius: 4px;
						font-size: 18px;
						color: #5c5c5c;
						background-color: #f5f5f5;
						outline-color: #757575;
						border: 0;
						box-sizing: border-box;
					}
					.code_box {
						position: relative;
						width: 100%;
						display: flex;
						input {
							padding-right: 100px;
						}
						i {
							position: absolute;
							top: 50%;
							right: 10px;
							transform: translateY(-50%);
							font-size: 20px;
							cursor: pointer;
						}
						.getCode {
							width: 80px;
							text-align: center;
							position: absolute;
							top: 48%;
							right: 10px;
							transform: translateY(-50%);
							color: #61b0ff;
							cursor: pointer;
							user-select: none;
							&.disab {
								color: #757575;
								cursor: no-drop;
							}
						}
					}
				}
				.loginSubmit {
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
			.changeType {
				padding: 10px 0;
				div {
					margin: 6px 0;
				}
				span {
					cursor: pointer;
					color: #409eff;
				}
			}
			.tips {
				color: #949494;
				font-size: 14px;
				margin-bottom: 30px;
			}
			.login_type {
				display: flex;
				align-items: center;
				cursor: pointer;
				font-size: 18px;
				color: #f38f1c;
				user-select: none;
				i {
					margin-top: 1px;
					font-size: 22px;
					margin-right: 6px;
				}
			}
		}
	}
}
</style>
