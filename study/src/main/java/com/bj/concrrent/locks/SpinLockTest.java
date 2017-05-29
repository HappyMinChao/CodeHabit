package com.bj.concrrent.locks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class SpinLockTest{
	
	private SpinLock lock = new SpinLock();
	
	@Test
	public void test(){
		for(int i = 0 ; i < 100; i++){
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					for(int j = 0 ; j < 6 ; j ++){
						lock.getPeople("san_+mao_" + j % 2);
					}
				}
			});
			thread.setName("Thread-"+i);
			thread.start();
			try {thread.join();} catch (InterruptedException e) {}
		}
		System.out.println(SpinLock.mapPeople.size());
	}
	
}

/**
 * 自旋锁测试
 * 
 * 知识补充：   V  putIfAbsent(String key, V value)
 * If the specified key is not already associated with a value, associate it with the given value. This is equivalent to
   if (!map.containsKey(key))       	//如果不包含key，则put， 并返回 oldValue ，第一次为null
       return map.put(key, value);
   else
       return map.get(key);  			//如果包含则返回get(key)的值， 说明返回的还是第一次设置的值
except that the action is performed atomically.
 */
class SpinLock {
	
	public SpinLock() {}

	//用来存储people的容器
	public static Map<String, People> mapPeople = new ConcurrentHashMap<String,People>();
	//用来辅助标识每一个key对应的自旋锁的状态
	private ConcurrentHashMap<String, SpinStatus> mapSpin = new ConcurrentHashMap<String, SpinStatus>();
	
	//无锁实现单实例People的创建, 这里假设name不重名，能做为主键
	public People getPeople(String name){
		People people = mapPeople.get(name);
		
		// 当people为null 说明mapPeople中还没有对象
		if(people == null){
			// 通过key(name),获取自旋锁状态
			SpinStatus spin = mapSpin.get(name);
			try {Thread.sleep(600);} catch (InterruptedException e1) {}
			if(spin == null){
				//自旋锁为空， 说明此mapSpin中没有对应的自旋锁
				SpinStatus spinStatus = new SpinStatus();
				System.out.println(Thread.currentThread().getName() + "将要创建name: " + name + "的people。" );
				//像mapSpin中添加该key的自旋锁,并返回与键第一次相关联的值，如果没有键映射，则为null。
				//当第一次设置key为该name变量的值时，返回为null
				//其他线程拿到的oldSpinStatus是第一次设置的SpinStatus，可以在所有线程中共享  
				SpinStatus oldSpinStatus = mapSpin.putIfAbsent(name, spinStatus);
				try {
					//只会有一个设置相同name的线程能走到这
					if(oldSpinStatus == null){ // 为空， 说明为第一次设值， 需创建people实例
						System.out.println(Thread.currentThread().getName() + "创建name: " + name + "的people。" );
						People p = new People(name,(int)(Math.random() * 1000 % 99), 'Y');
						mapPeople.put(name, p);
						
						spinStatus.status.set(true);;
					}else{ //如果oldSpinStatus不为空，说明已经设置自旋锁，People不一定被创建， 当spinStatus.status为true时， 说明创建People完成
						while(! spinStatus.status.get()) {
							System.out.println(Thread.currentThread().getName() + " 死循环等待  name " + name + "的实例创建完成");
						} // 死循环直到自旋锁状态设置为true
					}
				} catch (Exception e) {
					System.out.println("当抛出异常时，需要把对用的key删除掉！ 以保证数据一致性");
					e.printStackTrace();
					mapSpin.remove(name);
				}
			}
            // 再次获取一下，这时候是肯定不为空  
			people = mapPeople.get(name);
		}else{
			System.out.println(Thread.currentThread().getName() + " key为： " + name + "自旋锁已创建！");
		}
		
		return people;
	}
	
}

//记录自旋锁的状态
class SpinStatus {
	//当status = true 为锁定, 线程安全的
	volatile AtomicBoolean status = new AtomicBoolean(false);
}
//定义bean
class People {

	private String name;
	private int age;
	private char sex;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public People() {
	}

	public People(String name, int age, char sex) {
		super();
		this.name = name;
		this.age = age;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "People [name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}

}