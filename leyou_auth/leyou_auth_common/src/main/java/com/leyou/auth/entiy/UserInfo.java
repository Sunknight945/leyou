package com.leyou.auth.entiy;

public class UserInfo {
  
  private Long id;
  
  private String username;
  
  public UserInfo() {
  }
  
  public UserInfo(Long id, String username) {
    this.id = id;
    this.username = username;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  
  @Override
  public String toString() {
    return "UserInfo{" +
               "id=" + id +
               ", username='" + username + '\'' +
               '}';
  }
  
}