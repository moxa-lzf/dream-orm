package com.moxa.dream.module.mapped;

import com.moxa.dream.module.producer.PropertyInfo;
import com.moxa.dream.module.producer.factory.ObjectFactory;
import com.moxa.dream.module.type.handler.TypeHandler;
import com.moxa.dream.util.wrapper.ObjectWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MappedColumn {
    private int index;
    private int jdbcType;
    private String table;
    private boolean primary;
    private TypeHandler typeHandler;
    private PropertyInfo propertyInfo;

    public MappedColumn(int index, int jdbcType, String table,PropertyInfo propertyInfo, boolean primary) {
        this.index = index;
        this.jdbcType = jdbcType;
        this.table = table;
        this.propertyInfo=propertyInfo;
        this.primary = primary;
    }

    public Object getValue(ResultSet resultSet) throws SQLException {
        return typeHandler.getResult(resultSet, index, jdbcType);
    }

    public void linkObject(ResultSet resultSet, ObjectFactory objectFactory) throws SQLException {
        objectFactory.set(propertyInfo, getValue(resultSet));
    }

    public String getTable() {
        return table;
    }

    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }

    public boolean isPrimary() {
        return primary;
    }
}
