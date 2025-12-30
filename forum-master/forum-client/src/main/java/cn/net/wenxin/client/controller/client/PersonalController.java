package cn.net.wenxin.client.controller.client;

import cn.net.wenxin.service.domain.UserDto;
import cn.net.wenxin.service.service.IUserService;
import cn.net.wenxin.common.core.controller.BaseController;
import cn.net.wenxin.common.core.domain.AjaxResult;
import cn.net.wenxin.common.core.domain.model.LoginUser;
import cn.net.wenxin.common.utils.ServletUtils;
import cn.net.wenxin.common.utils.file.FileUploadUtils;
import cn.net.wenxin.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户注册Controller
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 14:54
 */
@RestController
@RequestMapping("/personal")
public class PersonalController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private TokenService tokenService;

    @Value("${file.uploadUrl}")
    private String uploadUrl;

    @Value("${file.showUrl}")
    private String showUrl;

    /**
     * 用户信息
     */
    @GetMapping("/userInfo")
    public AjaxResult userInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getAppUser().getId();
        return AjaxResult.success(userService.selectUserById(userId));
    }

    @GetMapping("/getUserInfo")
    public AjaxResult userInfo(Long userId) {
        return AjaxResult.success(userService.selectUserById(userId));
    }

    /**
     * 修改个人信息
     * @param userDto
     * @return
     */
    @PostMapping("/updateUser")
    public AjaxResult updateUser(@RequestBody UserDto userDto) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getAppUser().getId();
        userDto.setId(userId);
        return toAjax(userService.updateUser(userDto));
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @PostMapping("/changePwd")
    public AjaxResult changePwd(@RequestBody UserDto user) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getAppUser().getId();
        user.setId(userId);
        return AjaxResult.success(userService.changePwd(user));
    }

    /**
     * 修改邮箱
     * @param user
     * @return
     */
    @PostMapping("/changeEmail")
    public AjaxResult changeEmail(@RequestBody UserDto user) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getAppUser().getId();
        user.setId(userId);
        return AjaxResult.success(userService.changeEmail(user));
    }

    /**
     * 发送邮箱验证码
     *
     * @return
     */
    @PostMapping("/sendEmail")
    public AjaxResult sendEmail(@RequestBody UserDto user) {
        boolean b = userService.sendEmail(user);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 修改手机号码
     * @param user
     * @return
     */
    @PostMapping("/changePhone")
    public AjaxResult changePhone(@RequestBody UserDto user) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getAppUser().getId();
        user.setId(userId);
        return AjaxResult.success(userService.changePhone(user));
    }

    /**
     * 发送短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendSms")
    public AjaxResult sendSms(@RequestBody UserDto user) {
        boolean b = userService.sendSms(user);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 上传头像
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/avatar")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传并返回新路径
            String path = FileUploadUtils.saveFile(file, uploadUrl, showUrl);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", path);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
