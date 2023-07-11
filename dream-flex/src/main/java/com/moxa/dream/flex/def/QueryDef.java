package com.moxa.dream.flex.def;

import com.moxa.dream.antlr.smt.ListColumnStatement;
import com.moxa.dream.antlr.smt.PreSelectStatement;
import com.moxa.dream.antlr.smt.QueryStatement;
import com.moxa.dream.antlr.smt.SelectStatement;

public class QueryDef extends AbstractSqlDef {
    protected QueryStatement statement;

    public QueryDef() {
        this(new QueryStatement());
    }

    public QueryDef(QueryStatement statement) {
        this.statement = statement;
    }

    public SelectDef select(ColumnDef... columnDefs) {
        SelectStatement selectStatement = new SelectStatement();
        selectStatement.setPreSelect(new PreSelectStatement());
        ListColumnStatement listColumnStatement = new ListColumnStatement(",");
        for (ColumnDef columnDef : columnDefs) {
            listColumnStatement.add(columnDef.getStatement(true));
        }
        selectStatement.setSelectList(listColumnStatement);
        statement.setSelectStatement(selectStatement);
        return new SelectDef(statement);
    }

    @Override
    public QueryStatement getStatement() {
        return statement;
    }
}
