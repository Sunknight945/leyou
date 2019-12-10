package com.leyou.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ovo
 */
@Component
@ConfigurationProperties(prefix = "leyou.filter")
public class LoginProperties {
  
  
  List<String> allowPaths;
  
  public List<String> getAllowPaths() {
    return allowPaths;
  }
  
  public void setAllowPaths(List<String> allowPaths) {
    this.allowPaths = allowPaths;
  }
  
  public LoginProperties(List<String> allowPaths) {
    this.allowPaths = allowPaths;
  }
}
 

