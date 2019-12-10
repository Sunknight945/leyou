package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ovo
 */
@Service
public class CategoryService {
  @Autowired
  private CategoryMapper categoryMapper;
  
  /**
   * 根据父节点的id查询子节点
   *
   * @param pid 父id
   * @return 返回子节点
   */
  public List<Category> queryCategoriesByPid(Long pid) {
    Category category = new Category();
    category.setParentId(pid);
    return this.categoryMapper.select(category);
  }
  
  
  public List<String> queryCategoriesNamesByCids(List<Long> cids) {
    List<Category> categories = this.categoryMapper.selectByIdList(cids);
    return categories.stream().map(Category::getName).collect(Collectors.toList());
  }
}
 

