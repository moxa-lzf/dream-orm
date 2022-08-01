package com.moxa.dream.system.typehandler.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DoubleTypeHandler extends BaseTypeHandler<Double> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Double parameter, int jdbcType) throws SQLException {
        ps.setDouble(i, parameter);
    }

    @Override
    public Double getResult(ResultSet rs, int i, int jdbcType) throws SQLException {
        double result = rs.getDouble(i);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public int getNullType() {
        return Types.DOUBLE;
    }
}
