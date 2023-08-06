package com.dream.template.condition;

import com.dream.antlr.invoker.Invoker;
import com.dream.antlr.util.AntlrUtil;
import com.dream.system.antlr.invoker.MarkInvoker;
import com.dream.template.mapper.AbstractMapper;

public class BetweenCondition implements Condition {

    @Override
    public String getCondition(String table, String column, String field) {
        return table + "." + column
                + " between "
                + AntlrUtil.invokerSQL(MarkInvoker.FUNCTION, Invoker.DEFAULT_NAMESPACE,
                AbstractMapper.DREAM_TEMPLATE_PARAM + "." + field + ".0") + " and "
                + AntlrUtil.invokerSQL(MarkInvoker.FUNCTION, Invoker.DEFAULT_NAMESPACE,
                AbstractMapper.DREAM_TEMPLATE_PARAM + "." + field + ".1");
    }
}
