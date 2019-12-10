package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ovo
 * key=&page=1&rows=5&sortBy=id&desc=false
 */
@RestController
@RequestMapping("brand")
public interface BrandApi {
  /**
   * 按用brand的主键id来查询当前品牌的信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  public Brand queryBrandById(@PathVariable("id") Long id);
  
  @GetMapping("list")
  List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
 

