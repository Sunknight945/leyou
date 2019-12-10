package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ovo
 * redundant
 */
@Service
public class SpecificationService {
  @Autowired
  SpecGroupMapper specGroupMapper;
  @Autowired
  SpecParamMapper specParamMapper;
  
  public List<SpecGroup> querySpecsByCid(Long cid) {
    SpecGroup specGroup = new SpecGroup();
    specGroup.setCid(cid);
    return specGroupMapper.select(specGroup);
  }
  
  /**
   * 根据条件查询规格参数
   *
   * @param gid
   * @param cid
   * @param generic
   * @param searching
   * @return
   */
  public List<SpecParam> queryParamsByGid(Long gid, Long cid, Boolean generic, Boolean searching) {
    SpecParam specParam = new SpecParam();
    specParam.setGroupId(gid);
    specParam.setCid(cid);
    specParam.setGeneric(generic);
    specParam.setSearching(searching);
    return specParamMapper.select(specParam);
  }
  
  public List<SpecGroup> queryGroupsWithParam(Long cid) {
    List<SpecGroup> groups = this.querySpecsByCid(cid);
    groups.forEach(group -> {
      List<SpecParam> params = this.queryParamsByGid(group.getId(), null, null, null);
      group.setParams(params);
    });
    return groups;
  }
}
 

