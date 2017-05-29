package com.bj.concrrent.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

public class LocksTest {
	
	ReadWriteLock lock = new ReentrantReadWriteLock();
	public static int num = 0; 
	
	private Cache cache = new Cache();
	
	@Test
	public void testLocks(){
		//创建1000个线程同时对Cache做读写操作， 其中的key不同， 所以， 不会有读版本不一致的问题。
		for(int i = 0; i< 1000; i++){
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					int num =  (int) (Math.random() * 1000 % 10);
					cache.putIfNotExit("key"+num, "value"+num);
				}
			});
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(Cache.map);
	}
	
}

/**
 * 描述：使用HashMap实现缓存， 线程间可以同时读取， 不能同时写， 读和写相互隔离。类似于乐观锁
 * 
 */
class Cache {
	//新建一个HashMap，线程间可见， 但不安全
    public volatile static Map<String, Object> map = new HashMap<String, Object>(16);
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public Object get(String key){
    	lock.readLock().lock();
    	
    	Object result = null;
		try {
			result = map.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.readLock().unlock();
		}
    	
    	return result;
    }
    
    public Object put(String key, Object value){
    	lock.writeLock().lock();
    	try {
			map.put(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.writeLock().unlock();
		}
    	return value;
    }

    //get功能描述： 如果map中有值则返回， 如果没有值设置为“default”返回
    public Object putIfNotExit(String key,Object value){
        lock.readLock().lock();//首先开启读锁，从缓存中去取
        Object desValue = null;
        try{
        	desValue = map.get(key);		//如果不为空可以不用进入writeLock， 提高性能
        	System.out.println(Thread.currentThread().getName() + " : 读取key : " + key +" ,value : " + desValue);
            if(desValue == null){  			//如果缓存中没有对应key的值，释放读锁，上写锁
				Thread.sleep(100); 			//模拟执行其他操作用时
                lock.readLock().unlock();   // 有可能多个线程进入到这， 要修改map， 所以可能会产生线程1put， 紧接着线程2put
                lock.writeLock().lock();	// 如果想保证读写的原子性， 不能使用读锁
            	System.out.println(Thread.currentThread().getName() + "进入写锁 " + key +" ,value : " + desValue);
                try{
                	if(map.get(key) != null) return desValue;   //在写之前重新读一次， 保证一个key只被写一次，不发生线程间覆盖， 类似事物的可重复读。 
                    if(value != null){
                    	System.out.println(Thread.currentThread().getName() + "真正写 " + key +" ,value : " + value);
                        map.put(key, value);    // 能保证线程间写互斥， 但不能保证线程覆盖
                        desValue = value;
                    }
                }finally{
                    lock.writeLock().unlock(); //释放写锁
                }
                lock.readLock().lock(); //然后再上读锁
            }
        }catch(Exception e){} finally{
            lock.readLock().unlock(); //最后释放读锁
        }
        return desValue;
    }

}