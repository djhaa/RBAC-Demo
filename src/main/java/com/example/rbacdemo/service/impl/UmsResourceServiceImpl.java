package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.dao.UmsResourceMapper;
import com.example.rbacdemo.pojo.UmsResource;
import com.example.rbacdemo.pojo.UmsResourceExample;
import com.example.rbacdemo.service.UmsAdminCacheService;
import com.example.rbacdemo.service.UmsAdminService;
import com.example.rbacdemo.service.UmsResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源管理实现类
 *
 * @author djhaa
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Autowired
    private UmsAdminCacheService umsAdminCacheService;

    @Override
    public boolean create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        return umsResourceMapper.insertSelective(umsResource) > 0;
    }

    @Override
    public boolean update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        umsAdminCacheService.delResourceListByResource(id);
        return umsResourceMapper.updateByPrimaryKeySelective(umsResource) > 0;
    }

    @Override
    public boolean delete(Long id) {
        umsAdminCacheService.delResourceListByResource(id);
        return umsResourceMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public PageInfo<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        UmsResourceExample example = null;
        if(categoryId != null || nameKeyword != null || urlKeyword != null) {
            example = new UmsResourceExample();
            UmsResourceExample.Criteria criteria = example.createCriteria();
            if(categoryId != null) {
                criteria.andCategoryIdEqualTo(categoryId);
            }
            if(nameKeyword != null) {
                criteria.andNameLike(nameKeyword);
            }
            if(urlKeyword != null) {
                criteria.andUrlLike(urlKeyword);
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UmsResource> data = umsResourceMapper.selectByExample(example);
        return new PageInfo<>(data);
    }

    @Override
    public UmsResource getById(Long id) {
        return umsResourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsResource> listAll() {
        return umsResourceMapper.selectByExample(null);
    }
}
