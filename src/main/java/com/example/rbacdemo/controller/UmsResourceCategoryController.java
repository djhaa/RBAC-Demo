package com.example.rbacdemo.controller;

import com.example.rbacdemo.common.api.CommonResult;
import com.example.rbacdemo.pojo.UmsResourceCategory;
import com.example.rbacdemo.service.UmsResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台资源分类管理
 *
 * @author djhaa
 */
@Api(tags = "UmsResourceCategoryController")
@RequestMapping(value = "/resourceCategory")
@RestController
public class UmsResourceCategoryController {
    @Autowired
    private UmsResourceCategoryService resourceCategoryService;

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("查询所有后台资源分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<UmsResourceCategory>> listAll() {
        List<UmsResourceCategory> resourceList = resourceCategoryService.listAll();
        return CommonResult.success(resourceList);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("添加后台资源分类，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Object> create(@RequestBody UmsResourceCategory umsResourceCategory) {
        boolean success = resourceCategoryService.create(umsResourceCategory);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改后台资源分类，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<Object> update(@PathVariable Long id,
                               @RequestBody UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        boolean success = resourceCategoryService.updateById(umsResourceCategory);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("根据ID删除后台资源，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<Object> delete(@PathVariable Long id) {
        boolean success = resourceCategoryService.removeById(id);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }
}
