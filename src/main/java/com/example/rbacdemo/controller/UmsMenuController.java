package com.example.rbacdemo.controller;

import com.example.rbacdemo.common.api.CommonPage;
import com.example.rbacdemo.common.api.CommonResult;
import com.example.rbacdemo.pojo.UmsMenu;
import com.example.rbacdemo.pojo.dto.UmsMenuNode;
import com.example.rbacdemo.service.UmsMenuService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台菜单管理
 *
 * @author djhaa
 */
@Api(tags = "UmsMenuController")
@RequestMapping(value = "/menu")
@RestController
public class UmsMenuController {
    @Autowired
    private UmsMenuService menuService;

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("添加后台菜单，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Object> create(@RequestBody UmsMenu umsMenu) {
        boolean success = menuService.create(umsMenu);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改后台菜单，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<Object> update(@PathVariable Long id,
                               @RequestBody UmsMenu umsMenu) {
        boolean success = menuService.update(id, umsMenu);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("根据ID获取菜单详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsMenu> getItem(@PathVariable Long id) {
        UmsMenu umsMenu = menuService.getById(id);
        return CommonResult.success(umsMenu);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("根据ID删除后台菜单，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<Object> delete(@PathVariable Long id) {
        boolean success = menuService.removeById(id);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("分页查询后台菜单")
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsMenu>> list(@PathVariable Long parentId,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsMenu> menuList = menuService.list(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(menuList));
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("树形结构返回所有菜单列表")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    public CommonResult<List<UmsMenuNode>> treeList() {
        List<UmsMenuNode> list = menuService.treeList();
        return CommonResult.success(list);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改菜单显示状态，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/updateHidden/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateHidden(@PathVariable Long id, @RequestParam("hidden") Integer hidden) {
        boolean success = menuService.updateHidden(id, hidden);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }
}
