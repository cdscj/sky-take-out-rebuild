package cn.net.wenxin.client.controller.client;

import cn.net.wenxin.service.domain.*;
import cn.net.wenxin.service.service.*;
import cn.net.wenxin.common.core.controller.BaseController;
import cn.net.wenxin.common.core.domain.AjaxResult;
import cn.net.wenxin.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: CasLoginController
 * @Description: 论坛控制器（需认证）
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 14:54
 */

@RestController
@RequestMapping("/dev-api/topic")
public class TopicController extends BaseController {

    @Autowired
    private ITopicReplyService topicReplyService;
    @Autowired
    private ITopicInfoService topicInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ITopicReplyPraiseService topicReplyPraiseService;
    @Autowired
    private ITopicReplyReportService topicReplyReportService;
    @Autowired
    private IUserFollowService userFollowService;


    /**
     * 发布主题
     * @param info
     * @return
     */
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody TopicInfo info) {
        String username = tokenService.getAppUserName();
        info.setCreateBy(username);
        info.setUpdateBy(username);
        return success(topicInfoService.insertTopicInfo(info));
    }

    @PostMapping("/amend")
    public AjaxResult amend(@RequestBody TopicInfo info) {
        String username = tokenService.getAppUserName();
        info.setCreateBy(username);
        info.setUpdateBy(username);
        return success(topicInfoService.updateTopicInfo(info));
    }

    /**
     * 撤销主题
     * @param id
     * @return
     */
    @PostMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable("id") Long id) {
        return success(topicInfoService.cancelTopicInfo(id));
    }


    /**
     * 回复主题或回复评论
     * @param reply
     * @return
     */
    @PostMapping("/reply")
    public AjaxResult reply(@RequestBody TopicReply reply) {
        String username = tokenService.getAppUserName();
        reply.setReplyerId(username);
        reply.setCreateBy(username);
        reply.setUpdateBy(username);
        return success(topicReplyService.insertTopicReply(reply));
    }


    /**
     * 用户点赞主题或评论
     * @param praise
     * @return
     */
    @PostMapping("/reply/praise")
    public AjaxResult replyPraise(@RequestBody TopicReplyPraise praise) {
        String username = tokenService.getAppUserName();
        praise.setCreateBy(username);
        praise.setUpdateBy(username);
        return success(topicReplyPraiseService.insertTopicReplyPraise(praise));
    }

    /**
     * 用户取消点赞主题或评论
     * @param praise
     * @return
     */
    @PostMapping("/reply/unpraise")
    public AjaxResult unPraise(@RequestBody TopicReplyPraise praise) {
        String username = tokenService.getAppUserName();
        praise.setCreateBy(username);
        return success(topicReplyPraiseService.unpraise(praise));
    }

    /**
     * 用户举报主题或评论
     * @param replyReport
     * @return
     */
    @PostMapping("/reply/report")
    public AjaxResult replyReport(@RequestBody TopicReplyReport replyReport) {
        String username = tokenService.getAppUserName();
        replyReport.setReportUser(username);
        replyReport.setStatus("1");
        replyReport.setCreateBy(username);
        replyReport.setUpdateBy(username);
        return success(topicReplyReportService.insertTopicReplyReport(replyReport));
    }

    /**
     * 用户关注主题
     * @param follow
     * @return
     */
    @PostMapping("/follow")
    public AjaxResult follow(@RequestBody UserFollow follow) {
        String username = tokenService.getAppUserName();
        follow.setUserId(username);
        return success(userFollowService.insertUserFollow(follow));
    }

    @PostMapping("/unfollow")
    public AjaxResult unfollow(@RequestBody UserFollow follow) {
        String username = tokenService.getAppUserName();
        follow.setUserId(username);
        return success(userFollowService.unfollow(follow));
    }





}
