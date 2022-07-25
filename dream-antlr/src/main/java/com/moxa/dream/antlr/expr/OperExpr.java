package com.moxa.dream.antlr.expr;

import com.moxa.dream.antlr.config.ExprInfo;
import com.moxa.dream.antlr.config.ExprType;
import com.moxa.dream.antlr.read.ExprReader;
import com.moxa.dream.antlr.smt.BraceStatement;
import com.moxa.dream.antlr.smt.OperStatement;
import com.moxa.dream.antlr.smt.Statement;

public class OperExpr extends TreeExpr {
    public static final ExprType[] OPER = {ExprType.ADD, ExprType.SUB, ExprType.STAR, ExprType.DIVIDE, ExprType.MOD, ExprType.LLM, ExprType.RRM, ExprType.BITAND, ExprType.BITOR, ExprType.BITXOR};
    private CompareExpr compareExpr;

    public OperExpr(ExprReader exprReader) {
        this(exprReader, () -> new ColumnExpr(exprReader));
    }

    public OperExpr(ExprReader exprReader, CompareExpr compareExpr) {
        this(exprReader);
        this.compareExpr = compareExpr;
    }

    public OperExpr(ExprReader exprReader, Helper helper) {
        super(exprReader, helper);
        setExprTypes(ExprType.ADD, ExprType.SUB, ExprType.HELP, ExprType.LBRACE);
    }

    @Override
    protected Statement exprAdd(ExprInfo exprInfo) {
        push();
        OperStatement.ADDStatement addStatement = new OperStatement.ADDStatement();
        if ((cur.getOper() != null && cur.getRight() == null) || cur.getLeft() == null)
            addStatement.setLevel(13);
        exprTree(addStatement);
        setExprTypes(ExprType.SUB, ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprSub(ExprInfo exprInfo) {
        push();
        OperStatement.SUBStatement subStatement = new OperStatement.SUBStatement();
        if ((cur.getOper() != null && cur.getRight() == null) || cur.getLeft() == null)
            subStatement.setLevel(13);
        exprTree(subStatement);
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprStar(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.STARStatement());
        setExprTypes(ExprType.SUB, ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprLlm(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.LLMStatement());
        setExprTypes(ExprType.SUB, ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprRrm(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.RRMStatement());
        setExprTypes(ExprType.SUB, ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprDivide(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.DIVIDEStatement());
        setExprTypes(ExprType.SUB, ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprMod(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.MODStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    public Statement nil() {
        if (compareExpr == null) {
            if (top.getOper() != null)
                return top;
            else return top.getLeft();
        } else return compareExpr.nil();
    }

    @Override
    protected Statement exprAnd(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.ANDStatement());
        this.setExprTypes(ExprType.HELP, ExprType.LBRACE);
        return expr();
    }

    @Override
    protected Statement exprBitAnd(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.BITANDStatement());
        this.setExprTypes(ExprType.HELP, ExprType.LBRACE);
        return expr();
    }

    @Override
    protected Statement exprBitOr(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.BITORStatement());
        this.setExprTypes(ExprType.HELP, ExprType.LBRACE);
        return expr();
    }

    @Override
    protected Statement exprBitXor(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.BITXORStatement());
        this.setExprTypes(ExprType.HELP, ExprType.LBRACE);
        return expr();
    }

    @Override
    protected Statement exprOr(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.ORStatement());
        this.setExprTypes(ExprType.HELP, ExprType.LBRACE);
        return expr();
    }

    @Override
    protected Statement exprLt(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.LTStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprLeq(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.LEQStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();

    }

    @Override
    protected Statement exprGt(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.GTStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprGeq(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.GEQStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprEq(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.EQStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }

    @Override
    protected Statement exprNeq(ExprInfo exprInfo) {
        push();
        exprTree(new OperStatement.NEQStatement());
        setExprTypes(ExprType.LBRACE, ExprType.HELP);
        return expr();
    }


    @Override
    protected Statement exprLBrace(ExprInfo exprInfo) {
        BraceExpr braceExpr = new BraceExpr(exprReader);
        BraceStatement braceStatement = (BraceStatement) braceExpr.expr();
        exprTree(braceStatement);
        setExprTypes(OPER).addExprTypes(ExprType.NIL);
        return expr();
    }

    @Override
    public Statement exprHelp(Statement statement) {
        exprTree(statement);
        setExprTypes(OPER).addExprTypes(ExprType.NIL);
        return expr();
    }

    @Override
    protected void exprTree(OperStatement oper) {
        if (compareExpr == null)
            super.exprTree(oper);
        else compareExpr.exprTree(oper);
    }

    @Override
    protected void exprTree(Statement statement) {
        if (compareExpr == null)
            super.exprTree(statement);
        else compareExpr.exprTree(statement);
    }
}
