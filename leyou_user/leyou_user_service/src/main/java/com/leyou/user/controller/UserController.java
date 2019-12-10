package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ovo
 */
@RestController
public class UserController {
  
  @Autowired
  UserService userService;
  
  
  @GetMapping("/check/{data}/{type}")
  public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data,
                                               @PathVariable("type") Integer type) {
    Boolean flag = this.userService.checkUserData(data, type);
    if (flag == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(flag);
  }
  
  @PostMapping("code")
  public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
    Boolean flag = this.userService.sendVerifyCode(phone);
    if (flag == null || !flag) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
  /**
   * 用户注册 里面有用户填写的信息与手机上的验证码
   *
   * @param user
   * @param code
   * @return
   */
  @PostMapping("register")
  public ResponseEntity<Void> userRegister(@Valid User user, @RequestParam("code") String code) {
    this.userService.userRegister(user, code);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
  /**
   * 查询功能，根据参数中的用户名和密码查询指定用户
   * @param username
   * @param password
   * @return
   *browser
   * Signature
   */
  @GetMapping("query")
  public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password) {
    User qUser = this.userService.queryUser(username,password);
    if (qUser == null) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(qUser);
  }
  
}
 

