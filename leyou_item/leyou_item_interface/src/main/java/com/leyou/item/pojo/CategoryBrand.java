package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @author ovo
 */
@Data
@Table(name = "tb_category_brand")
@ToString
public class CategoryBrand {
  /**
   * 商品类目id
   */
  private Long categoryId;
  
  /**
   * 品牌id
   */
  private Long brandId;
}

