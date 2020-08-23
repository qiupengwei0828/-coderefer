package com.microservices.coderefer.test.list;

import java.util.ArrayList;

import com.sun.tools.javac.util.List;
/***
	*   subList ArrayList的内部类，是一个视图  ，接的时候不能用ArrayList来接收
	*   <p>Title: TestArrayListSubList</p>  
	*   <p>Description: </p>  
	*    @author qiupengwei  
	*    @date 2020年8月16日
 */
public class TestArrayListSubList {
  public static void main(String[] args) {
	  ArrayList<String> arryList=new ArrayList<String>() {{
       add("hello");
       add("world");
       add("china");
	}};
	List  subList=(List) arryList.subList(0, 1);
	System.out.println(subList);
  }
}
