package com.example.rbacdemo.service;

import com.example.rbacdemo.pojo.UmsResource;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 后台资源管理接口
 *
 * @author djhaa
 */
public interface UmsResourceService {
    /**
     * 添加资源
     */
    boolean create(UmsResource umsResource);

    /**
     * 修改资源
     */
    boolean update(Long id, UmsResource umsResource);

    /**
     * 删除资源
     */
    boolean delete(Long id);

    /**
     * 分页查询资源
     */
    PageInfo<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * 通过id查找资源
     * */
    UmsResource getById(Long id);

    /**
     * 查询所有资源
     * */
    List<UmsResource> listAll();
}
