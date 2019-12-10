package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author ovo
 */
@Service
public class BrandService {
  
  @Autowired
  private BrandMapper brandMapper;
  
  /**
   * 按参数 分页查询, 排序, 和搜索品牌信息
   */
  public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
    Example example = new Example(Brand.class);
    Example.Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(key)) {
      criteria.andLike("name", "% " + key + " %");
      criteria.orEqualTo("letter", key);
    }
    if (page < 1) {
      page = 1;
    }
    if (rows < 5) {
      rows = 5;
    }
    PageHelper.startPage(page, rows);
    if (StringUtils.isNotBlank(sortBy)) {
      example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
    }
    
    List<Brand> brands = brandMapper.selectByExample(example);
    PageInfo<Brand> pageInfo = new PageInfo<>(brands);
    return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
  }
  
  
  @Transactional(rollbackFor = Exception.class)
  public void saveBrand(Brand brand, List<Long> cids) {
    this.brandMapper.insertSelective(brand);
//    cids.forEach(cid -> this.brandMapper.insertCategoryAndBrand(cid, brand.getId()));
    for (Long cid : cids) {
      this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
    }
  }
  
  
  public List<Brand> queryBrandsByBid(Long cid) {
    List<Long> bids = brandMapper.selectBidListByCid(cid);
    return brandMapper.selectByIdList(bids);
  }
  
  public Brand queryBrandById(Long id) {
    return this.brandMapper.selectByPrimaryKey(id);
  }
  
  
  public List<Brand> queryBrandByIds(List<Long> ids) {
    return this.brandMapper.selectByIdList(ids);
  }
}
 

