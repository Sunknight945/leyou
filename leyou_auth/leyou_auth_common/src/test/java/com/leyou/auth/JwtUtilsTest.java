package com.leyou.auth;


import com.leyou.auth.entiy.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;


public class JwtUtilsTest {
  private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";
  
  private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";
  
  private PublicKey publicKey;
  
  private PrivateKey privateKey;
  
  @Test
  public void testRsa() throws Exception {
    RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
  }
  
  @Before
  public void testGetRsa() throws Exception {
    this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
  }
  
  @Test
  public void testGenerateToken() throws Exception {
    // 生成token
    String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
    System.out.println("token = " + token);
  }
  
  @Test
  public void testParseToken() throws Exception {
    String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU3NDQyNTAxOH0.hddecnXcT4TBhEB8FVg_dlBm3So4EsiN4-q1ow3WRdYATSIN0xN6myL6qH-5QrGzduk60RFLnTqjCeyTDJS2RaIc0H5Gje3Qp3lSSkd-wRd48kXKt6OWWXfMnaFMyBgqUbwShgE4IKr5Q4kEw99aMz4MyX1WMzXmPpsOCCNz81E";
    
    // 解析token
    UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
    System.out.println("id: " + user.getId());
    System.out.println("userName: " + user.getUsername());
  }
  
}