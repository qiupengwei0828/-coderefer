package com.microservices.coderefer.po;

import java.util.Date;

import com.microservices.coderefer.annotation.Code;

import lombok.Data;
/**
 * 
* <p>Title: ReferCode</p>  
* <p>Description: 引用代码实现 </p>  
* @author qiupengwei  
* @date 2020年7月10日
 */
@Data
public class ReferCode {
	/**
	 * 编号
	 */
	private String bh;
	/**
	 * 类别
	 */
	private String lb;
	/**
	 * 代码
	 */
	private String dm;
	/**
	 * 名称
	 */
	@Code(type="name")
	private String mc;
	/***
	 * 显示顺序
	 */
	private Integer xssx;
	/**
	 * 有效标识
	 */
	private String  yxbs;
	/**
	 * 最后修改方式
	 */
	private String zhxgfs;
	/**
	 * 最后修改时间
	 */
	private Date zhxgsj;
	/**
	 * 创建时间
	 */
	private Date cjsj;


}
