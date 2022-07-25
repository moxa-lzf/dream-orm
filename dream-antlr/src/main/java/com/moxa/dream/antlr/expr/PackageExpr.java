package com.moxa.dream.antlr.expr;

import com.moxa.dream.antlr.config.ExprInfo;
import com.moxa.dream.antlr.config.ExprType;
import com.moxa.dream.antlr.read.ExprReader;
import com.moxa.dream.antlr.smt.BraceStatement;
import com.moxa.dream.antlr.smt.PackageStatement;
import com.moxa.dream.antlr.smt.Statement;

public class PackageExpr extends HelperExpr {
    private final PackageStatement statement = new PackageStatement();

    public PackageExpr(ExprReader exprReader) {
        this(exprReader, () -> new CrudExpr(exprReader));
    }

    public PackageExpr(ExprReader exprReader, Helper helper) {
        super(exprReader, helper);
        setExprTypes(ExprType.LBRACE, ExprType.INVOKER, ExprType.HELP, ExprType.ACC);
    }

    @Override
    protected Statement exprLBrace(ExprInfo exprInfo) {
        push();
        BraceStatement braceStatement = new BraceStatement();
        braceStatement.setStatement(this.expr());
        statement.setStatement(braceStatement);
        setExprTypes(ExprType.RBRACE);
        return expr();
    }

    @Override
    protected Statement exprRBrace(ExprInfo exprInfo) {
        push();
        setExprTypes(ExprType.ACC);
        return expr();
    }

    @Override
    protected Statement exprInvoker(ExprInfo exprInfo) {
        statement.setStatement(new InvokerExpr(exprReader).expr());
        setExprTypes(ExprType.ACC);
        return expr();
    }

    @Override
    protected Statement exprAcc(ExprInfo exprInfo) {
        setExprTypes(ExprType.NIL);
        return expr();
    }

    @Override
    public Statement exprHelp(Statement statement) {
        this.statement.setStatement(statement);
        setExprTypes(ExprType.ACC);
        return expr();
    }

    @Override
    protected Statement nil() {
        exprReader.close();
        return statement;
    }
}
