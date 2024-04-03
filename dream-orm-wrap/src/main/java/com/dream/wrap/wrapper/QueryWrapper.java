package com.dream.wrap.wrapper;

import com.dream.struct.command.Query;
import com.dream.wrap.factory.WrapQueryFactory;

public interface QueryWrapper extends Query {
    WrapQueryFactory creatorFactory();
}
