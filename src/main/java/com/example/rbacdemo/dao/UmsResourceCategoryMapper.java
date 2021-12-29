package com.example.rbacdemo.dao;

import com.example.rbacdemo.pojo.UmsResourceCategory;
import com.example.rbacdemo.pojo.UmsResourceCategoryExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UmsResourceCategoryMapper {
    long countByExample(UmsResourceCategoryExample example);

    int deleteByExample(UmsResourceCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsResourceCategory record);

    int insertSelective(UmsResourceCategory record);

    List<UmsResourceCategory> selectByExample(UmsResourceCategoryExample example);

    UmsResourceCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsResourceCategory record, @Param("example") UmsResourceCategoryExample example);

    int updateByExample(@Param("record") UmsResourceCategory record, @Param("example") UmsResourceCategoryExample example);

    int updateByPrimaryKeySelective(UmsResourceCategory record);

    int updateByPrimaryKey(UmsResourceCategory record);
}
