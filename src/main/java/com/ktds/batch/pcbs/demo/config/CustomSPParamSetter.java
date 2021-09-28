package com.ktds.batch.pcbs.demo.config;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class CustomSPParamSetter implements PreparedStatementSetter {
    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        CallableStatement callableStatement = (CallableStatement) ps;
        callableStatement.setString(1,"202109");
//        callableStatement.registerOutParameter(1, Types.VARCHAR);
//        callableStatement.registerOutParameter(2, Types.VARCHAR);
    }
}
