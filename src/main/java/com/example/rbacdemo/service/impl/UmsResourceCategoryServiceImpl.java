package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.dao.UmsResourceCategoryMapper;
import com.example.rbacdemo.pojo.UmsResourceCategory;
import com.example.rbacdemo.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理实现类
 *
 * @author djhaa
 */
@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {
    @Autowired
    private UmsResourceCategoryMapper umsResourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        return umsResourceCategoryMapper.selectByExample(null);
    }

    @Override
    public boolean create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        return umsResourceCategoryMapper.insertSelective(umsResourceCategory) > 0;
    }

    @Override
    public boolean updateById(UmsResourceCategory umsResourceCategory) {
        return umsResourceCategoryMapper.updateByPrimaryKeySelective(umsResourceCategory) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return umsResourceCategoryMapper.deleteByPrimaryKey(id) > 0;
    }
}
