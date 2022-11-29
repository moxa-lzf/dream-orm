package com.moxa.dream.mate.permission.inject;

import com.moxa.dream.antlr.smt.InvokerStatement;
import com.moxa.dream.antlr.smt.PackageStatement;
import com.moxa.dream.mate.permission.invoker.PermissionInjectInvoker;
import com.moxa.dream.system.config.MethodInfo;
import com.moxa.dream.system.inject.Inject;
import com.moxa.dream.system.util.InvokerUtil;

public class PermissionInject implements Inject {
    @Override
    public void inject(MethodInfo methodInfo) {
        PackageStatement statement = methodInfo.getStatement();
        InvokerStatement tenantStatement = InvokerUtil.wrapperInvoker(null,
                PermissionInjectInvoker.getName(), ",",
                statement.getStatement());
        statement.setStatement(tenantStatement);
    }
}
