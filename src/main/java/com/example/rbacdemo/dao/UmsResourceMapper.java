package com.example.rbacdemo.dao;

import com.example.rbacdemo.pojo.UmsResource;
import com.example.rbacdemo.pojo.UmsResourceExample;
import java.util.List;

import com.example.rbacdemo.pojo.UmsResourceNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UmsResourceMapper {
    long countByExample(UmsResourceExample example);

    int deleteByExample(UmsResourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsResource record);

    int insertSelective(UmsResource record);

    List<UmsResource> selectByExample(UmsResourceExample example);

    UmsResource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsResource record, @Param("example") UmsResourceExample example);

    int updateByExample(@Param("record") UmsResource record, @Param("example") UmsResourceExample example);

    int updateByPrimaryKeySelective(UmsResource record);

    int updateByPrimaryKey(UmsResource record);

    List<UmsResourceNode> getResourceList(@Param("adminId") Long adminId);

    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
