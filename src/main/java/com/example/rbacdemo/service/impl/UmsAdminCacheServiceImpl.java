package com.example.rbacdemo.service.impl;

import com.example.rbacdemo.common.service.RedisService;
import com.example.rbacdemo.dao.UmsAdminMapper;
import com.example.rbacdemo.dao.UmsAdminRoleRelationMapper;
import com.example.rbacdemo.pojo.UmsAdmin;
import com.example.rbacdemo.pojo.UmsAdminRoleRelation;
import com.example.rbacdemo.pojo.UmsAdminRoleRelationExample;
import com.example.rbacdemo.pojo.UmsResourceNode;
import com.example.rbacdemo.service.UmsAdminCacheService;
import com.example.rbacdemo.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author djhaa
 */
@SuppressWarnings("unchecked")
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {
    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private UmsAdminMapper umsAdminMapper;
    @Autowired
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;

    @Override
    public void delAdmin(Long adminId) {
        UmsAdmin admin = umsAdminService.getAdminById(adminId);
        if(admin != null) {
            String key = REDIS_KEY_ADMIN + ":" + admin.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public void delResourceList(Long adminId) {
        String key = REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        UmsAdminRoleRelationExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<UmsAdminRoleRelation> data = umsAdminRoleRelationMapper.selectByExample(example);
        String prefixKey = REDIS_KEY_RESOURCE_LIST + ":";
        List<String> res = data.stream().map(umsAdminRoleRelation -> prefixKey + umsAdminRoleRelation.getAdminId()).collect(Collectors.toList());
        redisService.del(res);
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        UmsAdminRoleRelationExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdIn(roleIds);
        List<UmsAdminRoleRelation> data = umsAdminRoleRelationMapper.selectByExample(example);
        String prefixKey = REDIS_KEY_RESOURCE_LIST + ":";
        List<String> res = data.stream().map(umsAdminRoleRelation -> prefixKey + umsAdminRoleRelation.getAdminId()).collect(Collectors.toList());
        redisService.del(res);
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIds = umsAdminMapper.getAdminIdList(resourceId);
        String prefixKey = REDIS_KEY_RESOURCE_LIST + ":";
        List<String> res = adminIds.stream().map(adminId -> prefixKey + adminId).collect(Collectors.toList());
        redisService.del(res);
    }

    @Override
    public UmsAdmin getAdmin(String username) {
        String key = REDIS_KEY_ADMIN + ":" + username;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisService.set(key, admin, REDIS_EXPIRE);
    }

    @Override
    public List<UmsResourceNode> getResourceList(Long adminId) {
        String key = REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        Object res = redisService.get(key);
        return res != null ? (List<UmsResourceNode>) res : new ArrayList<>();
    }

    @Override
    public void setResourceList(Long adminId, List<UmsResourceNode> resourceList) {
        String key = REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }
}
