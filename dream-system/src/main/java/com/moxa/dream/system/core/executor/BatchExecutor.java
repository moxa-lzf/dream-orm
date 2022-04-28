package com.moxa.dream.system.core.executor;

import com.moxa.dream.system.config.Configuration;
import com.moxa.dream.system.core.statementhandler.BatchStatementHandler;
import com.moxa.dream.system.core.statementhandler.PrepareStatementHandler;
import com.moxa.dream.system.core.statementhandler.StatementHandler;

public class BatchExecutor extends AbstractExecutor {

    public BatchExecutor(Configuration configuration, boolean autoCommit) {
        super(configuration, autoCommit);
    }

    @Override
    protected StatementHandler createStatementHandler() {
        return new BatchStatementHandler(new PrepareStatementHandler());
    }

    @Override
    public void commit() {
        try {
            flushStatement(false);
        } finally {
            super.commit();
        }
    }

    @Override
    public void rollback() {
        try {
            flushStatement(true);
        } finally {
            super.rollback();
        }
    }

    @Override
    public void close() {
        try {
            flushStatement(false);
        } finally {
            super.close();
        }
    }

    public void flushStatement(boolean rollback) {
        statementHandler.flushStatement(rollback);
    }
}