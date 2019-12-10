package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ovo
 */
public interface GoodsApi {
  /**
   * 根据 spuId 查询spuDetail
   *
   * @param spuId
   * @return
   */
  @GetMapping("spu/detail/{spuId}")
  public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);
  
  /**
   * 根据分页条件查询spu
   *
   * @param key
   * @param saleable
   * @param page
   * @param rows
   * @return
   */
  @GetMapping("spu/page")
  public PageResult<SpuBo> querySpuByPage(@RequestParam(value = "key", required = false) String key, @RequestParam(value = "saleable", required = false) Boolean saleable, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows);
  
  
  /**
   * 根据spuId查询sku集合
   *
   * @param spuId
   * @return
   */
  @GetMapping("/sku/list")
  public List<Sku> querySkuListBySpuId(@RequestParam("id") Long spuId);
  
  /**
   * 根据spuId查询spu
   *
   * @param id
   * @return
   */
  @GetMapping("{id}")
  public Spu querySpuById(@PathVariable("id") Long id);
  
  /**
   * 根据skuId查询sku
   *
   * @param id
   * @return
   */
  @GetMapping("spu/{skuId}")
  public Sku querySkuBySkuId(@PathVariable("skuId") long id);
}