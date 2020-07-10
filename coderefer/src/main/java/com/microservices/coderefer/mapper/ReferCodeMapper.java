package com.microservices.coderefer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.microservices.coderefer.mapper.provider.ReferCodeSqlProvider;
import com.microservices.coderefer.po.ReferCode;

@Mapper
public interface ReferCodeMapper   {
    @SelectProvider(type = ReferCodeSqlProvider.class, method = "listReferCode")
    @Results({
            @Result(property = "bh", column = "c_bh"),
            @Result(property = "lb", column = "c_lb"),
            @Result(property = "dm", column = "c_dm"),
            @Result(property = "mc", column = "c_mc"),
    })
    List<ReferCode> listReferCode();

}
