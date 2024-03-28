package com.dream.lambda.wrapper.defaults;

import com.dream.antlr.smt.ConditionStatement;
import com.dream.antlr.smt.QueryStatement;
import com.dream.lambda.factory.QueryCreatorFactory;
import com.dream.lambda.wrapper.ConditionWrapper;
import com.dream.lambda.wrapper.WhereWrapper;

import java.util.function.Consumer;

public class DefaultWhereWrapper extends ConditionWrapper<DefaultWhereWrapper> implements WhereWrapper<DefaultGroupByWrapper, DefaultHavingWrapper, DefaultOrderByWrapper, DefaultLimitWrapper, DefaultUnionWrapper, DefaultForUpdateWrapper, DefaultQueryWrapper> {
    @Override
    public QueryStatement statement() {
        return null;
    }

    @Override
    public QueryCreatorFactory creatorFactory() {
        return null;
    }

    @Override
    public DefaultGroupByWrapper where(Consumer<ConditionWrapper> fn) {
        fn.accept(this);
        return null;
    }

    @Override
    public DefaultOrderByWrapper having(Consumer<ConditionWrapper> fn) {
        return null;
    }

    @Override
    protected void accept(ConditionStatement statement) {

    }
}
