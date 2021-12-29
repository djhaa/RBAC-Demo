package com.example.rbacdemo.controller;

import com.example.rbacdemo.common.api.CommonPage;
import com.example.rbacdemo.common.api.CommonResult;
import com.example.rbacdemo.pojo.UmsMenu;
import com.example.rbacdemo.pojo.UmsResource;
import com.example.rbacdemo.pojo.UmsRole;
import com.example.rbacdemo.service.UmsRoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户角色管理
 *
 * @author djhaa
 */
@Api(tags = "UmsRoleController")
@RequestMapping(value = "/role")
@RestController
public class UmsRoleController {
    @Autowired
    private UmsRoleService umsRoleService;

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation(value = "添加角色，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Object> create(@RequestBody UmsRole role) {
        boolean res = umsRoleService.create(role);
        return res ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改角色，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<Object> update(@PathVariable Long id, @RequestBody UmsRole role) {
        boolean success = umsRoleService.updateById(id, role);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("批量删除角色，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids) {
        boolean success = umsRoleService.delete(ids);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("获取所有角色")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<UmsRole>> listAll() {
        List<UmsRole> roleList = umsRoleService.listAll();
        return CommonResult.success(roleList);
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("根据角色名称分页获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsRole>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageInfo<UmsRole> roleList = umsRoleService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改角色状态，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateStatus(@PathVariable("id") Long id, @RequestParam(value = "status") Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setStatus(status);
        boolean success = umsRoleService.updateById(id, umsRole);
        return success ? CommonResult.success(null) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("获取角色相关菜单")
    @RequestMapping(value = "/listMenu/{roleId}", method = RequestMethod.GET)
    public CommonResult<List<UmsMenu>> listMenu(@PathVariable("roleId") Long roleId) {
        List<UmsMenu> roleList = umsRoleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("获取角色相关资源")
    @RequestMapping(value = "/listResource/{roleId}", method = RequestMethod.GET)
    public CommonResult<List<UmsResource>> listResource(@PathVariable("roleId") Long roleId) {
        List<UmsResource> roleList = umsRoleService.listResource(roleId);
        return CommonResult.success(roleList);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("给角色分配菜单，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/allocMenu", method = RequestMethod.POST)
    public CommonResult<Object> allocMenu(@RequestParam("roleId") Long roleId, @RequestParam("menuIds") List<Long> menuIds) {
        int count = umsRoleService.allocMenu(roleId, menuIds);
        return CommonResult.success(count);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("给角色分配资源，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/allocResource", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Object> allocResource(@RequestParam("roleId") Long roleId, @RequestParam("resourceIds") List<Long> resourceIds) {
        int count = umsRoleService.allocResource(roleId, resourceIds);
        return CommonResult.success(count);
    }
}
