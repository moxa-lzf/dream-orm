package com.moxa.dream.antlr.invoker;

import com.moxa.dream.antlr.exception.InvokerException;
import com.moxa.dream.antlr.handler.Handler;
import com.moxa.dream.antlr.smt.InvokerStatement;
import com.moxa.dream.antlr.sql.ToAssist;
import com.moxa.dream.antlr.sql.ToSQL;
import com.moxa.dream.util.common.ObjectWrapper;

import java.util.ArrayList;
import java.util.List;

public class $Invoker extends AbstractInvoker {
    ObjectWrapper paramWrapper;
    private final List<ParamInfo> paramInfoList = new ArrayList<>();

    @Override
    public void init(ToAssist assist) {
        paramWrapper = assist.getCustom(ObjectWrapper.class);
    }

    @Override
    public String invoker(InvokerStatement invokerStatement, ToAssist assist, ToSQL toSQL, List<Invoker> invokerList) throws InvokerException {
        String paramName = toSQL.toStr(invokerStatement.getParamStatement(), assist, invokerList);
        ParamInfo paramInfo = new ParamInfo(paramName, paramWrapper.get(paramName));
        paramInfoList.add(paramInfo);
        return "?";
    }

    @Override
    public Handler[] handler() {
        return new Handler[0];
    }

    public List<ParamInfo> getParamInfoList() {
        return paramInfoList;
    }

    public static class ParamInfo {
        private final String paramName;
        private Object paramValue;

        public ParamInfo(String paramName) {
            this(paramName, null);
        }

        public ParamInfo(String paramName, Object paramValue) {
            this.paramName = paramName;
            this.paramValue = paramValue;
        }

        public String getParamName() {
            return paramName;
        }

        public Object getParamValue() {
            return paramValue;
        }

        public void setParamValue(Object paramValue) {
            this.paramValue = paramValue;
        }
    }

}