package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
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
@RequestMapping("spec")
public class SpecificationController {
  /**
   * http://api.leyou.com/api/item/spec/groups/207
   */
  @Autowired
  SpecificationService specificationService;
  
  @GetMapping("groups/{cid}")
  public ResponseEntity<List<SpecGroup>> querySpecByCid(@PathVariable("cid") Long cid) {
    List<SpecGroup> specGroups = this.specificationService.querySpecsByCid(cid);
    if (CollectionUtils.isEmpty(specGroups)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(specGroups);
  }
  
  /**
   * http://api.leyou.com/api/item/spec/params?gid=5
   * http://api.leyou.com/api/item/spec/params?cid=76
   */
  @GetMapping("params")
  public ResponseEntity<List<SpecParam>> queryParamsByGid(
      @RequestParam(value = "gid", required = false) Long gid,
      @RequestParam(value = "cid", required = false) Long cid,
      @RequestParam(value = "generic", required = false) Boolean generic,
      @RequestParam(value = "searching", required = false) Boolean searching) {
    List<SpecParam> params = this.specificationService.queryParamsByGid(gid, cid, generic, searching);
    if (CollectionUtils.isEmpty(params)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(params);
  }
  
  /**
   * 根据 cid 查询规格参数组下面的规格参数
   * @param cid
   * @return
   */
  @GetMapping("group/param/{cid}")
  public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid") Long cid) {
    List<SpecGroup> groups;
    groups = this.specificationService.queryGroupsWithParam(cid);
    if (CollectionUtils.isEmpty(groups)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(groups);
  }
  
}
 

