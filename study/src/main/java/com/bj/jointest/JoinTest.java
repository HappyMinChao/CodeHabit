package com.bj.jointest;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.bj.volatiletest.bean.AddNumber;
import com.bj.volatiletest.test.VolatileTest;

public class JoinTest {
	
    public static int number = 0 ; 
    private ConcurrentHashMap<String, Thread> con = new ConcurrentHashMap<>();
    
    /* 其中结果会出现： 
     * 	9400
	 * 	9866
	 *  说明不是线程顺序执行, 通过打印线程名， 发现线程间是交叉执行的。 
	 *  运行多次后出现了的结果： 9998
	 *  但是为什么会线程安全呢？  结果为： 10000
	 *  
	 *  9997
		9998
		-0----9998
	 *  总结：  使用了join 主线程能等待其他线程执行完毕后继续执行， 
	 *  	但是不是线程安全的。
     */
    
    @Test
    public void testAddJoinThread(){
    	
    	for(int i = 0 ; i < 100 ; i ++){
    		AddNumber addThread = new AddNumber();
    		addThread.setName("addThread"+i);
    		con.put("addThread"+i, addThread);
    		addThread.start();
    	}
    	
    	for(int i = 0 ; i < 100; i++){
    		
    		Thread thread = con.get("addThread"+i);
    		try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	System.out.println("-0----"+VolatileTest.number);
    }
	
}
