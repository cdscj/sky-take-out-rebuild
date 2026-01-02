package cn.net.wenxin.client.controller.client;

import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.core.controller.BaseController;
import cn.net.wenxin.common.core.domain.AjaxResult;
import cn.net.wenxin.common.core.domain.model.LoginBody;
import cn.net.wenxin.framework.web.service.AppLoginService;
import cn.net.wenxin.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: CasLoginController
 * @Description: 用户登录控制器
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 14:54
 */

@RestController
@RequestMapping( "/dev-api/cas")
public class CasLoginController extends BaseController {

    @Autowired
    private AppLoginService loginService;

    @Autowired
    private TokenService tokenService;


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();

        // 生成令牌
          String token = loginService.login(loginBody.getUsername(), loginBody.getPassword());
//          String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2MjYzNDEyMzQsInVzZXJJZCI6MTAwMSwicm9sZSI6IkFETUlOIn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

}
