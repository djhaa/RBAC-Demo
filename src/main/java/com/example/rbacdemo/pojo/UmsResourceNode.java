package com.example.rbacdemo.pojo;

import com.example.rbacdemo.pojo.UmsResource;
import io.swagger.annotations.ApiModelProperty;

/**
 * 额外的资源包装类(作用是指定当前登录用户的权限集)
 *
 * @author djhaa
 */
public class UmsResourceNode extends UmsResource {
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
