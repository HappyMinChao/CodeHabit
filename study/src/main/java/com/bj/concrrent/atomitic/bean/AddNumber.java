package com.bj.concrrent.atomitic.bean;

import com.bj.concrrent.atomitic.test.AtomicIntegerTest;

/*
 * 作用 对atomicInteger 进行减操作
 */
public class AddNumber extends Thread{
	
    @Override
    public void run() {
        for (int i = 0 ; i< 100; i++){
        	AtomicIntegerTest.number.getAndAdd(1);
            System.out.println(AtomicIntegerTest.number);
            try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }
}

