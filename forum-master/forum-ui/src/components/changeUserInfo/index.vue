<template>
	<div class="changeDialog" v-if="changeShow">
		<div class="mask" @click="closechangeMask"></div>
		<div class="change_box">
			<div class="change_right_qr">
				<div class="closeBtn">
					<i class="el-icon-close" @click="closechangeMask"></i>
				</div>
				<template v-if="modelType == 1">
					<div class="changeType_title">
						<span>更改密码</span>
					</div>
					<div class="change_form">
						<div class="formItem">
							<span>旧密码</span>
							<input type="password" v-model="form.oldPassword" />
						</div>
						<div class="formItem">
							<span>新密码</span>
							<input type="password" v-model="form.password" />
						</div>
						<div class="formItem">
							<span>确认密码</span>
							<input type="password" v-model="form.confirmPassword" />
						</div>
						<!-- <div class="formItem">
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
						</div> -->
						<div class="changeSubmit" @click="pwdSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							提交
						</div>
					</div>
				</template>
				<template v-if="modelType == 2">
					<div class="changeType_title">
						<span>更改邮箱</span>
					</div>
					<div class="change_form">
						<div class="formItem">
							<span>邮箱</span>
							<input type="text" v-model="form.email" />
						</div>
						<div class="formItem">
							<span>验证码</span>
							<div class="code_box">
								<input type="text" v-model="form.code" />
								<div
									:class="{ getCode: true, disab: getCodeTime_email > 0 ? true : false }"
									@click="getEmailSms"
								>
									<i class="el-icon-loading" style="margin-right: 4px;" v-if="smsLoading"></i>
									{{ getCodeTime_email > 0 ? getCodeTime_email + "秒后获取" : "获取验证码" }}
								</div>
							</div>
						</div>
						<div class="changeSubmit" @click="emailSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							提交
						</div>
					</div>
				</template>
				<template v-if="modelType == 3">
					<div class="changeType_title">
						<span>更改昵称</span>
					</div>
					<div class="change_form">
						<div class="formItem">
							<span>昵称</span>
							<input type="text" v-model="form.nickName" />
						</div>
						<div class="changeSubmit" @click="nameSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							保存
						</div>
					</div>
				</template>
				<template v-if="modelType == 4">
					<div class="changeType_title">
						<span>更改手机</span>
					</div>
					<div class="change_form">
						<div class="formItem">
							<span>手机</span>
							<input type="text" v-model="form.phonenumber" />
						</div>
						<div class="formItem">
							<span>验证码</span>
							<div class="code_box">
								<input type="text" v-model="form.code" />
								<div
									:class="{ getCode: true, disab: getCodeTime_phone > 0 ? true : false }"
									@click="getPhoneSms"
								>
									<i class="el-icon-loading" style="margin-right: 4px;" v-if="smsLoading"></i>
									{{ getCodeTime_phone > 0 ? getCodeTime_phone + "秒后获取" : "获取验证码" }}
								</div>
							</div>
						</div>
						<div class="changeSubmit" @click="phoneSubmit">
							<i class="el-icon-loading" style="margin-right: 4px;" v-if="subLoading"></i>
							提交
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script>
import { registerSms } from "@/api/login";
import { updateUser, changePwd, sendEmail, changeEmail, sendSms, changePhone } from "@/api/user";
export default {
	data() {
		return {
			form: {},
			smsLoading: false,
			changeShow: false,
			changeoutShow: false,
			modelType: 1,
			subLoading: false,
			userInfo: {},
			getCodeTime: 0,
			getCodeTime_email: 0,
			getCodeTime_phone: 0,
		};
	},

	mounted() {},

	beforeDestroy() {
		document.onkeydown = null;
	},

	methods: {
		// 回车提交
		submit() {
			if (this.modelType == 1) this.pwdSubmit();
			if (this.modelType == 2) this.emailSubmit();
			if (this.modelType == 3) this.nameSubmit();
			if (this.modelType == 4) this.phoneSubmit();
		},
		pwdSubmit() {
			let params = {
				userName: this.userInfo.userName,
				password: this.form.password,
				confirmPassword: this.form.confirmPassword,
				code: this.form.code,
			};
			if (!params.password) return this.$message.warning("请输入新密码");
			if (!params.confirmPassword) return this.$message.warning("请输入确认密码");
			if (params.password != params.confirmPassword) {
				return this.$message.warning("新密码与确认密码不一致");
			}
			if (!params.code) return this.$message.warning("请输入验证码");
			this.subLoading = true;
			changePwd(params)
				.then(() => {
					this.$message.success("密码修改成功,下次登录生效");
					this.closechangeMask();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		// 获取验证码-手机
		getPhoneSms() {
			if (this.getCodeTime_phone > 0 || this.smsLoading) return;
			let params = {
				userName: this.userInfo.userName,
				phonenumber: this.form.phonenumber,
			};
			if (!params.phonenumber) return this.$message.warning("请输入手机");
			this.smsLoading = true;
			sendSms(params)
				.then(() => {
					this.$message.success("手机验证码发送成功");
					this.setInterval_phone();
				})
				.finally(() => {
					this.smsLoading = false;
				});
		},
		// 验证码倒计时-手机
		setInterval_phone() {
			if (this.getCodeTime_phone == 0) this.getCodeTime_phone = 60;
			let timeId = setInterval(() => {
				this.getCodeTime_phone--;
				if (this.getCodeTime_phone < 10) this.getCodeTime_phone = `0${this.getCodeTime_phone}`;
				if (this.getCodeTime_phone <= 0) clearInterval(timeId);
			}, 1000);
		},
		// 获取验证码-邮箱
		getEmailSms() {
			if (this.getCodeTime_email > 0 || this.smsLoading) return;
			let params = {
				userName: this.userInfo.userName,
				email: this.form.email,
			};
			if (!params.email) return this.$message.warning("请输入邮箱");
			this.smsLoading = true;
			sendEmail(params)
				.then(() => {
					this.$message.success("邮箱验证码发送成功");
					this.setInterval_email();
				})
				.finally(() => {
					this.smsLoading = false;
				});
		},
		// 验证码倒计时-邮箱
		setInterval_email() {
			if (this.getCodeTime_email == 0) this.getCodeTime_email = 60;
			let timeId = setInterval(() => {
				this.getCodeTime_email--;
				if (this.getCodeTime_email < 10) this.getCodeTime_email = `0${this.getCodeTime_email}`;
				if (this.getCodeTime_email <= 0) clearInterval(timeId);
			}, 1000);
		},
		// 获取验证码
		getSms() {
			if (this.getCodeTime > 0 || this.smsLoading) return;
			this.smsLoading = true;
			registerSms({ username: this.userInfo.userName })
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
		nameSubmit() {
			let params = {
				id: this.userInfo.id,
				userName: this.userInfo.userName,
				nickName: this.form.nickName,
			};
			if (!params.nickName) return this.$message.warning("昵称不能为空");
			this.subLoading = true;
			updateUser(params)
				.then(() => {
					this.$emit("changeNickName", this.form.nickName);
					this.$message.success("修改成功");
					this.closechangeMask();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		emailSubmit() {
			let params = {
				userName: this.userInfo.userName,
				email: this.form.email,
				code: this.form.code,
			};
			if (!params.email) return this.$message.warning("请输入邮箱");
			if (!params.code) return this.$message.warning("请输入验证码");
			this.subLoading = true;
			changeEmail(params)
				.then(() => {
					this.$emit("changeEmail", this.form.email);
					this.$message.success("邮箱修改成功");
					this.closechangeMask();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		phoneSubmit() {
			let params = {
				userName: this.userInfo.userName,
				phonenumber: this.form.phonenumber,
				code: this.form.code,
			};
			if (!params.phonenumber) return this.$message.warning("请输入手机");
			if (!params.code) return this.$message.warning("请输入验证码");
			this.subLoading = true;
			changePhone(params)
				.then(() => {
					this.$emit("changePhone", this.form.phonenumber);
					this.$message.success("手机修改成功");
					this.closechangeMask();
				})
				.finally(() => {
					this.subLoading = false;
				});
		},
		show(type, info) {
			this.modelType = type;
			this.userInfo = { ...info };
			this.form = {};
			if (type == 2) this.form.email = info.email;
			if (type == 3) this.form.nickName = info.nickName;
			if (type == 4) this.form.phonenumber = info.phonenumber;
			this.changeShow = true;
			document.body.classList.add("ovh");
			document.onkeydown = (e) => {
				let ev = document.all ? window.event : e;
				if (ev.keyCode == 13) this.submit();
			};
		},
		closechangeMask() {
			this.changeShow = false;
			document.body.classList.remove("ovh");
			document.onkeydown = null;
		},
	},
};
</script>

<style lang="less" scoped>
.changeDialog {
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
	.change_box {
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
		.change_right_qr {
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
			.changeType_title {
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
			.change_form {
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
						.getCode {
							min-width: 80px;
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
				.changeSubmit {
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
			.change_type {
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
