package com.moxa.dream.antlr.invoker;

import com.moxa.dream.antlr.exception.InvokerException;
import com.moxa.dream.antlr.factory.AntlrInvokerFactory;
import com.moxa.dream.antlr.smt.InvokerStatement;
import com.moxa.dream.antlr.smt.ListColumnStatement;
import com.moxa.dream.antlr.smt.Statement;
import com.moxa.dream.antlr.sql.ToAssist;
import com.moxa.dream.antlr.sql.ToSQL;
import com.moxa.dream.util.common.ObjectWrapper;

import java.util.List;

public class RepInvoker extends AbstractInvoker {
    ObjectWrapper paramWrapper;

    @Override
    public void init(ToAssist assist) {
        paramWrapper = assist.getCustom(ObjectWrapper.class);
    }

    @Override
    public String invoker(InvokerStatement invokerStatement, ToAssist assist, ToSQL toSQL, List<Invoker> invokerList) throws InvokerException {
        Statement[] columnList = ((ListColumnStatement) invokerStatement.getParamStatement()).getColumnList();
        if (columnList.length != 1)
            throw new InvokerException("参数个数错误,不满足@" + AntlrInvokerFactory.REP + ":" + AntlrInvokerFactory.NAMESPACE + "(value)");
        String paramName = toSQL.toStr(columnList[0], assist, invokerList);
        Object value = paramWrapper.get(paramName);
        return String.valueOf(value);
    }

}