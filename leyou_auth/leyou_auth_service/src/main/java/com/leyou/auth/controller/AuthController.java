package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entiy.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ovo
 */
@RestController
public class AuthController {
  
  @Autowired
  AuthService authService;
  @Autowired
  JwtProperties jwtProperties;
  
  
  @GetMapping("accredit")
  public ResponseEntity<Void> accredit(
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      //httpstatus 里面有一个身份未认证的的消息UNAUTHORIZED
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    String token = authService.accredit(username, password);
    if (StringUtils.isBlank(token)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getExpire(), "utf-8", true);
    return ResponseEntity.ok().build();
  }
  
  @GetMapping("verify")
  public ResponseEntity<UserInfo> verify(
      @CookieValue("LY_TOKEN") String token,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (StringUtils.isBlank(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    try {
      UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
      if (userInfo == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getExpire()*60, "utf-8", true);
      return ResponseEntity.ok(userInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  
  }
  
}
  
  


