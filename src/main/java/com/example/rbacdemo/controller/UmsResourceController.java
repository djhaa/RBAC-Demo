package com.example.rbacdemo.controller;

import com.example.rbacdemo.common.api.CommonPage;
import com.example.rbacdemo.common.api.CommonResult;
import com.example.rbacdemo.pojo.UmsResource;
import com.example.rbacdemo.service.UmsResourceService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台资源管理
 *
 * @author djhaa
 */
@Api(tags = "UmsResourceController")
@RequestMapping(value = "/resource")
@RestController
public class UmsResourceController {
    @Autowired
    private UmsResourceService resourceService;

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("添加后台资源，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Object> create(@RequestBody UmsResource umsResource) {
        boolean success = resourceService.create(umsResource);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改后台资源，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<Object> update(@PathVariable("id") Long id,
                               @RequestBody UmsResource umsResource) {
        boolean success = resourceService.update(id, umsResource);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("根据ID获取资源详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource umsResource = resourceService.getById(id);
        return CommonResult.success(umsResource);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("根据ID删除后台资源，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<Object> delete(@PathVariable("id") Long id) {
        boolean success = resourceService.delete(id);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("分页模糊查询后台资源")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsResource>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsResource> resourceList = resourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("查询所有后台资源，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> resourceList = resourceService.listAll();
        return CommonResult.success(resourceList);
    }
}
