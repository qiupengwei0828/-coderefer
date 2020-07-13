package com.microservices.coderefer.resolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.microservices.coderefer.annotation.Code;
import com.microservices.coderefer.common.Consts;
import com.microservices.coderefer.util.Result;

/**
 * 
* <p>Title: CodeResolver</p>  
* <p>Description: </p>  
* @author qiupengwei  
* @date 2020年7月10日
 */
@Aspect
@Configuration
public class CodeResolver {
	     final static Logger log = LoggerFactory.getLogger(CodeResolver.class);
	      private long beginTime=0;

	  @Pointcut("@annotation(com.microservices.coderefer.annotation.Code)")
	    public void serviceStatistics() {
	 
	    }
	 
	    @Before("serviceStatistics()")
	    public void doBefore(JoinPoint joinPoint) {
	        beginTime=System.currentTimeMillis();
	    }
	 
	    /**
	     * 自定义注解处理逻辑
	     * 1.通过aop方式拦截自定义注解方法
	     * 2.获取到方法返回值
	     * 3.判断方法返回值类型
	     * 4.对返回值类型进行判断，是否为list<pojo>和pojo
	     * 5.将list中pojo或者pojo的属性进行扫描，判断是否使用了自定义注解
	     * 6.如果属性使用了注解，根据注解属性值 type 进行相应的处理
	     * 7.将修改后的值通过反射的方式放入List<pojo>或者Pojo中，达到修改方法返回值的目的
	     * @param joinPoint
	     * @param value
	     */
	    @AfterReturning(value = "@annotation(com.microservices.coderefer.annotation.Code)",returning ="value")
	    public void doAfter(JoinPoint joinPoint,Object value) {
	        log.info("----数据模糊处理开始----");
	        value=this.dataHandle(value);
	        log.info("----数据模糊处理结束----");
	    }
	 
	    /**
	     * 数据处理
	     * 逻辑说明：查询结果只可在pojo中，目前常用返回值类型为pojo，list<pojo> Page,PageInfo Result等
	     * 最终实际上都是为pojo，此处后续可以扩展到其余类型
	     * 通过一层一层剥离的方式得到pojo进行处理
	     * 以Page为例，进入handListType方法后，handListType进行了遍历然后调用dataHandle方法，目的就是让最终dataHandle的入参是pojo
	     * 所有后续又新返回类型，可参考一下方式实现
	     * @param value
	     * @return
	     */
	    public Object dataHandle(Object value){
	        try {
	            if (value instanceof com.github.pagehelper.Page) {
	                return this.handListType((List<Object>) value);
	            }
	            else if(value instanceof ArrayList){
	                return this.handListType((List<Object>) value);
	            }
	            else if(value instanceof PageInfo){
	                PageInfo pageInfo= (PageInfo) value;
	                return this.dataHandle(pageInfo.getList());
	            }
	            else if(value instanceof List){
	                return this.handListType((List<Object>) value);
	            }
	            else if(value instanceof Result){
	                Result result= (Result) value;
	                return this.dataHandle(result.getData());
	            }
	            else if(value.getClass().getTypeName().indexOf("com.keyou")!=-1){
	                return this.handPojoType(value);
	            }
	            else{
	                return value;
	            }
	        }catch (Exception e){
	            return value;
	        }
	    }
	 
	    /**
	     * 通过反射模糊化处理pojo属性，如果属性是list，继续向下处理，递归逻辑
	     * @param object
	     * @return
	     */
	    public Object handPojoType(Object object){
	        if (object != null) {
	            Class clz =object.getClass();
	            // 获取到对象所有属性，并且遍历
	            Field[] fields = clz.getDeclaredFields();
	            for (Field field : fields) {
	                String classType=field.getType().getTypeName();
	                boolean fieldHasAnno = field.isAnnotationPresent(Code.class);
	                //判断属性上是否有注解，如果有进入逻辑，如果没有，返回对象
	                if (fieldHasAnno) {
	                    //如果属性是String 模糊化他（模糊化处理只能处理String了，不要问为什么）
	                    if(classType.equals("java.lang.String")){
	                    	Code code=field.getAnnotation(Code.class);
	                        String type=code.type();
	                        object=this.handleValue(field,object,type);
	                    }
	                    //这儿就相当于递归处理了，处理对象嵌套对象的方式的模糊化
	                    //如果不是，获取到他的值，继续走dataHandle（为什么又要走dataHandle？因为万一他又是List<POJO>这些呢？）
	                    else{
	                        Class fieldClass =object.getClass();
	                        String name=this.firstUpperCase(field.getName());
	                        field.setAccessible(true);// 设置操作权限为true
	                        Method getMethod= null;
	                        try {
	                            getMethod = fieldClass.getMethod("get"+name);
	                            Object value=getMethod.invoke(object);
	                            if(value!=null){
	                                value=this.dataHandle(value);
	                                Method setMethod=fieldClass.getMethod("set"+name,field.getType());
	                                setMethod.invoke(object,value);
	                            }
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        }
	        return object;
	    }
	    /**
	     * 处理List
	     * <p>Title: handListType</p>  
	     * <p>Description: </p>  
	     * @param page
	     * @return
	     */
	    public Object handListType(List<Object> page){
	        if (page != null && !page.isEmpty()) {
	           for(int i=0;i<page.size();i++){
	               page.set(i, this.dataHandle(page.get(i)));
	           }
	        }
	        return page;
	    }
	    /**
	     * 分页插件的使用
	     * <p>Title: handPageType</p>  
	     * <p>Description: </p>  
	     * @param page
	     * @return
	     */
	    public Object handPageType(Page page){
	        if (page != null && !page.isEmpty()) {
	            Object object = page.get(0);
	            Class clz =object.getClass();
	            // 判断类上是否有次注解
	            Field[] fields = clz.getDeclaredFields();
	            for (Field field : fields) {
	                boolean fieldHasAnno = field.isAnnotationPresent(Code.class);
	                if (fieldHasAnno) {
	                    Code code=field.getAnnotation(Code.class);
	                    String type=code.type();
	                    for(int i=0;i<page.size();i++){
	                        Object o=page.get(i);
	                        o=this.handleValue(field,o,type);
	                        page.set(i,o);
	                    }
	                }
	            }
	        }
	        return page;
	    }
	    
	    /**
	     * 处理值
	     * <p>Title: handleValue</p>  
	     * <p>Description: </p>  
	     * @param field
	     * @param object
	     * @param type
	     * @return
	     */
	    public Object handleValue(Field field,Object object,String type){
	        try {
	            Class clz =object.getClass();
	            String name=this.firstUpperCase(field.getName());
	            field.setAccessible(true);// 设置操作权限为true
	            Method getMethod=clz.getMethod("get"+name);
	            Object value=getMethod.invoke(object);
	            Method setMethod=clz.getMethod("set"+name,field.getType());
	            value=this.handleValue(value,type);
	            setMethod.invoke(object,value);
	            return object;
	        }catch (Exception e){
	            e.printStackTrace();
	            return object;
	        }
	    }
	    
	    /**
	     * 分类处理不同类型
	     * <p>Title: handleValue</p>  
	     * <p>Description: </p>  
	     * @param object
	     * @param type
	     * @return
	     */
	    public Object handleValue(Object object,String type){
	        switch (type){
	            case Consts.NAME:
	                return handleNAME(object);
	            case Consts.ID_CODE:
	                return handleIDCODE(object);
	            case Consts.BANK_CODE:
	                return handleBANK_CODE(object);
	            case Consts.ADDRESS:
	                return handleNAME(object);
	            case Consts.PHONE_NO:
	                return handlePHONE_NO(object);
	        }
	        return object;
	    }
	    
	    /**
	     * 处理手机号
	     * <p>Title: handlePHONE_NO</p>  
	     * <p>Description: </p>  
	     * @param object
	     * @return
	     */
	    public Object handlePHONE_NO(Object object) {
	        if(object!=null){
	            String phone_no=object.toString();
	            if(phone_no.length()==11){
	                String phoneNumber = phone_no.substring(0, 3) + "****" + phone_no.substring(7, phone_no.length());
	                return phoneNumber;
	            }
	        }
	        return object;
	    }
	    
	    /**
	     * 银行卡替换，保留后四位
	     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
	     * @param object 银行卡号
	     * @return
	     */
	    public Object handleBANK_CODE(Object object) {
	        if(object!=null){
	            String bankCard=object.toString();
	            if (bankCard.isEmpty() || bankCard == null) {
	                return null;
	            } else {
	                return bankCard.substring(0,4)+"**********"+bankCard.substring(bankCard.length()-4,bankCard.length());
	            }
	        }
	        return object;
	    }
	    /**
	     * 身份证号替换，保留前四位和后四位
	     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
	     * @param object 身份证号
	     * @return
	     */
	    public Object handleIDCODE(Object object) {
	        if(object!=null){
	            String idCode=object.toString();
	            if (idCode.isEmpty() || idCode == null) {
	                return object;
	            } else {
	                return idCode.substring(0,4)+"**********"+idCode.substring(idCode.length()-4,idCode.length());
	            }
	        }
	        return object;
	    }
	 
	  /**
	   *   
	   * <p>Title: 处理名称</p>  
	   * <p>Description: </p>  
	   * @param object
	   * @return
	   */
	  public Object handleNAME(Object object){
	        if(object!=null){
	            String userName=object.toString();
	            int length=object.toString().length();
	            if (length <= 1) {
	                return "*";
	            } else if (length == 2) {
	                return userName.substring(0, 1) + "*";
	            }
	            else if (length == 3) {
	                return userName.substring(0, 1) + "**";
	            }
	            else{
	                StringBuffer sb=new StringBuffer();
	                sb.append(userName.substring(0, 2));
	                for(int i=0;i<length-2;i++){
	                    sb.append("*");
	                }
	                return sb.toString();
	            }
	        }
	        return object;
	    }
	    public String firstUpperCase(String str){
	        return StringUtils.replaceChars(str, str.substring(0, 1),str.substring(0, 1).toUpperCase());
	    }
}
