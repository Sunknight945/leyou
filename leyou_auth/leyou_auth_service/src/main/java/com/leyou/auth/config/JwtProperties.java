package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ovo
 */
@Component
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {
  
  
  private String secret;
  private String pubKeyPath;
  private String priKeyPath;
  private Integer expire;
  private String cookieName;
  
  public static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);
  private PublicKey publicKey; // 公钥
  
  private PrivateKey privateKey; // 私钥
  
  
  public String getSecret() {
    return secret;
  }
  
  public String getPubKeyPath() {
    return pubKeyPath;
  }
  
  public String getPriKeyPath() {
    return priKeyPath;
  }
  
  public Integer getExpire() {
    return expire;
  }
  
  public String getCookieName() {
    return cookieName;
  }
  
  public PublicKey getPublicKey() {
    return publicKey;
  }
  
  public PrivateKey getPrivateKey() {
    return privateKey;
  }
  
  public void setSecret(String secret) {
    this.secret = secret;
  }
  
  public void setPubKeyPath(String pubKeyPath) {
    this.pubKeyPath = pubKeyPath;
  }
  
  public void setPriKeyPath(String priKeyPath) {
    this.priKeyPath = priKeyPath;
  }
  
  public void setExpire(Integer expire) {
    this.expire = expire;
  }
  
  public void setCookieName(String cookieName) {
    this.cookieName = cookieName;
  }
  
  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }
  
  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }
  
  
  @PostConstruct
  public void init() {
    try {
      File pubKey = new File(pubKeyPath);
      File priKey = new File(priKeyPath);
      if (!pubKey.exists() || !priKey.exists()) {
        //生成公钥和私钥
        RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
      }
      //获取公钥和私钥
      PublicKey publicKey = RsaUtils.getPublicKey(pubKeyPath);
      PrivateKey privateKey = RsaUtils.getPrivateKey(priKeyPath);
    } catch (Exception e) {
      LOGGER.error("初始化公钥和私钥失败!", e);
      throw new RuntimeException();
    }
    
    
  }
  
}
 

