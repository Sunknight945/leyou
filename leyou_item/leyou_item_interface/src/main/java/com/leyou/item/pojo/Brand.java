package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "tb_brand")
public class Brand {
  /**
   * 品牌id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  /**
   * 品牌名称
   */
  private String name;
  
  /**
   * 品牌图片地址
   */
  private String image;
  
  /**
   * 品牌的首字母
   */
  private String letter;
}

