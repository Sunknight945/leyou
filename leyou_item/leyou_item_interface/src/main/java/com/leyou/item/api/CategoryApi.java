package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ovo
 */
@RestController
@RequestMapping("category")
public interface CategoryApi {
  
  
  /**
   * 根据分类商品分类的三个分类id 查询三级分类的名称 数组
   *
   * @param ids
   * @return
   */
  @GetMapping
  public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
 

