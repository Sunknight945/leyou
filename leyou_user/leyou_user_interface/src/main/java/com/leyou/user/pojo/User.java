package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /**
   * 用户名
   */
  
  @Length(min = 2,max = 30,message = "用户名必须在2到30位之间")
  private String username;
  /**
   * 密码，加密存储
   * 为了安全考虑。这里对password和salt添加了注解@JsonIgnore，
   * 这样在json序列化时，就不会把password和salt返回。
   */
  @JsonIgnore
  @Length(min = 4,max = 30,message = "密码必须在2到30位之间")
  private String password;
  
  /**
   * 注册手机号
   */
  @Pattern(regexp="^[1][3,4,5,7,8][0-9]{9}$",message = "手机号有误")
  private String phone;
  
  /**
   * 创建时间
   */
  private Date created;
  
  /**
   * 密码加密的salt值
   */
  @JsonIgnore
  private String salt;
}

