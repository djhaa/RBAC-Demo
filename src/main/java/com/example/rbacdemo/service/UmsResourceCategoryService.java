package com.example.rbacdemo.service;

import com.example.rbacdemo.pojo.UmsResourceCategory;

import java.util.List;

/**
 * 后台资源分类管理接口
 *
 * @author djhaa
 */
public interface UmsResourceCategoryService {
    /**
     * 获取所有资源分类
     */
    List<UmsResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    boolean create(UmsResourceCategory umsResourceCategory);

    /**
     * 修改后台资源分类
     * */
    boolean updateById(UmsResourceCategory umsResourceCategory);

    /**
     * 根据id删除后台资源
     * */
    boolean removeById(Long id);
}
