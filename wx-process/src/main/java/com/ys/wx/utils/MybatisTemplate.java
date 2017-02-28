package com.ys.wx.utils;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.ys.wx.utils.pager.ORDERBY;
import com.ys.wx.utils.pager.PagerModel;
import com.ys.wx.utils.pager.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Title : mybatis模板抽象类
 * Description :
 * Author : Maxwell wen    date : 2016年1月12日
 * Update :                date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public abstract class MybatisTemplate {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    public <T> T selectOne(String statement) {
        return this.sqlSessionTemplate.selectOne(statement);
    }

    public <T> T selectOne(String statement, Object parameter) {
        return this.sqlSessionTemplate.selectOne(statement, parameter);
    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return this.sqlSessionTemplate.selectMap(statement, mapKey);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return this.sqlSessionTemplate.selectMap(statement, parameter, mapKey);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return this.sqlSessionTemplate.selectMap(statement, parameter, mapKey, rowBounds);
    }

    public <E> List<E> selectList(String statement) {
        return this.sqlSessionTemplate.selectList(statement);
    }

    public <E> List<E> selectList(String statement, Object parameter) {
        return this.sqlSessionTemplate.selectList(statement, parameter);
    }

    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return this.sqlSessionTemplate.selectList(statement, parameter, rowBounds);
    }

    public void select(String statement, ResultHandler handler) {
        this.sqlSessionTemplate.select(statement, handler);
    }

    public void select(String statement, Object parameter, ResultHandler handler) {
        this.sqlSessionTemplate.select(statement, parameter, handler);
    }

    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        this.sqlSessionTemplate.select(statement, parameter, rowBounds, handler);
    }

    public int insert(String statement) {
        return this.sqlSessionTemplate.insert(statement);
    }

    public int insert(String statement, Object parameter) {
        return this.sqlSessionTemplate.insert(statement, parameter);
    }

    public int update(String statement) {
        return this.sqlSessionTemplate.update(statement);
    }

    public int update(String statement, Object parameter) {
        return this.sqlSessionTemplate.update(statement, parameter);
    }

    public int delete(String statement) {
        return this.sqlSessionTemplate.delete(statement);
    }

    public int delete(String statement, Object parameter) {
        return this.sqlSessionTemplate.delete(statement, parameter);
    }

    /**
     * 排序查询list
     *
     * @param statement sql
     * @param params    参数
     * @param orderBy   排序的map key是列名，value是desc或者asc
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <E> List<E> selectList(String statement, Object params, Map<String, ORDERBY> orderBy) {
        PageBounds pageBounds = new PageBounds(PageBounds.NO_PAGE, PageBounds.NO_ROW_LIMIT);
        //不让其查询总记录数
        pageBounds.setContainsTotalCount(false);
        //添加排序
        setOrderInfo(orderBy, pageBounds);
        return (List<E>) sqlSessionTemplate.selectList(statement, params, pageBounds);
    }

    /**
     * 分页结果集
     *
     * @param statement
     * @param params
     * @param query
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <E> List<E> getPagerModelListByQuery(String statement, Object params, Query query) {
        PageBounds pageBounds = new PageBounds(query.getPageNumber(), query.getPageSize());
        //不让其查询总记录数
        pageBounds.setContainsTotalCount(false);
        //添加排序
        setOrderInfo(query.getSqlOrderBy(), pageBounds);
        return (List<E>) sqlSessionTemplate.selectList(statement, params, pageBounds);
    }

    /**
     * 分页查询方法
     *
     * @param params  参数
     * @param query   查询分页参数
     * @param dataSql 数据集sql
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> PagerModel<T> getPagerModelByQuery(Object params, Query query, String dataSql) {
        PageBounds pageBounds = new PageBounds(query.getPageNumber(), query.getPageSize());
        //添加排序
        this.setOrderInfo(query.getSqlOrderBy(), pageBounds);
        List<T> datas = sqlSessionTemplate.selectList(dataSql, params, pageBounds);
        PageList<T> pageList = (PageList<T>) datas;
        // 获得结果集条总数
        int count = pageList.getPaginator().getTotalCount();
        return new PagerModel<T>(count, datas);
    }


    // 设置排序信息
    private void setOrderInfo(Map<String, ORDERBY> orderBy, PageBounds pageBounds) {
        List<Order> orders;
        if (orderBy != null && orderBy.size() > 0) {
            orders = new ArrayList<Order>();
            for (Entry<String, ORDERBY> order : orderBy.entrySet()) {
                orders.add(Order.create(order.getKey(), order.getValue().toString()));
            }
            if (CollectionUtils.isNotEmpty(orders)) {
                pageBounds.setOrders(orders);
            }
        }
    }

}
