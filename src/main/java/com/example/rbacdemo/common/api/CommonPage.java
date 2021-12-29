package com.example.rbacdemo.common.api;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页数据封装类
 * @author djhaa
 */
public class CommonPage<T> {
    /**
     * pageNum 页码
     * pageSize 每页个数
     * totalPage 总页数
     * total 总数
     * list 数据
     * */
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Integer total;
    private List<T> list;

    /**
    * 将PageHelper分页后的data封装成CommonPage类型
    * */
    public static <T> CommonPage<T> restPage(List<T> data) {
        CommonPage<T> result = new CommonPage<>();
        PageInfo<T> pageInfo = new PageInfo<>(data);
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setList(pageInfo.getList());
        result.setTotalPage(pageInfo.getPages());
        result.setTotal(pageInfo.getSize());
        return result;
    }

    public static <T> CommonPage<T> restPage(PageInfo<T> data) {
        CommonPage<T> result = new CommonPage<>();
        result.setPageNum(data.getPageNum());
        result.setPageSize(data.getPageSize());
        result.setList(data.getList());
        result.setTotalPage(data.getPages());
        result.setTotal(data.getSize());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> data) {
        this.list = data;
    }
}
