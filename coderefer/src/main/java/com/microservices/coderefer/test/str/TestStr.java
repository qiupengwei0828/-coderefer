package com.microservices.coderefer.test.str;

import org.apache.commons.lang3.StringUtils;

public class TestStr {
	public static void main(String[] args) {
		 String  a="abc";
		 String  b="def";
	     a.concat("c").concat(b);
	     StringUtils.join("c",b,a);
	     String arrs[]= {"语文","数学","化学"};
	     //元素之间的拼接用，进行,常用场景，字符串规格拼接，或者，数据库查询时 in，用
	     StringUtils.join(arrs,",");
	     System.out.println( StringUtils.join(arrs,","));
	}

}
