package com.example.rbacdemo.service;

import com.example.rbacdemo.pojo.UmsAdmin;
import com.example.rbacdemo.pojo.UmsRole;
import com.example.rbacdemo.pojo.dto.UmsAdminParam;
import com.example.rbacdemo.pojo.UmsResourceNode;
import com.example.rbacdemo.pojo.dto.UpdateAdminPasswordParam;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台用户管理接口
 * @author djhaa
 */
public interface UmsAdminService {
    /**
     * 根据用户id获取后台管理员
     * */
    UmsAdmin getAdminById(Long adminId);

    /**
     * 根据用户名获取后台管理员
     * */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册
     * */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回对应的token
     * */
    String login(String username, String password);

    /**
     * 刷新token
     * @param token 旧token
     * */
    String refreshToken(String token);

    /**
    * 根据用户名或昵称分页查询用户
    * */
    PageInfo<UmsAdmin> queryAdminList(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 修改指定用户信息
     * */
    boolean updateById(Long id, UmsAdmin umsAdmin);

    /**
     * 删除指定用户
     * */
    boolean deleteById(Long id);

    /**
     * 修改用户角色关系
     * */
    @Transactional
    int updateRoleById(Long id, List<Long> roleIds);

    /**
     * 获取用户对应角色
     * */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     */
    List<UmsResourceNode> getResourceList(Long adminId);

    /**
     * 修改密码
     */
    boolean updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);
}
