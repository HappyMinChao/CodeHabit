package com.bj.concrrent.atomitic.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.bj.concrrent.atomitic.bean.AddNumber;

public class AtomicIntegerTest {
    public static AtomicInteger number = new AtomicInteger();

    private ConcurrentHashMap<String, Thread> con = new ConcurrentHashMap<>();
    
    /*
     * 测试结果为：   最终打印的数字为： 10000 说明在加操作中线程间没有被覆盖。 线程安全的。
     */
    @Test
    public void testAddJoinThread(){
    	
    	//使用volatile修改的成员变量无法保证线程的安全性， 最终输出的数字不是应该输出的
    	//使用 AtomicInteger可以保证线程安全性， 其使用的是cas （compare， and  switch）
    	
    	for(int i = 0 ; i < 100 ; i ++){
    		Thread  addThread = new AddNumber();
    		addThread.setName("addThread"+i);
    		con.put("addThread"+i, addThread);
    		addThread.start();
    	}
    	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    	System.out.println(Thread.currentThread().getName() + "-----"+number.intValue());
    }

}
