package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ovo
 * key=&page=1&rows=5&sortBy=id&desc=false
 */
@RestController
@RequestMapping("brand")
public class BrandController {
  @Autowired
  private BrandService brandService;
  
  @GetMapping("page")
  public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
      @RequestParam(value = "key", required = false) String key,
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "rows", defaultValue = "5") Integer rows,
      @RequestParam(value = "sortBy", required = false) String sortBy,
      @RequestParam(value = "desc", required = false) Boolean desc
  ) {
    PageResult<Brand> brands = brandService.queryBrandsByPage(key, page, rows, sortBy, desc);
    if (CollectionUtils.isEmpty(brands.getItems())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(brands);
  }
  
  @PostMapping
  public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
    this.brandService.saveBrand(brand, cids);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
  /**
   * perfect
   * api.leyou.com/api/item/brand/cid/79
   */
  @GetMapping("cid/{cid}")
  public ResponseEntity<List<Brand>> queryBrandByBid(@PathVariable("cid") Long cid) {
    List<Brand> brands = this.brandService.queryBrandsByBid(cid);
    if (CollectionUtils.isEmpty(brands)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(brands);
  }
  
  @GetMapping("{id}")
  public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
    Brand brand = this.brandService.queryBrandById(id);
    if (brand == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(brand);
    
  }
  
  /**
   * 根据多个id查询品牌
   * @param ids
   * @return
   */
  @GetMapping("list")
  public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
    List<Brand> list = this.brandService.queryBrandByIds(ids);
    if(list == null){
      new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(list);
  }
}
 

