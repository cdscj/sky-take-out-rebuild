package cn.net.wenxin.client.controller.client;

import cn.net.wenxin.service.domain.Label;
import cn.net.wenxin.service.domain.TopicReply;
import cn.net.wenxin.service.domain.User;
import cn.net.wenxin.service.domain.vo.TopicBaseVo;
import cn.net.wenxin.service.domain.vo.TopicDetailVo;
import cn.net.wenxin.service.domain.vo.UserVo;
import cn.net.wenxin.service.service.ILabelService;
import cn.net.wenxin.service.service.ITopicInfoService;
import cn.net.wenxin.service.service.ITopicReplyService;
import cn.net.wenxin.service.service.IUserService;
import cn.net.wenxin.common.core.controller.BaseController;
import cn.net.wenxin.common.core.domain.AjaxResult;
import cn.net.wenxin.common.core.page.TableDataInfo;
import cn.net.wenxin.common.utils.bean.BeanUtils;
import cn.net.wenxin.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: CasLoginController
 * @Description: 论坛控制器（免登录）
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 14:54
 */

@RestController
@RequestMapping("/forum")
public class ForumController extends BaseController {

    @Autowired
    private ILabelService labelService;
    @Autowired
    private ITopicInfoService topicInfoService;
    @Autowired
    private ITopicReplyService topicReplyService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IUserService userService;


    /**
     * 标签列表
     *
     * @return 结果
     */
    @PostMapping("/label/list")
    public AjaxResult labelList(@RequestBody Label label) {
        List<Label> labels = labelService.selectLabelList(label);
        return success(labels);
    }

    /**
     * 主题列表
     *
     * @param orderIn  排序：1最新回复，2热门主题，3新鲜出炉，4精华
     * @param labelId  标签ID
     * @param username 用户名
     * @param type 类型：1我的关注主题，2我的回复主题，3我编辑的主题，4我赞过的主题
     * @return
     */
    @GetMapping("/topic/list")
    public TableDataInfo topicList(@RequestParam(value = "orderIn", required = false, defaultValue = "1") Integer orderIn,
                                   @RequestParam(value = "labelId", required = false) Long labelId,
                                   @RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "type", required = false) Integer type,
                                   @RequestParam(value = "name", required = false) String name) {
        startPage();
        List<TopicDetailVo> list = topicInfoService.selectTopicInfoList(orderIn, labelId, username, type,name);
        return getDataTable(list);
    }

    /**
     * 获取别人的基础信息
     * @param username
     * @return
     */
    @GetMapping("/topic/user")
    public AjaxResult topicUser(String username) {
        User user = userService.selectUserByUserName(username);
        UserVo userVo = new UserVo();
        BeanUtils.copyBeanProp(userVo, user);
        return success(userVo);
    }

    /**
     * 顶部搜索
     * @param name
     * @return
     */
    @GetMapping("/topic/searchList")
    public AjaxResult searchList(@RequestParam(value = "name", required = false) String name) {
        List<TopicBaseVo> list = topicInfoService.selectTopicBaseList(name);
        return success(list);
    }

    /**
     * 主题详情
     *
     * @param topicId
     * @return
     */
    @GetMapping("/topic/detail")
    public AjaxResult topicDetail(@RequestParam Long topicId) {
        TopicDetailVo info = topicInfoService.selectTopicInfoDetail(topicId);
        return AjaxResult.success(info);
    }

    /**
     * 获取主题一级评论
     *
     * @param topicId
     * @return
     */
    @GetMapping("/reply/main/list")
    public TableDataInfo topicReplyMainList(@RequestParam Long topicId) {
        startPage();
        List<TopicReply> list = topicReplyService.selectTopicReplyMainList(topicId);
        return getDataTable(list);
    }

    /**
     * 获取评论下回复
     *
     * @param replyId
     * @return
     */
    @GetMapping("/reply/list")
    public TableDataInfo topicReplyList(@RequestParam Long replyId) {
        startPage();
        List<TopicReply> list = topicReplyService.getTopicReplyList(replyId);
        return getDataTable(list);
    }

}
