package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ovo
 */
@Service
public class GoodsService {
  @Autowired
  private BrandClient brandClient;
  @Autowired
  private GoodsClient goodsClient;
  @Autowired
  private SpecificationClient specificationClient;
  @Autowired
  private CategoryClient categoryClient;
  
  public Map<String, Object> loadDate(Long spuId) {
    
    Map<String, Object> model = new HashMap<>();
    Spu spu = this.goodsClient.querySpuById(spuId);
    //查询spuDetail
    SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
    //查询分类: map<String, Object>
    List<Map<String, Object>> categories = new ArrayList<>();
    //初始化一个分类的map
    List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
    List<String> names = this.categoryClient.queryNamesByIds(cids);
    for (int i = 0; i < cids.size(); i++) {
      Map<String, Object> map = new HashMap<>();
      map.put("id", cids.get(i));
      map.put("name", names.get(i));
      categories.add(map);
    }
    Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
    List<Sku> skus = this.goodsClient.querySkuListBySpuId(spu.getId());
    List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());
    List<SpecParam> params = this.specificationClient.queryParamsByGid(null, spu.getCid3(), false, null);
    //初始化特殊规格参数的map
    Map<Long, String> paramMap = new HashMap<>();
    params.forEach(param -> paramMap.put(param.getId(), param.getName()));
    
    
    model.put("spu", spu);
    model.put("spuDetail", spuDetail);
    model.put("categories", categories);
    model.put("brand", brand);
    model.put("skus", skus);
    model.put("groups", groups);
    model.put("paramMap", params);
    
    return model;
  }
}
 

