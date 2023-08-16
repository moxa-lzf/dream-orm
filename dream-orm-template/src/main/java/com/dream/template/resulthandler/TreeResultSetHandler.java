package com.dream.template.resulthandler;

import com.dream.system.config.MappedStatement;
import com.dream.system.core.resultsethandler.ResultSetHandler;
import com.dream.system.core.session.Session;
import com.dream.util.exception.DreamRunTimeException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class TreeResultSetHandler implements ResultSetHandler {
    private ResultSetHandler resultSetHandler;

    public TreeResultSetHandler(ResultSetHandler resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    @Override
    public Object result(ResultSet resultSet, MappedStatement mappedStatement, Session session) throws SQLException {
        Object result = resultSetHandler.result(resultSet, mappedStatement, session);
        if (Tree.class.isAssignableFrom(mappedStatement.getColType())) {
            return TreeUtil.toTree((Collection<? extends Tree>) result);
        } else {
            throw new DreamRunTimeException("树查询，请继承" + Tree.class.getName());
        }
    }
}