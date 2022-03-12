package com.moxa.dream.module.core.listener;

import com.moxa.dream.module.core.executor.Executor;
import com.moxa.dream.module.mapped.MappedStatement;

public interface ExecutorListener {
    void before(Executor executor, MappedStatement mappedStatement);

    void afterReturn(Object result, Executor executor, MappedStatement mappedStatement);

    void exception(Exception e, Executor executor, MappedStatement mappedStatement);

    void after(Executor executor, MappedStatement mappedStatement);
}