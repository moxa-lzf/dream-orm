package com.moxa.dream.flex.def;

import com.moxa.dream.antlr.smt.BraceStatement;
import com.moxa.dream.antlr.smt.ListColumnStatement;
import com.moxa.dream.antlr.smt.Statement;


public interface InsertIntoTableDef<T extends InsertIntoColumnsDef> extends Insert {
    default T columns(ColumnDef... columnDefs) {
        ListColumnStatement paramsListStatement = new ListColumnStatement(",");
        for (ColumnDef columnDef : columnDefs) {
            Statement statement = columnDef.getStatement();
            if (statement instanceof ListColumnStatement) {
                Statement[] columnList = ((ListColumnStatement) statement).getColumnList();
                statement = columnList[columnList.length - 1];
            }
            paramsListStatement.add(statement);
        }
        statement().setColumns(new BraceStatement(paramsListStatement));
        return (T) creatorFactory().newInsertIntoColumnsDef(statement());
    }
}
