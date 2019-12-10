package com.leyou.cart.config;

import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author ovo
 */
@Component
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {
  
  
  private String pubKeyPath;
  private String cookieName;
  private PublicKey publicKey; // 公钥
  
  
  public static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);
  
  
  public String getPubKeyPath() {
    return pubKeyPath;
  }
  
  
  public String getCookieName() {
    return cookieName;
  }
  
  public PublicKey getPublicKey() {
    return publicKey;
  }
  
  
  public void setPubKeyPath(String pubKeyPath) {
    this.pubKeyPath = pubKeyPath;
  }
  
  
  public void setCookieName(String cookieName) {
    this.cookieName = cookieName;
  }
  
  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }
  
  
  @PostConstruct
  public void init() {
    try {
      //获取公钥和私钥
      PublicKey publicKey = RsaUtils.getPublicKey(pubKeyPath);
    } catch (Exception e) {
      LOGGER.error("初始化公钥和私钥失败!", e);
      throw new RuntimeException();
    }
    
  }
  
}
 

