package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ovo
 */
@RestController
@RequestMapping("spec")
public interface SpecificationApi {
  
  
  /**
   * http://api.leyou.com/api/item/spec/params?gid=5
   * http://api.leyou.com/api/item/spec/params?cid=76
   */
  @GetMapping("params")
  public List<SpecParam> queryParamsByGid(
      @RequestParam(value = "gid", required = false) Long gid,
      @RequestParam(value = "cid", required = false) Long cid,
      @RequestParam(value = "generic", required = false) Boolean generic,
      @RequestParam(value = "searching", required = false) Boolean searching);
  
  
  /**
   * 根据 cid 查询规格参数组下面的规格参数
   *
   * @param cid
   */
  @GetMapping("group/param/{cid}")
  public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid") Long cid);
  
}
 

