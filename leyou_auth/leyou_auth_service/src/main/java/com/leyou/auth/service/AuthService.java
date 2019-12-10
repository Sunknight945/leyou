package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entiy.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ovo
 */
@Service
public class AuthService {
  
  @Autowired
  JwtProperties jwtProperties;
  @Autowired
  UserClient userClient;
  
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
  
  
  public String accredit(String username, String password) {
    try {
      User user = this.userClient.queryUser(username, password);
      if (user == null) {
        LOGGER.info("用户信息不存在,{}", username);
        return null;
      }
      return JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
}