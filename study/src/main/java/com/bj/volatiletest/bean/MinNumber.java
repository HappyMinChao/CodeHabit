package com.bj.volatiletest.bean;

import com.bj.volatiletest.test.VolatileTest;

/*
 * 作用：为把 volatile number变量减一
 */

public class MinNumber extends Thread{
	
    @Override
    public void run() {
        for (int i = 0 ; i< 1000; i++){
        	VolatileTest.number--;
            System.out.println(VolatileTest.number);
        }
        
    }
	
}
