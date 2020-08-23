package com.microservices.coderefer.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 *  前提是 调整JVM的 参数 -Xmn最小内存  -Xmx最大内存 -Xms 初始化内存
 *  设置JVM大小，线程达到最大阻塞队列时，就会出现 OOM
 *  Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
	at com.microservices.coderefer.test.test.main(test.java:10)
* <p>Title: test</p>  
* <p>Description: </p>  
* @author qiupengwei  
* @date 2020年8月16日
 */
public class test {
	private  static   ExecutorService executorService=Executors.newFixedThreadPool(15);
	public static void main(String[] args) {
		for(int i=0;i<Integer.MAX_VALUE;i++) {
			executorService.execute(new SubThread());
		}
		
	}
}
