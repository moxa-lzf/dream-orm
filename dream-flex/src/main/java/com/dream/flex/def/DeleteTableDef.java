package com.dream.flex.def;

import com.dream.antlr.smt.WhereStatement;

public interface DeleteTableDef<T extends DeleteWhereDef> extends Delete {
    default T where(ConditionDef conditionDef) {
        WhereStatement whereStatement = new WhereStatement();
        whereStatement.setCondition(conditionDef.getStatement());
        statement().setWhere(whereStatement);
        return (T) creatorFactory().newDeleteWhereDef(statement());
    }
}