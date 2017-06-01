package com.bj.goodhabit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CoderGoodHabit {
	
	List<String> list = new ArrayList<String>();

	@Before
	public void before(){
		list.add("111");
		list.add("222");
		list.add("333");
		list.add("444");
		list.add("555");
	}

	/**
	 * for 循环定义一个len来存放数组大小，避免每次获取
	 */
	@Test
	public void testFor(){
		for(int i = 0 , len = list.size(); i<len; i++){
			System.out.println(list.get(i));
		}
	}
	
	/*
	 * 返回集合时不要返回null，返货空集合
	 * 	可以使用 isEmpty 来判断是否为空集合
	 */
	@Test
	public void testEmptyList(){
		List listEmpty ;
		if(CollectionUtils.isEmpty(list))		; 
//			listEmpty = null; 
		listEmpty = Collections.EMPTY_LIST;
//		Collections.EMPTY_MAP;
//		Collections.EMPTY_SET;
		System.out.println(listEmpty);
	}
	
	
	/*
	 * 不要在for循环中操作数据库
	 */
	@Test
	public void testDB(){
		//根据业务场景，把for循环中的多次连接数据库查询，写到sql中去查询，即一次性出来
		//根据业务场景，看是否可以利用缓存，提高查询效率		
	}
	
	@Test
	public void testException(){
		//切勿把异常放置在循环体内
		//尽量缩小锁的范围
		//减少锁持有时间、 减少锁粒度 (使用变量锁)
		//锁分离 ReadWriteLock、 锁粗化、 锁消除
		//对象锁，不好，效率低
	    synchronized(this){	//对象锁
	        list.add("yyy");
	    	System.out.println("xxx");;
	    }

	    synchronized(list){	//变量锁，减小锁力度，性能更优
	    	list.add("xxx");
	        System.out.println(list);;
	    }
	}
	
	/*
	 * 1. sql 语句大小写规范，全部大写或者全部小写
	 * 2. 执行计划是数据库调优的利器
	 * 3. 建立索引，避免全表扫描， 在where/order by 使用的列上建索引
	 * 4. 注意索引失效的情况
	 * 5. 一个表上的索引最好不要超过7个
	 * 
	 * 查询效率的几点建议： 
	 * 	
		1. 尽量使用数字型字段，若只含数值信息的字段尽量不要设计为字符型，这会降低查询和连接的性能，并会增加存储开销。
		2. 尽可能的使用 varchar/nvarchar 代替 char/nchar ，因为变长字段存储空间小，对于查询来说，在一个相对较小的字段内搜索效率显然要高些。
		3. 最好不要给数据库留NULL，尽可能的使用 NOT NULL填充数据库。备注、描述、评论之类的可以设置为 NULL。其他的，最好不要使用NULL。
		4. 任何地方都不要使用 select * from t ，用具体的字段列表代替 * ，不要返回用不到的任何字段。
		5. 应尽量避免在 where 子句中使用 or 来连接条件,可以考虑使用 union 代替
		6. in 和 not in 也要慎用。对于连续的数值，能用 between 就不要用 in，exists 代替 in
		7. 尽量避免在 where 子句中对字段进行表达式操作和函数操作
	 */
	
	
	
	@Test
	public void testBitMap(){
	
	}
}


class CollectionUtils{
	public static boolean isEmpty(List list){
		return list == null || list.size() <= 0;
	}
}