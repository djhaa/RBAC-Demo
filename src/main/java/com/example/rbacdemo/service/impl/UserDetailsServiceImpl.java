package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.pojo.UmsAdmin;
import com.example.rbacdemo.pojo.AdminUserDetails;
import com.example.rbacdemo.pojo.UmsResourceNode;
import com.example.rbacdemo.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailsService 实现类
 *
 * @author djhaa
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UmsAdminService umsAdminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UmsAdmin admin = umsAdminService.getAdminByUsername(username);
        if(admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        List<UmsResourceNode> resourceList = umsAdminService.getResourceList(admin.getId());
        return new AdminUserDetails(admin, resourceList);
    }
}
