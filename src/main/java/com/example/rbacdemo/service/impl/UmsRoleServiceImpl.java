package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.dao.*;
import com.example.rbacdemo.pojo.*;
import com.example.rbacdemo.service.UmsAdminCacheService;
import com.example.rbacdemo.service.UmsRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台角色管理实现类
 *
 * @author djhaa
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {
    @Autowired
    private UmsRoleMapper umsRoleMapper;
    @Autowired
    private UmsAdminCacheService umsAdminCacheService;
    @Autowired
    private UmsMenuMapper umsMenuMapper;
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Autowired
    private UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;
    @Autowired
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;

    @Override
    public boolean create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        return umsRoleMapper.insertSelective(role) > 0;
    }

    @Override
    public boolean updateById(Long id, UmsRole role) {
        role.setId(id);
        return umsRoleMapper.updateByPrimaryKeySelective(role) > 0;
    }

    @Override
    public boolean delete(List<Long> ids) {
        UmsRoleExample example = new UmsRoleExample();
        UmsRoleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        umsAdminCacheService.delResourceListByRoleIds(ids);
        return umsRoleMapper.deleteByExample(example) > 0;
    }

    @Override
    public PageInfo<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        UmsRoleExample example = null;
        if(keyword != null) {
            example = new UmsRoleExample();
            UmsRoleExample.Criteria criteria = example.createCriteria();
            criteria.andNameLike(keyword);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UmsRole> res = umsRoleMapper.selectByExample(example);
        return new PageInfo<>(res);
    }

    @Override
    public List<UmsRole> listAll() {
        return umsRoleMapper.selectByExample(null);
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return umsMenuMapper.getMenuList(adminId);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return umsMenuMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return umsResourceMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        int cnt = menuIds == null ? 0 : menuIds.size();
        if (cnt == 0) {
            return cnt;
        }
        //删除原菜单
        UmsRoleMenuRelationExample example = new UmsRoleMenuRelationExample();
        UmsRoleMenuRelationExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        umsRoleMenuRelationMapper.deleteByExample(example);
        //新建菜单关联
        menuIds.forEach(menuId -> {
            UmsRoleMenuRelation relation = new UmsRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            umsRoleMenuRelationMapper.insert(relation);
        });
        return cnt;
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        int cnt = resourceIds == null ? 0 : resourceIds.size();
        //删除原资源
        UmsRoleResourceRelationExample example = new UmsRoleResourceRelationExample();
        UmsRoleResourceRelationExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        umsRoleResourceRelationMapper.deleteByExample(example);
        umsAdminCacheService.delResourceListByRole(roleId);
        if (cnt == 0) {
            return cnt;
        }
        //新建资源关联
        resourceIds.forEach(resourceId -> {
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            umsRoleResourceRelationMapper.insert(relation);
        });
        return cnt;
    }
}
