package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ovo
 */
@RestController
@RequestMapping
public class GoodsController {
  
  @Autowired
  private GoodsService goodsService;
  
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
  public ResponseEntity<PageResult<SpuBo>> querySpuByPage(@RequestParam(value = "key", required = false) String key, @RequestParam(value = "saleable", required = false) Boolean saleable, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
    PageResult<SpuBo> result = goodsService.querySpuByPage(key, saleable, page, rows);
    if (result == null || CollectionUtils.isEmpty(result.getItems())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(result);
  }
  
  @PostMapping("goods")
  public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
    this.goodsService.saveGoods(spuBo);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
  @PutMapping("goods")
  public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
    this.goodsService.updateGoods(spuBo);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
  
  @GetMapping("spu/detail/{spuId}")
  public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
    SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
    if (spuDetail == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(spuDetail);
  }
  
  
  /**
   * 根据spuId查询sku集合
   *
   * @param spuId
   * @return
   */
  @GetMapping("/sku/list")
  public ResponseEntity<List<Sku>> querySkuListBySpuId(@RequestParam("id") Long spuId) {
    List<Sku> skuList = this.goodsService.querySkuListBySpuId(spuId);
    if (skuList == null || CollectionUtils.isEmpty(skuList)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(skuList);
  }
  
  
  @GetMapping("{id}")
  public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
    Spu spu = goodsService.querySpuById(id);
    if (spu == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(spu);
  }
  
  @GetMapping("spu/{skuId}")
  public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("skuId") long id) {
    Sku sku = goodsService.querySkuBySkuId(id);
    if (sku == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(sku);
  }
}
 

