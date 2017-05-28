package com.bj.volatiletest.bean;

import com.bj.concrrent.atomitic.test.AtomicIntegerTest;
import com.bj.volatiletest.test.VolatileTest;

/*
 * 作用 对 volatile int 进行加操作
 */
public class AddNumber extends Thread{
    @Override
    public void run() {
        for (int i = 0 ; i< 100; i++){
        	VolatileTest.number++;
            System.out.println(Thread.currentThread().getName() + "---" +VolatileTest.number);
        }

    }
}

