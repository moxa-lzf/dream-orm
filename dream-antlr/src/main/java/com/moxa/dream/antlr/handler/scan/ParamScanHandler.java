package com.moxa.dream.antlr.handler.scan;

import com.moxa.dream.antlr.config.Assist;
import com.moxa.dream.antlr.exception.InvokerException;
import com.moxa.dream.antlr.handler.AbstractHandler;
import com.moxa.dream.antlr.handler.Handler;
import com.moxa.dream.antlr.invoker.Invoker;
import com.moxa.dream.antlr.invoker.ScanInvoker;
import com.moxa.dream.antlr.smt.*;
import com.moxa.dream.antlr.sql.ToSQL;
import com.moxa.dream.antlr.util.InvokerUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParamScanHandler extends AbstractHandler {

    private final ConditionHandler conditionHandler;
    private final InsertHandler insertHandler;

    public ParamScanHandler(ScanInvoker.ScanInfo scanInfo) {
        conditionHandler = new ConditionHandler(scanInfo);
        insertHandler = new InsertHandler(scanInfo);
    }

    @Override
    protected Statement handlerBefore(Statement statement, Assist assist, ToSQL toSQL, List<Invoker> invokerList, int life) throws InvokerException {
        return statement;
    }

    @Override
    protected Handler[] handlerBound() {
        return new Handler[]{conditionHandler, insertHandler};
    }

    @Override
    protected boolean interest(Statement statement, Assist sqlAssist) {
        return statement instanceof InsertStatement || statement instanceof ConditionStatement;
    }


    class ConditionHandler extends AbstractHandler {
        private final ScanInvoker.ScanInfo scanInfo;

        public ConditionHandler(ScanInvoker.ScanInfo scanInfo) {
            this.scanInfo = scanInfo;
        }

        public void scanStatement(InvokerStatement invokerStatement) {
            Statement paramStatement = ((ListColumnStatement) invokerStatement.getParamStatement()).getColumnList()[0];
            if (paramStatement instanceof SymbolStatement.LetterStatement) {
                SymbolStatement.LetterStatement letterStatement = (SymbolStatement.LetterStatement) paramStatement;
                String field = letterStatement.getSymbol();
                Statement parentStatement = invokerStatement.getParentStatement();
                if (parentStatement instanceof ConditionStatement) {
                    ConditionStatement conditionStatement = (ConditionStatement) parentStatement;
                    OperStatement oper = conditionStatement.getOper();
                    if (oper instanceof OperStatement.ANDStatement) {
                        parentStatement = parentStatement.getParentStatement();
                        if (paramStatement instanceof ConditionStatement) {
                            conditionStatement = (ConditionStatement) parentStatement;
                        }
                    }
                    Statement left = conditionStatement.getLeft();
                    if (left instanceof SymbolStatement.LetterStatement) {
                        SymbolStatement.LetterStatement letter = (SymbolStatement.LetterStatement) left;
                        String database = null;
                        String table = null;
                        String column = letter.getSuffix();
                        String prefix = letter.getPrefix();
                        if (prefix != null) {
                            String[] s = prefix.split("\\.");
                            switch (s.length) {
                                case 1:
                                    table = s[0];
                                    break;
                                case 2:
                                    database = s[0];
                                    table = s[1];
                                    break;
                                default:
                                    break;
                            }
                        }
                        scanInfo.add(new ScanInvoker.ParamScanInfo(database, table, column, field));
                    }
                }
            }

        }

        @Override
        protected Statement handlerBefore(Statement statement, Assist assist, ToSQL toSQL, List<Invoker> invokerList, int life) throws InvokerException {
            scanStatement((InvokerStatement) statement);
            return statement;
        }

        @Override
        protected boolean interest(Statement statement, Assist sqlAssist) {
            return InvokerUtil.is$(statement);
        }
    }

    class InsertHandler extends AbstractHandler {

        private ScanInvoker.ScanInfo scanInfo;
        private List<String> columnList;
        private String table;

        public InsertHandler(ScanInvoker.ScanInfo scanInfo) {
            this.scanInfo = scanInfo;
        }

        @Override
        protected Handler[] handlerBound() {
            return new Handler[]{new ValuesHandler(this)};
        }

        @Override
        protected Statement handlerBefore(Statement statement, Assist assist, ToSQL toSQL, List<Invoker> invokerList, int life) throws InvokerException {
            InsertStatement insertStatement = (InsertStatement) statement;
            table = insertStatement.getTable().getSymbol();
            if (insertStatement.getParams() != null) {
                columnList = Arrays.stream(((ListColumnStatement) ((BraceStatement) insertStatement.getParams()).getStatement()).getColumnList())
                        .map(column -> ((SymbolStatement.LetterStatement) column).getSymbol())
                        .collect(Collectors.toList());
            }
            return statement;
        }

        @Override
        protected boolean interest(Statement statement, Assist sqlAssist) {
            return statement instanceof InsertStatement;
        }

        class ValuesHandler extends AbstractHandler {
            private InsertHandler insertHandler;

            public ValuesHandler(InsertHandler insertHandler) {
                this.insertHandler = insertHandler;
            }

            @Override
            protected Statement handlerBefore(Statement statement, Assist assist, ToSQL toSQL, List<Invoker> invokerList, int life) throws InvokerException {
                if (insertHandler.columnList != null) {
                    InsertStatement.ValuesStatement valuesStatement = (InsertStatement.ValuesStatement) statement;
                    Statement[] columnList = ((ListColumnStatement) ((BraceStatement) ((ListColumnStatement) valuesStatement.getStatement()).getColumnList()[0]).getStatement()).getColumnList();
                    for (int i = 0; i < columnList.length; i++) {
                        if (InvokerUtil.is$(columnList[i])) {
                            String field = ((SymbolStatement.LetterStatement) ((ListColumnStatement) ((InvokerStatement) columnList[i]).getParamStatement()).getColumnList()[0]).getSymbol();
                            scanInfo.add(new ScanInvoker.ParamScanInfo(null, table, insertHandler.columnList.get(i), field));
                        }
                    }
                }
                return statement;
            }

            @Override
            protected boolean interest(Statement statement, Assist sqlAssist) {
                return statement instanceof InsertStatement.ValuesStatement;
            }
        }
    }
}
