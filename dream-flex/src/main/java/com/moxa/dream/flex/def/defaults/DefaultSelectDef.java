package com.moxa.dream.flex.def.defaults;

import com.moxa.dream.antlr.smt.QueryStatement;
import com.moxa.dream.flex.def.AbstractQuery;
import com.moxa.dream.flex.def.QueryCreatorFactory;
import com.moxa.dream.flex.def.SelectDef;

public class DefaultSelectDef extends AbstractQuery implements SelectDef {
    public DefaultSelectDef(QueryStatement queryStatement, QueryCreatorFactory queryCreatorFactory) {
        super(queryStatement, queryCreatorFactory);
    }
}
