package com.dream.tdengine.def;

import com.dream.antlr.smt.QueryStatement;
import com.dream.flex.def.WhereDef;
import com.dream.flex.factory.QueryCreatorFactory;
import com.dream.flex.mapper.FlexMapper;

public class TdChainWhereDef extends AbstractTdChainQuery implements WhereDef<TdChainGroupByDef, TdChainHavingDef, TdChainOrderByDef, TdChainLimitDef, TdChainUnionDef, TdChainForUpdateDef> {
    public TdChainWhereDef(QueryStatement queryStatement, QueryCreatorFactory queryCreatorFactory, FlexMapper flexMapper) {
        super(queryStatement, queryCreatorFactory, flexMapper);
    }
}
