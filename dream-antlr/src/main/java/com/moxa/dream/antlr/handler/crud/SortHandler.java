package com.moxa.dream.antlr.handler.crud;

import com.moxa.dream.antlr.exception.InvokerException;
import com.moxa.dream.antlr.handler.AbstractHandler;
import com.moxa.dream.antlr.invoker.Invoker;
import com.moxa.dream.antlr.invoker.SortInvoker;
import com.moxa.dream.antlr.smt.QueryStatement;
import com.moxa.dream.antlr.smt.Statement;
import com.moxa.dream.antlr.config.Assist;
import com.moxa.dream.antlr.sql.ToSQL;

import java.util.List;

public class SortHandler extends AbstractHandler {
    private final SortInvoker sortInvoker;
    private final Statement[] statementList;

    public SortHandler(SortInvoker sortInvoker, Statement[] statementList) {
        this.sortInvoker = sortInvoker;
        this.statementList = statementList;

    }

    @Override
    protected Statement handlerBefore(Statement statement, Assist assist, ToSQL toSQL, List<Invoker> invokerList, int life) throws InvokerException {
        QueryStatement queryStatement = (QueryStatement) statement;
        sortInvoker.addSort(queryStatement, statementList);
        return statement;
    }

    @Override
    protected boolean interest(Statement statement, Assist sqlAssist) {
        return statement instanceof QueryStatement;
    }
}
