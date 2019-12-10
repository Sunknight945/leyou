package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "tb_stock")
public class Stock {
  /**
   * 库存对应的商品sku id
   */
  @Id
  private Long skuId;
  
  /**
   * 可秒杀库存
   */
  private Integer seckillStock;
  
  /**
   * 秒杀总数量
   */
  private Integer seckillTotal;
  
  /**
   * 库存数量
   */
  private Integer stock;
}

