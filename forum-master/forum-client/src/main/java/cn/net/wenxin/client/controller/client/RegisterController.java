package cn.net.wenxin.client.controller.client;

import cn.net.wenxin.common.core.controller.BaseController;
import cn.net.wenxin.common.core.domain.AjaxResult;
import cn.net.wenxin.common.core.domain.model.RegisterBody;
import cn.net.wenxin.common.utils.StringUtils;
import cn.net.wenxin.framework.web.service.AppRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: RegisterController
 * @Description: 用户注册控制器
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 14:54
 */

@RestController
@RequestMapping("/register")
public class RegisterController extends BaseController {

    @Autowired
    private AppRegisterService registerService;

    /**
     * 注册验证
     *
     * @param user
     * @return
     */
    @PostMapping("/commit")
    public AjaxResult register(@RequestBody RegisterBody user) {
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    /**
     * 验证码
     * @param username
     * @return
     */
    @GetMapping("/registerSms")
    public AjaxResult registerSms(String username) {
        if (!StringUtils.isPhone(username) && !StringUtils.isEmail(username)) {
            return error("手机号/邮箱格式不正确");
        }
        String msg = registerService.getCode(username);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody RegisterBody user) {
        String msg = registerService.resetPwd(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

}
