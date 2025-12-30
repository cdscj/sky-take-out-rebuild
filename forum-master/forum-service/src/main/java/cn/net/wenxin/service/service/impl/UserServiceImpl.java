package cn.net.wenxin.service.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.net.wenxin.service.domain.UserDto;
import cn.net.wenxin.service.mapper.UserPointsMapper;
import cn.net.wenxin.service.service.IOperLogService;
import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.constant.UserConstants;
import cn.net.wenxin.common.core.redis.RedisCache;
import cn.net.wenxin.common.exception.ServiceException;
import cn.net.wenxin.common.utils.*;
import cn.net.wenxin.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.UserMapper;
import cn.net.wenxin.service.domain.User;
import cn.net.wenxin.service.service.IUserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息Service业务层处理
 *
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IOperLogService operLogService;
    @Autowired
    private UserPointsMapper userPointsMapper;

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    @Override
    public User selectUserById(Long id) {
        User user = userMapper.selectUserById(id);
        if(user != null){
            user.setNickName(StringUtils.strMasking(user.getNickName()));
            //设置等级信息
            Map<String,Object> userPoint = userPointsMapper.getUserPointLevel(user.getUserName());
            if(userPoint != null){
                user.setLevelName(userPoint.getOrDefault("name","").toString());
                user.setLevelIcon(userPoint.getOrDefault("icon","").toString());
                user.setCurrentPoint(Long.parseLong(userPoint.getOrDefault("point","0").toString()));
            }
        }
        return user;
    }

    /**
     * 查询用户信息列表
     *
     * @param user 用户信息
     * @return 用户信息
     */
    @Override
    public List<User> selectUserList(User user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int insertUser(User user) {
        user.setPassword(UserConstants.DEFAULT_PASSWORD);
        if (StringUtils.isEmpty(user.getUserName())) {
            throw new ServiceException("用户名不能为空");
        } else if (StringUtils.isEmpty(user.getPassword())) {
            throw new ServiceException("用户密码不能为空");
        } else if (user.getUserName().length() < UserConstants.USERNAME_MIN_LENGTH
                || user.getUserName().length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        } else if (user.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || user.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        } else if (!checkUserNameUnique(user)) {
            throw new ServiceException("新增注册账号已存在");
        }
        if (StringUtils.isNotEmpty(user.getPhonenumber())) {
            if (!StringUtils.isPhone(user.getPhonenumber())) {
                throw new ServiceException("手机号码格式不正确");
            }
            User puser = userMapper.checkPhoneUnique(user.getPhonenumber());
            if (puser != null && puser.getId().longValue() != user.getId().longValue()) {
                throw new ServiceException("手机号码已存在");
            }
        }
        if (StringUtils.isNotEmpty(user.getEmail())) {
            if (!StringUtils.isEmail(user.getEmail())) {
                throw new ServiceException("邮箱格式不正确");
            }
            User puser = userMapper.checkEmailUnique(user.getEmail());
            if (puser != null && puser.getId().longValue() != user.getId().longValue()) {
                throw new ServiceException("邮箱已存在");
            }
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setCreateTime(DateUtils.getNowDate());
        return userMapper.insertUser(user);
    }

    /**
     * 修改用户信息
     *
     * @param userDto 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(UserDto userDto) {
        User ouser = userMapper.selectUserById(userDto.getId());
        if(ouser == null){
            throw new ServiceException("获取用户异常");
        }
        if (StringUtils.isNotEmpty(userDto.getPhonenumber())) {
            if (!StringUtils.isPhone(userDto.getPhonenumber())) {
                throw new ServiceException("手机号码格式不正确");
            }
            User puser = userMapper.checkPhoneUnique(userDto.getPhonenumber());
            if (puser != null && puser.getId().longValue() != userDto.getId().longValue()) {
                throw new ServiceException("手机号码已存在");
            }
        }
        if (StringUtils.isNotEmpty(userDto.getEmail())) {
            if (!StringUtils.isEmail(userDto.getEmail())) {
                throw new ServiceException("邮箱格式不正确");
            }
            User puser = userMapper.checkEmailUnique(userDto.getEmail());
            if (puser != null && puser.getId().longValue() != userDto.getId().longValue()) {
                throw new ServiceException("邮箱已存在");
            }
        }
        BeanUtils.copyBeanProp(ouser, userDto);
        ouser.setUpdateTime(DateUtils.getNowDate());

        //新增操作日志信息
        operLogService.insertUserOperLog(ouser.getUserName(), 2, "修改个人信息");
        return userMapper.updateUser(ouser);

    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] ids) {
        return userMapper.deleteUserByIds(ids);
    }

    /**
     * 删除用户信息信息
     *
     * @param id 用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserById(Long id) {
        return userMapper.deleteUserById(id);
    }

    @Override
    public User selectUserByUserName(String username) {
        User user = userMapper.selectUserByUserName(username);
        if(user != null){
            user.setNickName(StringUtils.strMasking(user.getNickName()));
            //设置等级信息
            Map<String,Object> userPoint = userPointsMapper.getUserPointLevel(user.getUserName());
            if(userPoint != null){
                user.setLevelName(userPoint.getOrDefault("name","").toString());
                user.setLevelIcon(userPoint.getOrDefault("icon","").toString());
                user.setCurrentPoint(Long.parseLong(userPoint.getOrDefault("point","0").toString()));
            }
        }
        return user;
    }

    @Override
    public boolean checkUserNameUnique(User user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        User info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }else{
            info = userMapper.checkEmailUnique(user.getUserName());
            if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }else{
                info = userMapper.checkPhoneUnique(user.getUserName());
                if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
                    return UserConstants.NOT_UNIQUE;
                }
            }
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean registerUser(User user) {
        user.setCreateTime(DateUtils.getNowDate());
        user.setUpdateTime(DateUtils.getNowDate());
        return userMapper.insertUser(user) > 0;
    }

    @Override
    public int changePwd(UserDto user) {
        Long userId = user.getId();
        if (userId != null) {
            User ouser = userMapper.selectUserById(userId);
            if (StringUtils.isNotNull(ouser)) {
                if ("1".equals(ouser.getStatus())) {
                    throw new ServiceException("用户已停用");
                }
            } else {
                throw new ServiceException("用户不存在");
            }
            if (StringUtils.isBlank(user.getOldPassword())) {
                throw new ServiceException("旧密码不能为空");
            }
            if (!SecurityUtils.matchesPassword(user.getOldPassword(),ouser.getPassword())) {
                throw new ServiceException("旧密码错误");
            }
            if (StringUtils.isBlank(user.getPassword())) {
                throw new ServiceException("密码不能为空");
            }
            if (StringUtils.isBlank(user.getConfirmPassword())) {
                throw new ServiceException("确认密码不能为空");
            }
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                throw new ServiceException("密码和确认密码不一致");
            }
            if (user.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                    || user.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH) {
                throw new ServiceException("密码长度必须在5到20个字符之间");
            }
            ouser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
            ouser.setUpdateTime(DateUtils.getNowDate());
            //新增操作日志信息
            operLogService.insertUserOperLog(ouser.getUserName(), 2, "修改密码");
            return userMapper.updateUser(ouser);
        }
        return 0;
    }

    @Override
    public int changeEmail(UserDto user) {
        Long userId = user.getId();
        if (userId == null) {
            throw new ServiceException("用户不存在");
        }
        if (StringUtils.isBlank(user.getCode())) {
            throw new ServiceException("验证码不能为空");
        }
        if (!StringUtils.isEmail(user.getEmail())) {
            throw new ServiceException("邮箱格式不正确");
        }
        User info = userMapper.checkEmailUnique(user.getEmail());
        if(info != null){
            throw new ServiceException("邮箱已存在");
        }
        String code = redisCache.getCacheObject(Constants.EMAIL_CODE + user.getUserName());
        if (StringUtils.isBlank(code) || !code.equals(user.getCode())) {
            throw new ServiceException("验证码不正确");
        }
        User ouser = userMapper.selectUserById(userId);
        if (StringUtils.isNotNull(ouser)) {
            if ("1".equals(ouser.getStatus())) {
                throw new ServiceException("用户已停用");
            }
        } else {
            throw new ServiceException("用户不存在");
        }
        ouser.setEmail(user.getEmail());
        ouser.setUpdateTime(DateUtils.getNowDate());
        //新增操作日志信息
        operLogService.insertUserOperLog(ouser.getUserName(), 2, "修改邮箱");
        return userMapper.updateUser(ouser);
    }

    @Override
    public boolean sendEmail(UserDto user) {
        if (StringUtils.isBlank(user.getEmail())) {
            throw new ServiceException("邮箱不能为空");
        }
        if (!StringUtils.isEmail(user.getEmail())) {
            throw new ServiceException("邮箱格式不正确");
        }
        String code = redisCache.getCacheObject(Constants.EMAIL_CODE + user.getEmail());
        if (StringUtils.isNotBlank(code)) {
            return true;
        } else {
            String ecode = SMSUtils.sendMail(user.getEmail());
            if (StringUtils.isNotBlank(ecode)) {
                redisCache.setCacheObject(Constants.EMAIL_CODE + user.getEmail(), ecode, Constants.SMS_CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
                //新增操作日志信息
                operLogService.insertUserOperLog(SecurityUtils.getUsername(), 1, "发送邮箱验证码");
                return true;
            }
        }
        return false;
    }

    @Override
    public int changePhone(UserDto user) {
        Long userId = user.getId();
        if (userId == null) {
            throw new ServiceException("用户不存在");
        }
        if (StringUtils.isBlank(user.getCode())) {
            throw new ServiceException("验证码不能为空");
        }
        if (!StringUtils.isPhone(user.getPhonenumber())) {
            throw new ServiceException("手机号码格式不正确");
        }
        String code = redisCache.getCacheObject(Constants.SMS_CAPTCHA_CHANGEUSER + user.getPhonenumber());
        if (StringUtils.isBlank(code) || !code.equals(user.getCode())) {
            throw new ServiceException("验证码不正确");
        }
        User info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if(info != null){
            throw new ServiceException("手机号码已存在");
        }
        User ouser = userMapper.selectUserById(userId);
        if (StringUtils.isNotNull(ouser)) {
            if ("1".equals(ouser.getStatus())) {
                throw new ServiceException("用户已停用");
            }
        } else {
            throw new ServiceException("用户不存在");
        }

        ouser.setPhonenumber(user.getPhonenumber());
        ouser.setUpdateTime(DateUtils.getNowDate());
        //新增操作日志信息
        operLogService.insertUserOperLog(SecurityUtils.getUsername(), 2, "修改手机号码");
        return userMapper.updateUser(ouser);
    }

    @Override
    public boolean sendSms(UserDto user) {
        if (StringUtils.isBlank(user.getPhonenumber())) {
            throw new ServiceException("手机号码不能为空");
        }
        if (!StringUtils.isPhone(user.getPhonenumber())) {
            throw new ServiceException("手机号码格式不正确");
        }
        String code = redisCache.getCacheObject(Constants.SMS_CAPTCHA_CHANGEUSER + user.getPhonenumber());
        if (StringUtils.isNotBlank(code)) {
            return true;
        } else {
            String ecode = SMSUtils.SMSSend(user.getPhonenumber());
            if (StringUtils.isNotBlank(ecode)) {
                redisCache.setCacheObject(Constants.SMS_CAPTCHA_CHANGEUSER + user.getPhonenumber(), ecode, Constants.SMS_CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
                //新增操作日志信息
                operLogService.insertUserOperLog(SecurityUtils.getUsername(), 1, "发送手机验证码");
                return true;
            }
        }
        return false;
    }
}
