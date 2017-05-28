package com.bj.concrrent.atomitic.bean;

import com.bj.concrrent.atomitic.test.AtomicIntegerTest;

/*
 * 作用：为把number变量减一
 */
public class MinNumber extends Thread{
	
    @Override
    public void run() {
        for (int i = 0 ; i< 1000; i++){
        	AtomicIntegerTest.number.getAndDecrement();
//        	VolatileTest.number--;   当为普通数字是进行 -- 操作
            System.out.println(AtomicIntegerTest.number);
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    }
	
}
