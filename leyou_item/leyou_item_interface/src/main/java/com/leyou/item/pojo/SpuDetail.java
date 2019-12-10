package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "tb_spu_detail")
public class SpuDetail {
  /**
   *
   */
  @Id
  private Long spuId;
  
  /**
   * 商品描述信息
   */
  private String description;
  
  /**
   * 全部规格参数数据
   */
  private String specialSpec;
  
  /**
   * 特有规格参数及可选值信息，json格式
   */
  private String genericSpec;
  
  /**
   * 包装清单
   */
  private String packingList;
  
  /**
   * 售后服务
   */
  private String afterService;
}

