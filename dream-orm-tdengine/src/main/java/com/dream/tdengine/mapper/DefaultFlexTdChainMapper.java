package com.dream.tdengine.mapper;

import com.dream.antlr.sql.ToSQL;
import com.dream.flex.def.ColumnDef;
import com.dream.flex.def.FunctionDef;
import com.dream.flex.def.TableDef;
import com.dream.flex.mapper.DefaultFlexMapper;
import com.dream.flex.mapper.FlexMapper;
import com.dream.struct.factory.DefaultStructFactory;
import com.dream.system.core.session.Session;
import com.dream.tdengine.def.TdChainDeleteWhereDef;
import com.dream.tdengine.def.TdChainFromDef;
import com.dream.tdengine.def.TdChainInsertIntoColumnsDef;
import com.dream.tdengine.def.TdChainUpdateColumnDef;
import com.dream.tdengine.factory.TdChainFlexDeleteFactory;
import com.dream.tdengine.factory.TdChainFlexInsertFactory;
import com.dream.tdengine.factory.TdChainFlexQueryFactory;
import com.dream.tdengine.factory.TdChainFlexUpdateFactory;
import com.dream.tdengine.sql.ToTdEngine;

public class DefaultFlexTdChainMapper implements FlexTdChainMapper {
    private FlexMapper flexMapper;

    public DefaultFlexTdChainMapper(Session session) {
        this(session, new ToTdEngine());
    }

    public DefaultFlexTdChainMapper(Session session, ToSQL toSQL) {
        this.flexMapper = new DefaultFlexMapper(session, new DefaultStructFactory(toSQL));
    }

    @Override
    public TdChainFromDef select(ColumnDef... columnDefs) {
        return new TdChainFlexQueryFactory(flexMapper).newSelectDef().select(columnDefs);
    }

    @Override
    public TdChainFromDef select(boolean distinct, ColumnDef... columnDefs) {
        return new TdChainFlexQueryFactory(flexMapper).newSelectDef().select(distinct, columnDefs);
    }

    @Override
    public TdChainUpdateColumnDef update(TableDef tableDef) {
        return new TdChainFlexUpdateFactory(flexMapper).newUpdateTableDef().update(tableDef);
    }

    @Override
    public TdChainInsertIntoColumnsDef insertInto(String subTableName) {
        return new TdChainFlexInsertFactory(flexMapper).newInsertIntoTableDef().insertInto(FunctionDef.table(subTableName));
    }

    @Override
    public TdChainDeleteWhereDef delete(TableDef tableDef) {
        return new TdChainFlexDeleteFactory(flexMapper).newDeleteTableDef().delete(tableDef);
    }
}
