package com.moxa.dream.template.mapper;

import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.core.session.Session;
import com.moxa.dream.system.mapper.MethodInfo;
import com.moxa.dream.system.table.TableInfo;
import com.moxa.dream.util.reflection.util.NonCollection;

public abstract class DeleteSqlMapper extends AbstractSqlMapper {
    public DeleteSqlMapper(Session session) {
        super(session);
    }

    @Override
    protected MethodInfo getMethodInfo(Configuration configuration, TableInfo tableInfo, Class type) {
        String table = tableInfo.getTable();
        String suffix = getSuffix(tableInfo);
        String sql = "delete from `" + table + "` " + suffix;
        return new MethodInfo.Builder(configuration)
                .rowType(NonCollection.class)
                .colType(Integer.class)
                .sql(sql)
                .build();
    }

    protected abstract String getSuffix(TableInfo tableInfo);
}
