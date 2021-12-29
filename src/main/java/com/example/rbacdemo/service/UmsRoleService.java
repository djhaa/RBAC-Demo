package com.example.rbacdemo.service;

import com.example.rbacdemo.pojo.UmsMenu;
import com.example.rbacdemo.pojo.UmsResource;
import com.example.rbacdemo.pojo.UmsRole;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台角色管理接口
 *
 * @author djhaa
 */
public interface UmsRoleService {
    /**
     * 添加角色
     */
    boolean create(UmsRole role);

    /**
     * 更新角色
     * */
    boolean updateById(Long id, UmsRole role);

    /**
     * 批量删除角色
     */
    boolean delete(List<Long> ids);

    /**
     * 获取所有角色
     * */
    List<UmsRole> listAll();

    /**
     * 分页获取角色列表
     */
    PageInfo<UmsRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<UmsMenu> getMenuList(Long adminId);

    /**
     * 获取角色相关菜单
     */
    List<UmsMenu> listMenu(Long roleId);

    /**
     * 获取角色相关资源
     */
    List<UmsResource> listResource(Long roleId);

    /**
     * 给角色分配菜单
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     */
    @Transactional
    int allocResource(Long roleId, List<Long> resourceIds);
}
