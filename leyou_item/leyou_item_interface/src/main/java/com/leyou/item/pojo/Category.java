package com.leyou.item.pojo;


import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ovo
 */
@Data
@ToString
@Table(name = "tb_category")
public class Category {
  /**
   * 类目id
   */
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /**
   * 类目名称
   */
  private String name;
  
  /**
   * 父类目id,顶级类目填0
   */
  private Long parentId;
  
  /**
   * 是否为父节点，0为否，1为是
   */
  private Boolean isParent;
  
  /**
   * 排序指数，越小越靠前
   */
  private Integer sort;
}

