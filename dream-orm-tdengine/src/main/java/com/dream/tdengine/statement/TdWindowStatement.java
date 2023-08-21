package com.dream.tdengine.statement;

import com.dream.antlr.config.Assist;
import com.dream.antlr.exception.AntlrException;
import com.dream.antlr.invoker.Invoker;
import com.dream.antlr.smt.MyFunctionStatement;
import com.dream.antlr.sql.ToSQL;

import java.util.List;

public abstract class TdWindowStatement extends MyFunctionStatement {
    public static class TdSessionWindowStatement extends TdWindowStatement {

        @Override
        public String toString(ToSQL toSQL, Assist assist, List<Invoker> invokerList) throws AntlrException {
            return null;
        }
    }

    public static class TdStateWindowStatement extends TdWindowStatement {

        @Override
        public String toString(ToSQL toSQL, Assist assist, List<Invoker> invokerList) throws AntlrException {
            return null;
        }
    }

    public static class TdIntervalWindowStatement extends TdWindowStatement {
        private TdIntervalStatement interval;
        private TdSlidingStatement sliding;
        private TdFillStatement fill;

        public TdIntervalStatement getInterval() {
            return interval;
        }

        public void setInterval(TdIntervalStatement interval) {
            this.interval = interval;
        }

        public TdSlidingStatement getSliding() {
            return sliding;
        }

        public void setSliding(TdSlidingStatement sliding) {
            this.sliding = sliding;
        }

        public TdFillStatement getFill() {
            return fill;
        }

        public void setFill(TdFillStatement fill) {
            this.fill = fill;
        }

        @Override
        public String toString(ToSQL toSQL, Assist assist, List<Invoker> invokerList) throws AntlrException {
            return null;
        }
    }
}