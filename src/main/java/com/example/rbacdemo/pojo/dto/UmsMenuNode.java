package com.example.rbacdemo.pojo.dto;

import com.example.rbacdemo.pojo.UmsMenu;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 后台菜单节点封装
 *
 * @author djhaa
 */
public class UmsMenuNode extends UmsMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode> children;

    public List<UmsMenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<UmsMenuNode> children) {
        this.children = children;
    }
}
