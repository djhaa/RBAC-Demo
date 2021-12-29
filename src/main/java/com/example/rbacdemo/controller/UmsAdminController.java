package com.example.rbacdemo.controller;

import cn.hutool.core.collection.CollUtil;
import com.example.rbacdemo.common.api.CommonPage;
import com.example.rbacdemo.common.api.CommonResult;
import com.example.rbacdemo.pojo.UmsAdmin;
import com.example.rbacdemo.pojo.UmsRole;
import com.example.rbacdemo.pojo.dto.UmsAdminLoginParam;
import com.example.rbacdemo.pojo.dto.UmsAdminParam;
import com.example.rbacdemo.pojo.dto.UpdateAdminPasswordParam;
import com.example.rbacdemo.service.UmsAdminCacheService;
import com.example.rbacdemo.service.UmsAdminService;
import com.example.rbacdemo.service.UmsRoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台用户管理
 *
 * @author djhaa
 */
@Api(tags = "UmsAdminController")
@RequestMapping(value = "/admin")
@RestController
public class UmsAdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminController.class);
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private UmsRoleService umsRoleService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = umsAdminService.register(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<Map<String, String>> login(@RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String username = umsAdminLoginParam.getUsername();
        String password = umsAdminLoginParam.getPassword();
        String token = umsAdminService.login(username, password);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        LOGGER.info("token: {}", token);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenHead", tokenHead);
        return CommonResult.success(data);
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "刷新Token，未登录用户无权限")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    public CommonResult<Map<String, String>> refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = umsAdminService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    /**
     * @param principal principal中保存着已登录用户的信息
     * */
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "获取当前登录用户信息，未登录用户无权限")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<Map<String, Object>> getAdminInfo(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = umsAdminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("menus", umsRoleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = umsAdminService.getRoleList(umsAdmin.getId());
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public CommonResult<Object> logout() {
        LOGGER.info("Logout: 用户登出");
        return CommonResult.success(null);
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("根据用户名或姓名分页获取用户列表，未登录用户无权限")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsAdmin>> getUserList(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageInfo<UmsAdmin> res = umsAdminService.queryAdminList(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(res));
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("获取指定用户信息，未登录用户无权限")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsAdmin> getAdmin(@PathVariable("id") Long id) {
        UmsAdmin admin = umsAdminService.getAdminById(id);
        return CommonResult.success(admin);
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改指定用户信息，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateAdmin(@PathVariable("id") Long id, @RequestBody UmsAdmin umsAdmin) {
        boolean res = umsAdminService.updateById(id, umsAdmin);
        return res ? CommonResult.success("修改成功", null) : CommonResult.failed("修改失败");
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改指定用户密码，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public CommonResult<Object> updatePassword(@RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        boolean res = umsAdminService.updatePassword(updatePasswordParam);
        return res ? CommonResult.success("修改成功", null) : CommonResult.failed("修改失败");
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("删除指定用户信息，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<Object> deleteAdmin(@PathVariable("id") Long id) {
        boolean res = umsAdminService.deleteById(id);
        return res ? CommonResult.success("删除成功", null) : CommonResult.failed("删除失败");
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("修改帐号状态，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateAccountStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        UmsAdmin admin = new UmsAdmin();
        admin.setStatus(status);
        boolean res = umsAdminService.updateById(id, admin);
        return res ? CommonResult.success("更新成功", null) : CommonResult.failed("更新失败");
    }

    @PreAuthorize("hasAnyRole('admin_super', 'admin_permission')")
    @ApiOperation("给用户分配角色，权限拥有者(超级管理员、权限管理员)")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public CommonResult<Object> updateRole(@RequestParam("adminId") Long adminId,
                                           @RequestParam("roleIds") List<Long> roleIds) {
        int cnt = umsAdminService.updateRoleById(adminId, roleIds);
        return cnt > 0 ? CommonResult.success(cnt) : CommonResult.failed();
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation("获取指定用户的角色，未登录无权限")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable("id") Long adminId) {
        List<UmsRole> res = umsAdminService.getRoleList(adminId);
        return CommonResult.success(res);
    }
}
