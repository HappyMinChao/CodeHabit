package com.bj.volatiletest.test;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.bj.volatiletest.bean.AddNumber;


public class VolatileTest {

	public volatile static int number = 0;
	private ConcurrentHashMap<String, Thread> con = new ConcurrentHashMap<>();
	
	/*
	 * 测试结果为： 最终打印的数字为： 9997  说明在加操作中线程间有被覆盖。 线程不安全。
	 * 結果為： 线程不安全
	 * 
	 * 总结：  volatile能保证线程之间的可见性， 
	 * 		因为线程有工作空间， 主内存之分， 每一个成员变量在操作时拷贝一份到线程工作空间进行运算，
	 * 	运算完结果写入主内存中是会保证其他线程再次使用时的可见性， 但是如果在 线程计算到写内存过程中
	 *  不是原子操作， 就有可能会导致线程间的写覆盖问题，无法保证线程的安全性。 
	 *  
	 *  不要将volatile用在getAndOperate场合（这种场合不原子，需要再加锁），仅仅set或者get的场景是适合volatile的。
	 */
	@Test
	public void testAddJoinThread() {
		// 使用volatile修改的成员变量无法保证线程的安全性， 最终输出的数字不是应该输出的
		// 使用 AtomicInteger可以保证线程安全性， 其使用的是cas （compare， and switch）
		for (int i = 0; i < 100; i++) {
			Thread addThread = new AddNumber();
			addThread.setName("addThread" + i);
			con.put("addThread" + i, addThread);
			addThread.start();
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "-----" + number);
	}

}
