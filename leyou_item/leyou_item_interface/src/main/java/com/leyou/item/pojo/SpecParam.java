package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author ovo
 */
@Data
@Table(name = "tb_spec_param")
@ToString
public class SpecParam {
  /**
   * 主键
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /**
   * 商品分类id
   */
  private Long cid;
  
  /**
   *
   */
  private Long groupId;
  
  /**
   * 参数名
   */
  private String name;
  
  /**
   * 是否是数字参数类型，true还是false
   */
  @Column(name = "'numeric'")
  private Boolean numeric;
  
  /**
   * 数字类型参数的单位，非数字类型可以为空
   */
  private String unit;
  
  /**
   * 是否是sku通用属性，true还是false
   */
  private Boolean generic;
  
  /**
   * 是否用于搜索过滤，true还是false
   */
  private Boolean searching;
  
  /**
   * 数值类型参数，如果需要搜索，则添加分段间隔值，如cpu频率间隔：0.5-1.0
   */
  private String segments;
}

