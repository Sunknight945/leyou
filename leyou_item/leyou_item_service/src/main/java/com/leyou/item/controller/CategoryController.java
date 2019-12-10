package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
public class CategoryController {
  
  @Autowired
  private CategoryService categoryService;
  
  
  @GetMapping("list")
  public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
    if (pid == null || pid < 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    List<Category> list = this.categoryService.queryCategoriesByPid(pid);
    if (CollectionUtils.isEmpty(list)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(list);
  }
  
  /**
   * 根据分类商品分类的三个分类id 查询三级分类的名称 数组
   * @param ids
   * @return
   */
  @GetMapping
  public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
    List<String> names = this.categoryService.queryCategoriesNamesByCids(ids);
    if (CollectionUtils.isEmpty(names)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(names);
  }
}
 

