package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author ovo
 */
@Service
public class UserService {
  
  @Autowired
  UserMapper userMapper;
  @Autowired
  AmqpTemplate amqpTemplate;
  @Autowired
  StringRedisTemplate redisTemplate;
  
  private static final String key_prefix = "user:verify:";
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  
  /**
   * @param data
   * @param type
   * @return
   */
  public Boolean checkUserData(String data, Integer type) {
    User record = new User();
    switch (type) {
      case 1:
        record.setUsername(data);
        break;
      case 2:
        record.setPhone(data);
        break;
      default:
        return null;
    }
    return this.userMapper.selectCount(record) == 0;
  }
  
  /**
   * @param phone
   * @return
   */
  public Boolean sendVerifyCode(String phone) {
    if (StringUtils.isBlank(phone)) {
      return null;
    }
    try {
      //生成验证码
      String code = NumberUtils.generateCode(6);
      //发送消息到rabbitMQ
      HashMap<String, String> map = new HashMap<>();
      map.put("phone", phone);
      map.put("code", code);
      amqpTemplate.convertAndSend("leyou.sms.exchange", "sms.verify.code", map);
      //把验证码保存到redis中
      redisTemplate.opsForValue().set(key_prefix + phone, code, 20, TimeUnit.MINUTES);
      return true;
    } catch (AmqpException e) {
      e.printStackTrace();
      LOGGER.error("短信发送失败", e.getMessage());
      return false;
    }
  }
  
  /**
   * 用户注册 里面有用户填写的信息与手机上的验证码
   *
   * @param user
   * @param code
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public void userRegister(User user, String code) {
    //查询redis中的验证码
    if (user == null) {
      return;
    }
    //1.校验验证码
    String codeFromRedis = this.redisTemplate.opsForValue().get(key_prefix + user.getPhone());
    if (StringUtils.equals(codeFromRedis, code)) {
      //2.生成盐
      String salt = CodecUtils.generateSalt();
      user.setSalt(salt);
      //3.加盐加密
      String passwordWithSalt = CodecUtils.md5Hex(user.getPassword(), salt);
      user.setPassword(passwordWithSalt);
      user.setId(null);
      user.setCreated(new Date());
      //4.新增用户
      int i = this.userMapper.insertSelective(user);
      this.redisTemplate.delete(key_prefix + user.getPhone());
    }
  }
  
  
  /**
   * 查询功能，根据参数中的用户名和密码查询指定用户
   * @param username
   * @param password
   * @return
   */
  public User queryUser(String username, String password) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return null;
    }
    User record = new User();
    record.setUsername(username);
    User user = this.userMapper.selectOne(record);
    if (user == null) {
      return null;
    }
    if (StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password, user.getSalt()))) {
      return user;
    } else {
      return null;
    }
  }
}
 

