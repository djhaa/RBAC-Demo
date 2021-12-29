package com.example.rbacdemo.service;

import com.example.rbacdemo.pojo.UmsMenu;
import com.example.rbacdemo.pojo.dto.UmsMenuNode;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 后台菜单管理接口
 *
 * @author djhaa
 */
public interface UmsMenuService {
    /**
     * 创建后台菜单
     */
    boolean create(UmsMenu umsMenu);

    /**
     * 修改后台菜单
     */
    boolean update(Long id, UmsMenu umsMenu);

    /**
     * 查询后台菜单
     */
    List<UmsMenu> list();

    /**
     * 分页查询后台菜单
     */
    PageInfo<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<UmsMenuNode> treeList();

    /**
     * 修改菜单显示状态
     *
     * @param hidden 修改后的状态
     */
    boolean updateHidden(Long id, Integer hidden);

    /**
     * 根据id获取后台菜单详情
     * */
    UmsMenu getById(Long id);

    /**
     * 根据id删除后台菜单
     * */
    boolean removeById(Long id);
}
