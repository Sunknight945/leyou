package com.leyou.cart.service;

/**
 * @author ovo
 */
/*@Service
public class CartService {
  @Autowired
  StringRedisTemplate stringRedisTemplate;
  @Autowired
  GoodsClient goodsClient;
  @Autowired
  private static final String KEY_PREFIX = "user:cart";
  public void addCart(Cart cart) {
    //获取用户信息
    UserInfo userInfo = LoginInterceptor.getUserInfo();
    //查询购物车记录
    BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
    String key = cart.getSkuId().toString();
    Integer num = cart.getNum();
    //判断当前的商品是否在购物车中
    if (hashOperations.hasKey(key)) {
      //在更新数量
      String cartJson = hashOperations.get(key).toString();
      cart = JsonUtils.parse(cartJson, Cart.class);
      cart.setNum(cart.getNum() + num);
    } else {
      //不在更新购物车
      Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());
      cart.setUserId(userInfo.getId());
      cart.setOwnSpec(sku.getOwnSpec());
      cart.setPrice(sku.getPrice());
      cart.setTitle(sku.getTitle());
      cart.setEnable(sku.getEnable());
      cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
      cart.setPrice(sku.getPrice());
    }
    hashOperations.put(key, JsonUtils.serialize(cart));
  }
}*/

import com.leyou.auth.entiy.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
  @Autowired
  GoodsClient goodsClient;
  @Autowired
  StringRedisTemplate stringRedisTemplate;
  static final String KEY_PREFIX = "user:cart";
  
  public void addCart(Cart cart) {
    UserInfo userInfo = LoginInterceptor.getUserInfo();
    BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
    String key = hashOperations.get(cart.getSkuId()).toString();
    Integer num = cart.getNum();
    if (hashOperations.hasKey(key)) {
      String jsonCart = hashOperations.get(key).toString();
      cart = JsonUtils.parse(jsonCart, Cart.class);
      cart.setNum(cart.getNum() + num);
    } else {
      Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());
      cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
      cart.setOwnSpec(sku.getOwnSpec());
      cart.setEnable(sku.getEnable());
      cart.setTitle(sku.getTitle());
      cart.setPrice(sku.getPrice());
    }
    hashOperations.put(key, JsonUtils.serialize(cart));
  }
  
  public List<Cart> queryCarts() {
    UserInfo userInfo = LoginInterceptor.getUserInfo();
    if (!this.stringRedisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
      return null;
    }
    BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
    //获取购物车map中所有值集合
    List<Object> jsonCarts = hashOperations.values();
    if (CollectionUtils.isEmpty(jsonCarts)) {
      return null;
    }
    List<Cart> cartList = jsonCarts.stream().map(jsonCart -> JsonUtils.parse(jsonCart.toString(), Cart.class)).collect(Collectors.toList());
    return cartList;
  }
  
  /*public void updateNum(Cart cart) {
    UserInfo userInfo = LoginInterceptor.getUserInfo();
    BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
    if (!this.stringRedisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
      return;
    }
    String key = hashOperations.get(cart.getSkuId()).toString();
    Integer num = cart.getNum();
    if (hashOperations.hasKey(key)) {
      String jsonCart = hashOperations.get(key).toString();
      cart = JsonUtils.parse(jsonCart, Cart.class);
      cart.setNum(cart.getNum() + num);
    }
    hashOperations.put(key, JsonUtils.serialize(cart));
  }*/
  
  public void updateNum(Cart cart) {
    UserInfo userInfo = LoginInterceptor.getUserInfo();
    BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
    String key = hashOperations.get(cart.getSkuId()).toString();
    Integer num = cart.getNum();
    if (hashOperations.hasKey(key)) {
      return;
    }
    String jsonCart = hashOperations.get(key).toString();
    cart = JsonUtils.parse(jsonCart.toString(), Cart.class);
    cart.setNum(cart.getNum() + num);
    hashOperations.put(key, JsonUtils.serialize(cart));
  }
}
