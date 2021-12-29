package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.dao.UmsMenuMapper;
import com.example.rbacdemo.pojo.UmsMenu;
import com.example.rbacdemo.pojo.UmsMenuExample;
import com.example.rbacdemo.pojo.dto.UmsMenuNode;
import com.example.rbacdemo.service.UmsMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理实现类
 *
 * @author djhaa
 */
@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Autowired
    private UmsMenuMapper umsMenuMapper;

    @Override
    public boolean create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateMenuLevel(umsMenu);
        return false;
    }

    /**
     * 修改菜单层级
     */
    private void updateMenuLevel(UmsMenu umsMenu) {
        Long parentId = umsMenu.getParentId();
        if (parentId == 0) {
            umsMenu.setLevel(0);
        } else {
            UmsMenu parentMenu = umsMenuMapper.selectByPrimaryKey(parentId);
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public boolean update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateMenuLevel(umsMenu);
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu) > 0;
    }

    @Override
    public List<UmsMenu> list() {
        return umsMenuMapper.selectByExample(null);
    }

    @Override
    public PageInfo<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        UmsMenuExample example = new UmsMenuExample();
        UmsMenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        PageHelper.startPage(pageNum, pageSize);
        List<UmsMenu> data = umsMenuMapper.selectByExample(example);
        return new PageInfo<>(data);
    }

    @Override
    public List<UmsMenuNode> treeList() {
        List<UmsMenu> menuList = list();
        return menuList.stream()
                //获取parentId==0的菜单(即获取顶级菜单)
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
    }

    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     *
     * @param umsMenu  当前菜单
     * @param menuList 后台菜单列表
     */
    private UmsMenuNode covertMenuNode(UmsMenu umsMenu, List<UmsMenu> menuList) {
        UmsMenuNode umsMenuNode = new UmsMenuNode();
        BeanUtils.copyProperties(umsMenu, umsMenuNode);
        List<UmsMenuNode> children = menuList.stream()
                //获取当前菜单的子菜单流
                .filter(menu -> menu.getParentId().equals(umsMenu.getId()))
                //递归调用covertMenuNode方法，直到当前菜单无子菜单
                .map(menu -> covertMenuNode(menu, menuList))
                .collect(Collectors.toList());
        umsMenuNode.setChildren(children);
        return umsMenuNode;
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu) > 0;
    }

    @Override
    public UmsMenu getById(Long id) {
        return umsMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean removeById(Long id) {
        return umsMenuMapper.deleteByPrimaryKey(id) > 0;
    }
}
