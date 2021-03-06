package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ovo
 */


@RestController
public class GoodsController {
  
  @Autowired
  GoodsService goodsService;
  
  
  @GetMapping("item/{id}.html")
  public String toItemPage(@PathVariable("id") Long id, Model model) {
    Map<String, Object> map = goodsService.loadDate(id);
    model.addAllAttributes(map);
    return "item";
  }
}


