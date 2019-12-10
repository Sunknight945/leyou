package com.leyou.gateway.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.JwtProperties;
import com.leyou.gateway.config.LoginProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ovo
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, LoginProperties.class})
public class LoginFilter extends ZuulFilter {
  
  
  @Autowired
  LoginProperties loginProperties;
  @Autowired
  JwtProperties jwtProperties;
  
  /**
   * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
   * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
   * We also support a "static" type for static responses see  StaticResponseFilter.
   * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
   *
   * @return A String representing that type
   */
  @Override
  public String filterType() {
    return "pre";
  }
  
  /**
   * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
   * important for a filter. filterOrders do not need to be sequential.
   *
   * @return the int order of a filter
   */
  @Override
  public int filterOrder() {
    return 10;
  }
  
  /**
   * a "true" return from this method means that the run() method should be invoked
   *
   * @return true if the run() method should be invoked. false will not invoke the run() method
   */
  @Override
  public boolean shouldFilter() {
    RequestContext context = RequestContext.getCurrentContext();
    HttpServletRequest request = context.getRequest();
    String requestURI = request.getRequestURI();
    
    return !isAllowPath(requestURI);
  }
  
  private boolean isAllowPath(String requestURI) {
    boolean flag = false;
    List<String> allowPaths = this.loginProperties.getAllowPaths();
    for (String allowPath : allowPaths) {
      if (requestURI.startsWith(allowPath)) {
        flag = true;
        break;
      }
    }
    return flag;
  }
  
  
  /**
   * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
   *
   * @return Some arbitrary artifact may be returned. Current implementation ignores it.
   * @throws ZuulException if an error occurs during execution.
   */
  @Override
  public Object run() throws ZuulException {
    RequestContext context = RequestContext.getCurrentContext();
    HttpServletRequest request = context.getRequest();
    String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
    if (StringUtils.isBlank(token)) {
      context.setSendZuulResponse(false);
      context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
    }
    try {
      JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
    } catch (Exception e) {
      context.setSendZuulResponse(false);
      context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
    }
    return null;
  }
}
 

