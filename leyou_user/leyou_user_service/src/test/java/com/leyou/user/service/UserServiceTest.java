package com.leyou.user.service;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
  @Autowired
  UserMapper userMapper;
  @Test
  public void chechUserData() {
    User record = new User();
    record.setUsername("coco");
    List<User> select = this.userMapper.select(record);
    System.out.println(select);
  }
}