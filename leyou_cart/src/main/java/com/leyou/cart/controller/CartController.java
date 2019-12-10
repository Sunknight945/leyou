package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ovo
 */
public class CartController {
  @Autowired
  CartService cartService;
  
  
  @PostMapping()
  public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
    this.cartService.addCart(cart);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
  @GetMapping()
  public ResponseEntity<List<Cart>> queryCart() {
    List<Cart> carts = this.cartService.queryCarts();
    if (CollectionUtils.isEmpty(carts)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(carts);
  }
  
  @PutMapping()
  public ResponseEntity<Void> updateNum(@RequestBody Cart cart) {
    this.cartService.updateNum(cart);
    
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    
  }
}
 

