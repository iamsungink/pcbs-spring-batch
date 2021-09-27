package com.ktds.batch.pcbs.demo.config;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SpCallRowMapper implements RowMapper {
    @Override
    public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> map = new HashMap<>();

//        map.put("I_INV_YYYYMM", rs.getString(1));
        map.put("OUT_RETURN_STATUS", rs.getString(1));
        map.put("OUT_ERR_MSG", rs.getString(2));

        return map;
    }
}
