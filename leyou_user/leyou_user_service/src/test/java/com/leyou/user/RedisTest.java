package com.leyou.user;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
  
  @Autowired
  StringRedisTemplate redisTemplate;
  
  @Test
  public void testRedis() {
    // 存储数据
    this.redisTemplate.opsForValue().set("key1", "value1");
    // 获取数据
    String val = this.redisTemplate.opsForValue().get("key1");
    System.out.println("val = " + val);
  }
  
  
  /*@Test
  public void testRedis2() {
    // 存储数据，并指定剩余生命时间,5小时
    this.redisTemplate.opsForValue().set("key2", "value2",
        5, TimeUnit.HOURS);
  }*/
  @Test
  public void testRedis2() {
    //存储数据, 并指定剩余声明时间. 5小时
    this.redisTemplate.opsForValue().set("key2", "value332", 5, TimeUnit.HOURS);
  }
  
  
  /*@Test
  public void testHash() {
    BoundHashOperations<String, Object, Object> hashOps =
        this.redisTemplate.boundHashOps("user");
    // 操作hash数据
    hashOps.put("name", "jack");
    hashOps.put("age", "21");
    
    // 获取单个数据
    Object name = hashOps.get("name");
    System.out.println("name = " + name);
    
    // 获取所有数据
    Map<Object, Object> map = hashOps.entries();
    for (Map.Entry<Object, Object> me : map.entrySet()) {
      System.out.println(me.getKey() + " : " + me.getValue());
    }
  }*/
  
  @Test
  public void testRedis3() {
    BoundHashOperations<String, Object, Object> user = this.redisTemplate.boundHashOps("user");
    //错作hash数据
    user.put("name", "jack1");
    user.put("age", "22");
    //获取所有数据
    Map<Object, Object> map = user.entries();
    for (Map.Entry<Object, Object> objectEntry : map.entrySet()) {
      System.out.println(objectEntry.getKey() + ":" + objectEntry.getValue() );
    }
  }
  
  
}