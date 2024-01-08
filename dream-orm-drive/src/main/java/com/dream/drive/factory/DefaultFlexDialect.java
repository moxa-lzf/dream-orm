package com.dream.drive.factory;

import com.dream.antlr.invoker.Invoker;
import com.dream.antlr.sql.ToMySQL;
import com.dream.antlr.sql.ToSQL;
import com.dream.flex.dialect.AbstractFlexDialect;
import com.dream.mate.logic.inject.LogicHandler;
import com.dream.mate.logic.invoker.LogicInvoker;
import com.dream.mate.permission.inject.PermissionHandler;
import com.dream.mate.permission.invoker.PermissionGetInvoker;
import com.dream.mate.permission.invoker.PermissionInjectInvoker;
import com.dream.mate.tenant.inject.TenantHandler;
import com.dream.mate.tenant.invoker.TenantGetInvoker;
import com.dream.mate.tenant.invoker.TenantInjectInvoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultFlexDialect extends AbstractFlexDialect {
    private TenantHandler tenantHandler;
    private PermissionHandler permissionHandler;
    private LogicHandler logicHandler;

    public DefaultFlexDialect() {
        this(new ToMySQL());
    }

    public DefaultFlexDialect(ToSQL toSQL) {
        super(toSQL);
    }

    public DefaultFlexDialect tenantHandler(TenantHandler tenantHandler) {
        this.tenantHandler = tenantHandler;
        return this;
    }

    public DefaultFlexDialect permissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
        return this;
    }

    public DefaultFlexDialect logicHandler(LogicHandler logicHandler) {
        this.logicHandler = logicHandler;
        return this;
    }

    @Override
    protected List<Invoker> invokerList() {
        List<Invoker> invokerList = new ArrayList<>(3);
        if (tenantHandler != null) {
            invokerList.add(new TenantInjectInvoker(tenantHandler));
        }
        if (permissionHandler != null) {
            invokerList.add(new PermissionInjectInvoker(permissionHandler));
        }
        if (logicHandler != null) {
            invokerList.add(new LogicInvoker(logicHandler));
        }
        return invokerList;
    }

    @Override
    protected Invoker[] defaultInvokers() {
        List<Invoker> invokerList = new ArrayList<>(4);
        Invoker[] invokers = super.defaultInvokers();
        invokerList.addAll(Arrays.asList(invokers));
        if (tenantHandler != null) {
            invokerList.add(new TenantGetInvoker(tenantHandler));
        }
        if (permissionHandler != null) {
            invokerList.add(new PermissionGetInvoker(permissionHandler));
        }
        return invokerList.toArray(new Invoker[invokerList.size()]);
    }
}