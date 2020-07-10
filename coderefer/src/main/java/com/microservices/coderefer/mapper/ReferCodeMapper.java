package com.microservices.coderefer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.microservices.coderefer.mapper.provider.ReferCodeSqlProvider;
import com.microservices.coderefer.po.ReferCode;
/**
* <p>Title: ReferCodeMapper</p>  
* <p>Description: </p>  
* @author qiupengwei  
* @date 2020年7月10日
 */
@Mapper
public interface ReferCodeMapper   {
	
	
	/**
	 * <p>Title: listReferCode</p>  
	 * <p>Description: </p>  
	 * @return
	 */
    @SelectProvider(type = ReferCodeSqlProvider.class, method = "listReferCode")
    @Results({
            @Result(property = "bh", column = "c_bh"),
            @Result(property = "lb", column = "c_lb"),
            @Result(property = "dm", column = "c_dm"),
            @Result(property = "mc", column = "c_mc"),
    })
    List<ReferCode> listReferCode();

}
