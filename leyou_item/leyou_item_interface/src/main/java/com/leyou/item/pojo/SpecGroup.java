package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * @author ovo
 */
@Data
@ToString
@Table(name = "tb_spec_group")
public class SpecGroup {
  /**
   * 主键
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /**
   * 商品分类id,一个分类下有多个规格组
   */
  private Long cid;
  
  /**
   * 规格组的名称
   */
  private String name;
  @Transient
  private List<SpecParam> params;
}

