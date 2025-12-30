package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.User;
import cn.net.wenxin.service.domain.UserDto;

/**
 * 用户信息Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface IUserService 
{
    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    public User selectUserById(Long id);

    /**
     * 查询用户信息列表
     * 
     * @param user 用户信息
     * @return 用户信息集合
     */
    public List<User> selectUserList(User user);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(User user);

    /**
     * 修改用户信息
     * 
     * @param userDto 用户信息
     * @return 结果
     */
    public int updateUser(UserDto userDto);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteUserByIds(Long[] ids);

    /**
     * 删除用户信息信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteUserById(Long id);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    public User selectUserByUserName(String username);

    /**
     * 校验用户名称是否唯一
     * @param user
     * @return
     */
    public boolean checkUserNameUnique(User user);

    /**
     * 注册用户信息
     * @param user
     * @return
     */
    public boolean registerUser(User user);

    /**
     * 修改密码
     * @param user
     * @return
     */
    public int changePwd(UserDto user);

    /**
     * 修改邮箱
     * @param user
     * @return
     */
    public int changeEmail(UserDto user);

    /**
     *发送邮箱验证码
     * @param user
     * @return
     */
    public boolean sendEmail(UserDto user);

    /**
     *修改手机号码
     * @param user
     * @return
     */
    public int changePhone(UserDto user);

    /**
     * 发送短信验证码
     * @param user
     * @return
     */
    public boolean sendSms(UserDto user);
}
