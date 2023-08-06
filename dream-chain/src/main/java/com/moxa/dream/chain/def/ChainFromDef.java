package com.moxa.dream.chain.def;

import com.moxa.dream.antlr.smt.QueryStatement;
import com.moxa.dream.flex.def.FromDef;
import com.moxa.dream.flex.def.QueryCreatorFactory;
import com.moxa.dream.flex.mapper.FlexMapper;

public class ChainFromDef extends AbstractChainQuery implements FromDef<ChainFromDef,ChainWhereDef>{
    public ChainFromDef(QueryStatement queryStatement, QueryCreatorFactory queryCreatorFactory, FlexMapper flexMapper) {
        super(queryStatement, queryCreatorFactory,flexMapper);
    }
}
