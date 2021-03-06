package com.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ovo
 * <p>
 * leyou:
 * sms:
 * accessKeyId: JWffwFJIwada # 你自己的accessKeyId
 * accessKeySecret: aySRliswq8fe7rF9gQyy1Izz4MQ # 你自己的AccessKeySecret
 * signName: 乐优商城 # 签名名称
 * verifyCodeTemplate: SMS_133976814 # 模板名称
 */
@ConfigurationProperties(prefix = "leyou.sms")
public class SmsProperties {
  
  String accessKeyId;
  String accessKeySecret;
  String signName;
  String verifyCodeTemplate;
  
  public String getAccessKeyId() {
    return accessKeyId;
  }
  
  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }
  
  public String getAccessKeySecret() {
    return accessKeySecret;
  }
  
  public void setAccessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
  }
  
  public String getSignName() {
    return signName;
  }
  
  public void setSignName(String signName) {
    this.signName = signName;
  }
  
  public String getVerifyCodeTemplate() {
    return verifyCodeTemplate;
  }
  
  public void setVerifyCodeTemplate(String verifyCodeTemplate) {
    this.verifyCodeTemplate = verifyCodeTemplate;
  }
}
 

