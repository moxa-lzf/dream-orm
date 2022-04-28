package com.moxa.dream.system.cache;

import com.moxa.dream.system.mapped.MappedStatement;

public interface Cache {
    void put(MappedStatement mappedStatement, Object value);

    Object get(MappedStatement mappedStatement);

    void remove(MappedStatement mappedStatement);

    void clear();
}