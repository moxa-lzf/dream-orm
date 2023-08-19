package com.dream.tdengine.mapper;

import com.dream.flex.def.FunctionDef;
import com.dream.tdengine.def.TdChainDeleteTableDef;
import com.dream.tdengine.def.TdChainInsertIntoTableDef;
import com.dream.tdengine.def.TdChainSelectDef;
import com.dream.tdengine.def.TdChainUpdateColumnDef;
import com.dream.tdengine.factory.ChainDeleteCreatorFactory;
import com.dream.tdengine.factory.ChainInsertCreatorFactory;
import com.dream.tdengine.factory.ChainQueryCreatorFactory;
import com.dream.tdengine.factory.ChainUpdateCreatorFactory;
import com.dream.flex.def.ColumnDef;
import com.dream.flex.def.TableDef;
import com.dream.flex.mapper.FlexMapper;

public class DefaultFlexTdChainMapper implements FlexTdChainMapper {
    private FlexMapper flexMapper;

    public DefaultFlexTdChainMapper(FlexMapper flexMapper) {
        this.flexMapper = flexMapper;
    }

    @Override
    public TdChainSelectDef select(ColumnDef... columnDefs) {
        return new ChainQueryCreatorFactory(flexMapper).newQueryDef().select(columnDefs);
    }

    @Override
    public TdChainUpdateColumnDef update(TableDef tableDef) {
        return new ChainUpdateCreatorFactory(flexMapper).newUpdateDef().update(tableDef);
    }

    @Override
    public TdChainInsertIntoTableDef insertInto(String subTableName) {
        return new ChainInsertCreatorFactory(flexMapper).newInsertDef().insertInto(FunctionDef.table(subTableName));
    }

    @Override
    public TdChainDeleteTableDef delete(TableDef tableDef) {
        return new ChainDeleteCreatorFactory(flexMapper).newDeleteDef().delete(tableDef);
    }
}
