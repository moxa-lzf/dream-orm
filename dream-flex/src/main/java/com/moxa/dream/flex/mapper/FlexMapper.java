package com.moxa.dream.flex.mapper;

import com.moxa.dream.flex.def.Query;
import com.moxa.dream.flex.def.Update;
import com.moxa.dream.system.config.Page;
import com.moxa.dream.system.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 链式操作类
 */
public interface FlexMapper {
    Set<Class<? extends Inject>> WHITE_SET = new HashSet<>();

    /**
     * 查询并返回一条
     *
     * @param query 查询定义器
     * @param type  返回类型
     * @param <T>
     * @return
     */
    <T> T selectOne(Query query, Class<T> type);

    /**
     * 查询并返回集合
     *
     * @param query 查询定义器
     * @param type  返回类型
     * @param <T>
     * @return
     */
    <T> List<T> selectList(Query query, Class<T> type);

    /**
     * 查询并返回分页
     *
     * @param query 查询定义器
     * @param type  返回类型
     * @param page  分页
     * @param <T>
     * @return
     */
    <T> Page<T> selectPage(Query query, Class<T> type, Page page);

    /**
     * 更新操作
     *
     * @param update 更新定义器
     * @return
     */
    int update(Update update);
}