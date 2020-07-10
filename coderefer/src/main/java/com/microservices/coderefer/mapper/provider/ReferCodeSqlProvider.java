package com.microservices.coderefer.mapper.provider;

import org.apache.ibatis.jdbc.SQL;


public class ReferCodeSqlProvider {

    private static final String TABLE_NAME = "T_REFER_CODE";

    /**
     * 查询代码表
     *  @Result(property = "bh", column = "c_bh"),
            @Result(property = "lb", column = "c_lb"),
            @Result(property = "dm", column = "c_dm"),
            @Result(property = "mc", column = "c_mc"),
     * @return sql
     */
    public String listReferCode() {
        SQL sql = new SQL();
        sql.SELECT("c_bh", "c_lb", "c_dm", "c_mc");
        sql.FROM(TABLE_NAME);
        return sql.toString();
    }

}
