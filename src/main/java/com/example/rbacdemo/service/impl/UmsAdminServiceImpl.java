package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.common.exception.ApiException;
import com.example.rbacdemo.dao.*;
import com.example.rbacdemo.pojo.*;
import com.example.rbacdemo.pojo.AdminUserDetails;
import com.example.rbacdemo.pojo.dto.UmsAdminParam;
import com.example.rbacdemo.pojo.UmsResourceNode;
import com.example.rbacdemo.pojo.dto.UpdateAdminPasswordParam;
import com.example.rbacdemo.service.UmsAdminCacheService;
import com.example.rbacdemo.service.UmsAdminService;
import com.example.rbacdemo.utils.JwtTokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户管理实现类
 *
 * @author djhaa
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private UmsAdminMapper umsAdminMapper;
    @Autowired
    private UmsAdminLoginLogMapper umsAdminLoginLogMapper;
    @Autowired
    private UmsAdminCacheService umsAdminCacheService;
    @Autowired
    private UmsRoleMapper umsRoleMapper;
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Autowired
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public UmsAdmin getAdminById(Long adminId) {
        return umsAdminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdmin umsAdmin = umsAdminCacheService.getAdmin(username);
        if(umsAdmin != null) {
            return umsAdmin;
        }
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<UmsAdmin> res = umsAdminMapper.selectByExample(example);
        if (res != null && res.size() > 0) {
            UmsAdmin admin = res.get(0);
            //设置缓存
            umsAdminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        // 查询是否有相同名称用户
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> res = umsAdminMapper.selectByExample(example);
        if (res.size() > 0) {
            return null;
        }
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new ApiException("密码不正确");
            }
            if(!userDetails.isEnabled()) {
                throw new ApiException("账号已被禁用");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        }catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     */
    private void insertLoginLog(String username) {
        UmsAdmin umsAdmin = getAdminByUsername(username);
        if (umsAdmin != null) {
            UmsAdminLoginLog umsAdminLoginLog = new UmsAdminLoginLog();
            umsAdminLoginLog.setAdminId(umsAdmin.getId());
            umsAdminLoginLog.setCreateTime(new Date());
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();
            umsAdminLoginLog.setIp(request.getRemoteAddr());
            umsAdminLoginLogMapper.insert(umsAdminLoginLog);
        }
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setLoginTime(new Date());
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        umsAdminMapper.updateByExampleSelective(umsAdmin, example);
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshToken(token);
    }

    @Override
    public PageInfo<UmsAdmin> queryAdminList(String keyword, Integer pageNum, Integer pageSize) {
        UmsAdminExample example = null;
        if(keyword != null) {
            example = new UmsAdminExample();
            UmsAdminExample.Criteria criteria1 = example.createCriteria();
            criteria1.andUsernameLike(keyword);
            UmsAdminExample.Criteria criteria2 = example.createCriteria();
            criteria2.andNickNameLike(keyword);
            example.or(criteria2);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UmsAdmin> res = umsAdminMapper.selectByExample(example);
        return new PageInfo<>(res);
    }

    @Override
    public boolean updateById(Long id, UmsAdmin umsAdmin) {
        umsAdmin.setId(id);
        UmsAdmin rawAdmin = getAdminById(id);
        if(passwordEncoder.matches(umsAdmin.getPassword(), rawAdmin.getPassword())) {
            umsAdmin.setPassword(null);
        }else {
            if(StringUtils.isEmpty(umsAdmin.getPassword())) {
                umsAdmin.setPassword(null);
            }else {
                umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
            }
        }
        umsAdminCacheService.delAdmin(id);
        return umsAdminMapper.updateByPrimaryKeySelective(umsAdmin) > 0;
    }

    /**
    * ums_admin_role_relation表未进行删除处理
    * */
    @Override
    public boolean deleteById(Long id) {
        umsAdminCacheService.delAdmin(id);
        umsAdminCacheService.delResourceList(id);
        return umsAdminMapper.deleteByPrimaryKey(id) > 0;
    }

    /**
     * 先删除原来的用户角色关联数据，再新建新的用户角色关联数据
     * */
    @Override
    public int updateRoleById(Long id, List<Long> roleIds) {
        int cnt = roleIds == null ? 0 : roleIds.size();
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        UmsAdminRoleRelationExample.Criteria criteria = example.createCriteria();
        criteria.andAdminIdEqualTo(id);
        List<UmsAdminRoleRelation> data = umsAdminRoleRelationMapper.selectByExample(example);
        //批量删除原关联数据
        if (data.size() > 0) {
            UmsAdminRoleRelationExample example1 = new UmsAdminRoleRelationExample();
            UmsAdminRoleRelationExample.Criteria criteria1 = example1.createCriteria();
            List<Long> adminIds = data.stream().map(UmsAdminRoleRelation::getAdminId).collect(Collectors.toList());
            criteria1.andAdminIdIn(adminIds);
            umsAdminRoleRelationMapper.deleteByExample(example1);
        }
        //批量插入新关联数据(后期优化使用Batch)
        if (cnt > 0) {
            roleIds.forEach(roleId -> {
                UmsAdminRoleRelation relation = new UmsAdminRoleRelation();
                relation.setAdminId(id);
                relation.setRoleId(roleId);
                umsAdminRoleRelationMapper.insertSelective(relation);
            });
        }
        //清除缓存中的过期资源列表
        umsAdminCacheService.delResourceList(id);
        return cnt;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsRoleMapper.getRoleList(adminId);
    }

    @Override
    public List<UmsResourceNode> getResourceList(Long adminId) {
        List<UmsResourceNode> cache = umsAdminCacheService.getResourceList(adminId);
        if (cache.size() > 0) {
            return cache;
        }
        List<UmsResourceNode> data = umsResourceMapper.getResourceList(adminId);
        if (data.size() > 0) {
            umsAdminCacheService.setResourceList(adminId, data);
        }
        return data;
    }

    @Override
    public boolean updatePassword(UpdateAdminPasswordParam param) {
        String username = param.getUsername();
        String oldPassWord = param.getOldPassword();
        String newPassWord = param.getNewPassword();
        //判断输入参数是否合法
        if (username == null || oldPassWord == null || newPassWord == null) {
            return false;
        }
        //判断用户名是否存在
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<UmsAdmin> data = umsAdminMapper.selectByExample(example);
        if (data == null || data.size() == 0) {
            return false;
        }
        UmsAdmin admin = data.get(0);
        //判断旧密码是否输出正确
        if (!passwordEncoder.matches(oldPassWord, admin.getPassword())) {
            return false;
        }
        String encodePassWord = passwordEncoder.encode(newPassWord);
        admin.setPassword(encodePassWord);
        umsAdminCacheService.delAdmin(admin.getId());
        return umsAdminMapper.updateByPrimaryKeySelective(admin) > 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if(admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        List<UmsResourceNode> resourceList = getResourceList(admin.getId());
        return new AdminUserDetails(admin, resourceList);
    }
}
