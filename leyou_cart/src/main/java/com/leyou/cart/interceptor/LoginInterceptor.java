package com.leyou.cart.interceptor;

import com.leyou.auth.entiy.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ovo
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {
  @Autowired
  JwtProperties jwtProperties;
  
  private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();
  
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
    if (StringUtils.isBlank(token)) {
      return false;
    }
    UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
    if (userInfo == null) {
      return false;
    }
    THREAD_LOCAL.set(userInfo);
    return true;
  }
  
  public static UserInfo getUserInfo() {
    return THREAD_LOCAL.get();
  }
  
  
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    THREAD_LOCAL.remove();
  }
}
 

