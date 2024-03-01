package com.dream.antlr.expr;

import com.dream.antlr.config.ExprInfo;
import com.dream.antlr.config.ExprType;
import com.dream.antlr.exception.AntlrException;
import com.dream.antlr.read.ExprReader;
import com.dream.antlr.smt.Statement;
import com.dream.antlr.util.ExprUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 翻译顶级抽象语法解析类
 */
public abstract class SqlExpr {

    //可解析的单词类型
    protected Set<ExprType> acceptSet = new HashSet<>();
    //单词读取流
    protected ExprReader exprReader;
    //本语法器是否可解析单词
    protected boolean self;

    public SqlExpr(ExprReader exprReader) {
        this.exprReader = exprReader;
    }

    /**
     * 读取下一个词的具体信息
     *
     * @return
     * @throws AntlrException
     */
    public ExprInfo push() throws AntlrException {
        ExprInfo exprInfo = exprReader.push();
        exprReader.push(exprInfo);
        return exprInfo;
    }

    /**
     * 根据当前词，选择对应的语法解析器
     *
     * @return
     * @throws AntlrException
     */
    public Statement expr() throws AntlrException {
        //获取最后读入的单词信息
        ExprInfo exprInfo = exprReader.getLastInfo();
        if (exprInfo == null) {
            exprInfo = push();
        }
        //获取单词类型
        ExprType exprType = exprInfo.getExprType();
        Statement statement;
        //判断本语法器是否可解析单词
        if (exprBefore(exprType)) {
            statement = expr(exprInfo);
        } else {
            statement = exprNil(exprInfo);
        }
        return statement;
    }

    protected Statement expr(ExprInfo exprInfo) throws AntlrException {
        Statement statement;
        switch (exprInfo.getExprType()) {
            case TINYINT:
                statement = exprTinyInt(exprInfo);
                break;
            case SMALLINT:
                statement = exprSmallInt(exprInfo);
                break;
            case MEDIUMINT:
                statement = exprMediumInt(exprInfo);
                break;
            case INT:
                statement = exprInt(exprInfo);
                break;
            case BIGINT:
                statement = exprBigInt(exprInfo);
                break;
            case LONG:
                statement = exprLong(exprInfo);
                break;
            case FLOAT:
                statement = exprFloat(exprInfo);
                break;
            case DOUBLE:
                statement = exprDouble(exprInfo);
                break;
            case DOT:
                statement = exprDot(exprInfo);
                break;
            case STR:
                statement = exprStr(exprInfo);
                break;
            case JAVA_STR:
                statement = exprJavaStr(exprInfo);
                break;
            case LETTER:
                statement = exprLetter(exprInfo);
                break;
            case NUMBER:
                statement = exprNumber(exprInfo);
                break;
            case LIKE:
                statement = exprLike(exprInfo);
                break;
            case BETWEEN:
                statement = exprBetween(exprInfo);
                break;
            case LBRACE:
                statement = exprLBrace(exprInfo);
                break;
            case RBRACE:
                statement = exprRBrace(exprInfo);
                break;
            case INTERVAL:
                statement = exprInterval(exprInfo);
                break;
            case COMMA:
                statement = exprComma(exprInfo);
                break;
            case SINGLE_MARK:
                statement = exprSingleMark(exprInfo);
                break;
            case MARK:
                statement = exprMark(exprInfo);
                break;
            case ON:
                statement = exprOn(exprInfo);
                break;
            case AS:
                statement = exprAs(exprInfo);
                break;
            case ASC:
                statement = exprAsc(exprInfo);
                break;
            case DESC:
                statement = exprDesc(exprInfo);
                break;
            case ASCII:
                statement = exprAscii(exprInfo);
                break;
            case CHARACTER_LENGTH:
            case CHAR_LENGTH:
                statement = exprCharLength(exprInfo);
                break;
            case LENGTH:
                statement = exprLength(exprInfo);
                break;
            case CONCAT_WS:
                statement = exprConcatWs(exprInfo);
                break;
            case INSTR:
                statement = exprInstr(exprInfo);
                break;
            case LOCATE:
                statement = exprLocate(exprInfo);
                break;
            case LCASE:
                statement = exprLcase(exprInfo);
                break;
            case LOWER:
                statement = exprLower(exprInfo);
                break;
            case LTRIM:
                statement = exprLtrim(exprInfo);
                break;
            case REPEAT:
                statement = exprRepeat(exprInfo);
                break;
            case REVERSE:
                statement = exprReverse(exprInfo);
                break;
            case REPLACE:
                statement = exprReplace(exprInfo);
                break;
            case RTRIM:
                statement = exprRtrim(exprInfo);
                break;
            case STRCMP:
                statement = exprStrcmp(exprInfo);
                break;
            case SEPARATOR:
                statement = exprSeparator(exprInfo);
                break;
            case SUBSTR:
            case SUBSTRING:
                statement = exprSubStr(exprInfo);
                break;
            case SPACE:
                statement = exprSpace(exprInfo);
                break;
            case TRIM:
                statement = exprTrim(exprInfo);
                break;
            case UCASE:
            case UPPER:
                statement = exprUpper(exprInfo);
                break;
            case LPAD:
                statement = exprLpad(exprInfo);
                break;
            case RPAD:
                statement = exprRpad(exprInfo);
                break;
            case DATABASE:
                statement = exprDatabase(exprInfo);
                break;
            case TABLE:
                statement = exprTable(exprInfo);
                break;
            case ABS:
                statement = exprAbs(exprInfo);
                break;
            case AVG:
                statement = exprAvg(exprInfo);
                break;
            case ACOS:
                statement = exprAcos(exprInfo);
                break;
            case ASIN:
                statement = exprAsin(exprInfo);
                break;
            case SIN:
                statement = exprSin(exprInfo);
                break;
            case ATAN:
                statement = exprAtan(exprInfo);
                break;
            case ALL:
                statement = exprAll(exprInfo);
                break;
            case CEIL:
                statement = exprCeil(exprInfo);
                break;
            case CEILING:
                statement = exprCeiling(exprInfo);
                break;
            case COS:
                statement = exprCos(exprInfo);
                break;
            case COT:
                statement = exprCot(exprInfo);
                break;
            case COUNT:
                statement = exprCount(exprInfo);
                break;
            case EXP:
                statement = exprExp(exprInfo);
                break;
            case FLOOR:
                statement = exprFloor(exprInfo);
                break;
            case LN:
                statement = exprLn(exprInfo);
                break;
            case LOG:
                statement = exprLog(exprInfo);
                break;
            case LOG10:
                statement = exprLog10(exprInfo);
                break;
            case LOG2:
                statement = exprLog2(exprInfo);
                break;
            case MAX:
                statement = exprMax(exprInfo);
                break;
            case MIN:
                statement = exprMin(exprInfo);
                break;
            case PI:
                statement = exprPi(exprInfo);
                break;
            case POW:
                statement = exprPow(exprInfo);
                break;
            case POWER:
                statement = exprPower(exprInfo);
                break;
            case RAND:
                statement = exprRand(exprInfo);
                break;
            case ROUND:
                statement = exprRound(exprInfo);
                break;
            case SIGN:
                statement = exprSign(exprInfo);
                break;
            case SQRT:
                statement = exprSqrt(exprInfo);
                break;
            case SUM:
                statement = exprSum(exprInfo);
                break;
            case TAN:
                statement = exprTan(exprInfo);
                break;
            case CREATE:
                statement = exprCreate(exprInfo);
                break;
            case ALTER:
                statement = exprAlter(exprInfo);
                break;
            case TRUNCATE:
                statement = exprTruncate(exprInfo);
                break;
            case DROP:
                statement = exprDrop(exprInfo);
                break;
            case COLUMN:
                statement = exprColumn(exprInfo);
                break;
            case RENAME:
                statement = exprRename(exprInfo);
                break;
            case TO:
                statement = exprTo(exprInfo);
                break;
            case MODIFY:
                statement = exprModify(exprInfo);
                break;
            case ORDER:
                statement = exprOrder(exprInfo);
                break;
            case CURDATE:
                statement = exprCurDate(exprInfo);
                break;
            case DATEDIFF:
                statement = exprDateDiff(exprInfo);
                break;
            case DATE_ADD:
                statement = exprDateAdd(exprInfo);
                break;
            case DATE_SUB:
                statement = exprDateSub(exprInfo);
                break;
            case DATE_FORMAT:
                statement = exprDateFormat(exprInfo);
                break;
            case DAY:
            case DAYOFMONTH:
                statement = exprDay(exprInfo);
                break;
            case DAYOFWEEK:
                statement = exprDayOfWeek(exprInfo);
                break;
            case DAYOFYEAR:
                statement = exprDayOfYear(exprInfo);
                break;
            case HOUR:
                statement = exprHour(exprInfo);
                break;
            case LAST_DAY:
                statement = exprLastDay(exprInfo);
                break;
            case MINUTE:
                statement = exprMinute(exprInfo);
                break;
            case MONTH:
                statement = exprMonth(exprInfo);
                break;
            case NOW:
                statement = exprNow(exprInfo);
                break;
            case SYSDATE:
                statement = exprSysDate(exprInfo);
                break;
            case QUARTER:
                statement = exprQuarter(exprInfo);
                break;
            case SECOND:
                statement = exprSecond(exprInfo);
                break;
            case TIME:
                statement = exprTime(exprInfo);
                break;
            case WEEK:
                statement = exprWeek(exprInfo);
                break;
            case WEEKOFYEAR:
                statement = exprWeekOfYear(exprInfo);
                break;
            case YEAR:
                statement = exprYear(exprInfo);
                break;
            case STR_TO_DATE:
                statement = exprStrToDate(exprInfo);
                break;
            case MY_FUNCTION:
                statement = exprMyFunction(exprInfo);
                break;
            case CONVERT:
                statement = exprConvert(exprInfo);
                break;
            case CAST:
                statement = exprCast(exprInfo);
                break;
            case CHAR:
                statement = exprChar(exprInfo);
                break;
            case VARCHAR:
                statement = exprVarChar(exprInfo);
                break;
            case TEXT:
                statement = exprText(exprInfo);
                break;
            case BLOB:
                statement = exprBlob(exprInfo);
                break;
            case UNIX_TIMESTAMP:
                statement = exprUnixTimeStamp(exprInfo);
                break;
            case FROM_UNIXTIME:
                statement = exprFromUnixTime(exprInfo);
                break;
            case DATE:
                statement = exprDate(exprInfo);
                break;
            case DATETIME:
                statement = exprDateTime(exprInfo);
                break;
            case TIMESTAMP:
                statement = exprTimeStamp(exprInfo);
                break;
            case SIGNED:
                statement = exprSigned(exprInfo);
                break;
            case INTEGER:
                statement = exprInteger(exprInfo);
                break;
            case DECIMAL:
                statement = exprDecimal(exprInfo);
                break;
            case COALESCE:
                statement = exprCoalesce(exprInfo);
                break;
            case CONCAT:
                statement = exprConcat(exprInfo);
                break;
            case GROUP_CONCAT:
                statement = exprGroupConcat(exprInfo);
                break;
            case FIND_IN_SET:
                statement = exprFindInSet(exprInfo);
                break;
            case IFNULL:
                statement = exprIfNull(exprInfo);
                break;
            case IF:
                statement = exprIf(exprInfo);
                break;
            case NULLIF:
                statement = exprNullIf(exprInfo);
                break;
            case OFFSET:
                statement = exprOffSet(exprInfo);
                break;
            case UNION:
                statement = exprUnion(exprInfo);
                break;
            case FOR:
                statement = exprFor(exprInfo);
                break;
            case NOWAIT:
                statement = exprNoWait(exprInfo);
                break;
            case ISNULL:
                statement = exprIsNull(exprInfo);
                break;
            case CASE:
                statement = exprCase(exprInfo);
                break;
            case WHEN:
                statement = exprWhen(exprInfo);
                break;
            case THEN:
                statement = exprThen(exprInfo);
                break;
            case ELSE:
                statement = exprElse(exprInfo);
                break;
            case END:
                statement = exprEnd(exprInfo);
                break;
            case SELECT:
                statement = exprSelect(exprInfo);
                break;
            case FROM:
                statement = exprFrom(exprInfo);
                break;
            case WHERE:
                statement = exprWhere(exprInfo);
                break;
            case HAVING:
                statement = exprHaving(exprInfo);
                break;
            case LIMIT:
                statement = exprLimit(exprInfo);
                break;
            case DISTINCT:
                statement = exprDistinct(exprInfo);
                break;
            case AND:
                statement = exprAnd(exprInfo);
                break;
            case BITAND://&
                statement = exprBitAnd(exprInfo);
                break;
            case BITOR://|
                statement = exprBitOr(exprInfo);
                break;
            case BITXOR://^
                statement = exprBitXor(exprInfo);
                break;
            case OR:
                statement = exprOr(exprInfo);
                break;
            case EQ:
                statement = exprEq(exprInfo);
                break;
            case IS:
                statement = exprIs(exprInfo);
                break;
            case IN:
                statement = exprIn(exprInfo);
                break;
            case NOT:
                statement = exprNot(exprInfo);
                break;
            case NULL:
                statement = exprNull(exprInfo);
                break;
            case EXISTS:
                statement = exprExists(exprInfo);
                break;
            case LT:
                statement = exprLt(exprInfo);
                break;
            case LLM:
                statement = exprLlm(exprInfo);
                break;
            case RRM:
                statement = exprRrm(exprInfo);
                break;
            case GT:
                statement = exprGt(exprInfo);
                break;
            case LEQ:
                statement = exprLeq(exprInfo);
                break;
            case GEQ:
                statement = exprGeq(exprInfo);
                break;
            case NEQ:
                statement = exprNeq(exprInfo);
                break;
            case LEFT:
                statement = exprLeft(exprInfo);
                break;
            case GROUP:
                statement = exprGroup(exprInfo);
                break;
            case BY:
                statement = exprBy(exprInfo);
                break;
            case INNER:
                statement = exprInner(exprInfo);
                break;
            case RIGHT:
                statement = exprRight(exprInfo);
                break;
            case CROSS:
                statement = exprCross(exprInfo);
                break;
            case OUTER:
                statement = exprOuter(exprInfo);
                break;
            case JOIN:
                statement = exprJoin(exprInfo);
                break;
            case ADD:
                statement = exprAdd(exprInfo);
                break;
            case SUB:
                statement = exprSub(exprInfo);
                break;
            case STAR:
                statement = exprStar(exprInfo);
                break;
            case DIVIDE:
                statement = exprDivide(exprInfo);
                break;
            case MOD:
                statement = exprMod(exprInfo);
                break;
            case INSERT:
                statement = exprInsert(exprInfo);
                break;
            case INTO:
                statement = exprInto(exprInfo);
                break;
            case VALUES:
                statement = exprValues(exprInfo);
                break;
            case DELETE:
                statement = exprDelete(exprInfo);
                break;
            case UPDATE:
                statement = exprUpdate(exprInfo);
                break;
            case SET:
                statement = exprSet(exprInfo);
                break;
            case ROW_NUMBER:
                statement = exprRowNumber(exprInfo);
                break;
            case OVER:
                statement = exprOver(exprInfo);
                break;
            case PARTITION:
                statement = exprPartition(exprInfo);
                break;
            case INVOKER:
                statement = exprInvoker(exprInfo);
                break;
            case COLON:
                statement = exprColon(exprInfo);
                break;
            case DOLLAR:
                statement = exprDollar(exprInfo);
                break;
            case SHARP:
                statement = exprSharp(exprInfo);
                break;
            case TO_CHAR:
                statement = exprToChar(exprInfo);
                break;
            case TO_NUMBER:
                statement = exprToNumber(exprInfo);
                break;
            case TO_DATE:
                statement = exprToDate(exprInfo);
                break;
            case TO_TIMESTAMP:
                statement = exprToTimeStamp(exprInfo);
                break;
            case AUTO_INCREMENT:
                statement = exprAutoIncrement(exprInfo);
                break;
            case CONSTRAINT:
                statement = exprConstraint(exprInfo);
                break;
            case PRIMARY:
                statement = exprPrimary(exprInfo);
                break;
            case FOREIGN:
                statement = exprForeign(exprInfo);
                break;
            case REFERENCES:
                statement = exprReferences(exprInfo);
                break;
            case KEY:
                statement = exprKey(exprInfo);
                break;
            case ENGINE:
                statement = exprEngine(exprInfo);
                break;
            case CHARSET:
                statement = exprCharset(exprInfo);
                break;
            case COMMENT:
                statement = exprComment(exprInfo);
                break;
            case DEFAULT:
                statement = exprDefault(exprInfo);
                break;
            case ACC:
                statement = exprAcc(exprInfo);
                break;
            default:
                statement = exprNil(exprInfo);
                break;
        }
        return statement;
    }

    protected Statement exprSingleMark(ExprInfo exprInfo) throws AntlrException {
        return exprSymbol(exprInfo);
    }

    protected Statement exprMark(ExprInfo exprInfo) throws AntlrException {
        return exprSymbol(exprInfo);
    }

    protected Statement exprUnion(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprFor(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprNoWait(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprOffSet(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAutoIncrement(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprConstraint(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprPrimary(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprForeign(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprReferences(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprKey(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprEngine(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprCharset(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprComment(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprDefault(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprInvoker(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprColon(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprDollar(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprSharp(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprRepeat(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSin(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSpace(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLpad(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprRpad(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDatabase(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprTable(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprInterval(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAll(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprBetween(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprSet(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprRowNumber(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprOver(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprPartition(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprUpdate(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprDelete(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprValues(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprInto(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprInsert(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAsc(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprDesc(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprHaving(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprLimit(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprGroup(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprBy(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprInner(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprOn(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprLike(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAcc(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprGeq(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprLeq(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprDivide(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprStar(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprSub(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprAdd(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprOper(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprEnd(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprElse(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprThen(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprRBrace(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprLBrace(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprIsNull(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprNullIf(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprIf(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprIfNull(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprConcat(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprGroupConcat(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprFindInSet(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCoalesce(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprConvert(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCast(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprChar(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprVarChar(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprText(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprBlob(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprUnixTimeStamp(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprFromUnixTime(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDateTime(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprTimeStamp(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprSigned(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprInteger(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprDecimal(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprYear(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprStrToDate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprWeekOfYear(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprWeek(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprTime(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSecond(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprQuarter(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprNow(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSysDate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprMonth(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprMinute(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLastDay(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprHour(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDayOfYear(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDayOfWeek(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDay(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDateFormat(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDateAdd(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDateSub(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDateDiff(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCurDate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprOrder(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCreate(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAlter(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprTruncate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprDrop(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprColumn(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprRename(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprTo(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprModify(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprTan(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSum(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSqrt(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSign(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprRound(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprRand(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprPow(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprPower(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprPi(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprMod(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprMin(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprMax(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLog2(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLog10(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLog(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLn(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprFloor(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprExp(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCount(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCot(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCos(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCeil(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCeiling(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprAtan(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprAsin(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprAcos(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprAvg(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprAbs(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprUpper(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprTrim(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSubStr(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprStrcmp(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprSeparator(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprRtrim(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprReverse(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprReplace(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLtrim(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLcase(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLower(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprInstr(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLocate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprConcatWs(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprCharLength(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLength(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprAscii(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprJoin(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprOuter(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprCross(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprRight(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprLeft(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }


    protected Statement exprNeq(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprGt(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprLt(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprLlm(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprRrm(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprIs(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprIn(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprNot(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprNull(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprExists(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprEq(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprOr(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAnd(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprBitAnd(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprBitOr(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprBitXor(ExprInfo exprInfo) throws AntlrException {
        return exprOper(exprInfo);
    }

    protected Statement exprWhere(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprWhen(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprCase(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprMyFunction(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprKeyWord(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprFunction(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    /**
     * 规约，如果本语法器可以解析单词，调用解析单词函数，
     * 否则如果本次可以规约，则进行规约，
     * 否则抛异常
     *
     * @param exprInfo 单词信息
     * @return 抽象树
     * @throws AntlrException
     */
    protected Statement exprNil(ExprInfo exprInfo) throws AntlrException {
        if (self) {
            return exprSelf(exprInfo);
        } else if (acceptSet.contains(ExprType.NIL)) {
            Statement statement = nil();
            return statement;
        } else {
            throw new AntlrException(ExprUtil.wrapper(exprReader));
        }
    }

    protected Statement exprSelf(ExprInfo exprInfo) throws AntlrException {
        throw new AntlrException(this.getClass().getSimpleName() + "未实现'" + exprInfo.getExprType() + "'");
    }


    protected Statement exprDistinct(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprFrom(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprSelect(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprAs(ExprInfo exprInfo) throws AntlrException {
        return exprKeyWord(exprInfo);
    }

    protected Statement exprComma(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprSymbol(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprLetter(ExprInfo exprInfo) throws AntlrException {
        return exprSymbol(exprInfo);
    }

    protected Statement exprNumber(ExprInfo exprInfo) throws AntlrException {
        return exprSymbol(exprInfo);
    }

    protected Statement exprStr(ExprInfo exprInfo) throws AntlrException {
        return exprSymbol(exprInfo);
    }

    protected Statement exprJavaStr(ExprInfo exprInfo) throws AntlrException {
        return exprSymbol(exprInfo);
    }

    protected Statement exprFloat(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprTinyInt(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprSmallInt(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprMediumInt(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprInt(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprBigInt(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprLong(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprDouble(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprDot(ExprInfo exprInfo) throws AntlrException {
        return exprNil(exprInfo);
    }

    protected Statement exprToChar(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprToNumber(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprToDate(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected Statement exprToTimeStamp(ExprInfo exprInfo) throws AntlrException {
        return exprFunction(exprInfo);
    }

    protected abstract Statement nil();

    protected boolean exprBefore(ExprType exprType) {
        return self = acceptSet.contains(exprType);
    }

    public SqlExpr setExprTypes(ExprType... exprTypes) {
        acceptSet.clear();
        acceptSet.addAll(Arrays.asList(exprTypes));
        return this;
    }

    protected SqlExpr addExprTypes(ExprType... exprTypes) {
        acceptSet.addAll(Arrays.asList(exprTypes));
        return this;
    }

    public Set<ExprType> getAcceptSet() {
        return acceptSet;
    }

    protected String name() {
        return this.getClass().getSimpleName();
    }
}
