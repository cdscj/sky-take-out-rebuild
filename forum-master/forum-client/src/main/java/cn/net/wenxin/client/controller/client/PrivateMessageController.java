package cn.net.wenxin.client.controller.client;
import cn.net.wenxin.client.entity.PrivateMessage;
import cn.net.wenxin.client.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    /**
     * 发送私信
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam Long senderId, 
                                         @RequestParam Long receiverId, 
                                         @RequestParam String content) {
        try {
            privateMessageService.sendMessage(senderId, receiverId, content);
            return ResponseEntity.ok("私信发送成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("私信发送失败");
        }
    }

    /**
     * 获取未读私信
     */
    @GetMapping("/unread")
    public ResponseEntity<List<PrivateMessage>> getUnreadMessages(@RequestParam Long userId) {
        List<PrivateMessage> messages = privateMessageService.getUnreadMessages(userId);
        return ResponseEntity.ok(messages);
    }

    /**
     * 获取两个用户之间的私信
     */
    @GetMapping("/between")
    public ResponseEntity<List<PrivateMessage>> getMessagesBetweenUsers(@RequestParam Long userId1, 
                                                                       @RequestParam Long userId2) {
        List<PrivateMessage> messages = privateMessageService.getMessagesBetweenUsers(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

    /**
     * 标记私信为已读
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        privateMessageService.markAsRead(id);
        return ResponseEntity.ok("私信已标记为已读");
    }

    /**
     * 批量标记私信为已读
     */
    @PutMapping("/batch/read")
    public ResponseEntity<?> markBatchAsRead(@RequestBody List<Long> messageIds) {
        privateMessageService.markBatchAsRead(messageIds);
        return ResponseEntity.ok("私信已批量标记为已读");
    }
}