package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ovo
 */
@Service
public class SearchService {
  
  @Autowired
  CategoryClient categoryClient;
  @Autowired
  BrandClient brandClient;
  @Autowired
  GoodsClient goodsClient;
  @Autowired
  SpecificationClient specificationClient;
  @Autowired
  GoodsRepository goodsRepository;
  @Autowired
  ElasticsearchTemplate elasticsearchTemplate;
  
  private ObjectMapper mapper = new ObjectMapper();
  
  public Goods buildGoods(Spu spu) throws IOException {
    Goods goods = new Goods();
    // 查询商品分类名称
    List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
    // 查询sku
    List<Sku> skus = this.goodsClient.querySkuListBySpuId(spu.getId());
    // 查询详情
    SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
    // 查询规格参数
    List<SpecParam> params = this.specificationClient.queryParamsByGid(null, spu.getCid3(), true, null);
    
    // 处理sku，仅封装id、价格、标题、图片，并获得价格集合
    List<Long> prices = new ArrayList<>();
    List<Map<String, Object>> skuList = new ArrayList<>();
    skus.forEach(sku -> {
      prices.add(sku.getPrice());
      Map<String, Object> skuMap = new HashMap<>();
      skuMap.put("id", sku.getId());
      skuMap.put("title", sku.getTitle());
      skuMap.put("price", sku.getPrice());
      skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
      skuList.add(skuMap);
    });
    
    // 处理规格参数
    Map<String, Object> genericSpecs = mapper.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
    });
    Map<String, List<Object>> specialSpecs = mapper.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
    });
    // 获取可搜索的规格参数
    //    Map<String, Object> searchSpec = new HashMap<>();
    
    // 过滤规格模板，把所有可搜索的信息保存到Map中
    Map<String, Object> specMap = new HashMap<>();
    params.forEach(p -> {
      if (p.getGeneric()) {
        String value = StringUtils.isNotBlank(genericSpecs.get(p.getId().toString()).toString()) ? genericSpecs.get(p.getId().toString()).toString() : "无";
        if (p.getNumeric()) {
          value = chooseSegment(value, p);
        }
        specMap.put(p.getName(), StringUtils.isBlank(value) ? "其它" : value);
      } else {
        specMap.put(p.getName(), specialSpecs.get(p.getId().toString()));
      }
    });
    
    goods.setId(spu.getId());
    goods.setSubTitle(spu.getSubTitle());
    goods.setBrandId(spu.getBrandId());
    goods.setCid1(spu.getCid1());
    goods.setCid2(spu.getCid2());
    goods.setCid3(spu.getCid3());
    goods.setCreateTime(spu.getCreateTime());
    goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " "));
    goods.setPrice(prices);
    goods.setSkus(mapper.writeValueAsString(skuList));
    goods.setSpecs(specMap);
    return goods;
  }
  
  private String chooseSegment(String value, SpecParam p) {
    double val = NumberUtils.toDouble(value);
    String result = "其它";
    // 保存数值段
    for (String segment : p.getSegments().split(",")) {
      String[] segs = segment.split("-");
      // 获取数值范围
      double begin = NumberUtils.toDouble(segs[0]);
      double end = Double.MAX_VALUE;
      if (segs.length == 2) {
        end = NumberUtils.toDouble(segs[1]);
      }
      // 判断是否在范围内
      if (val >= begin && val < end) {
        if (segs.length == 1) {
          result = segs[0] + p.getUnit() + "以上";
        } else if (begin == 0) {
          result = segs[1] + p.getUnit() + "以下";
        } else {
          result = segment + p.getUnit();
        }
        break;
      }
    }
    return result;
  }
  
  
  public SearchResult search(SearchRequest searchRequest) {
    //获取搜索跳进
    String key = searchRequest.getKey();
    //构建查询条件
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    if (StringUtils.isBlank(key)) {
      return null;
    }
    //1.用key对all字段进行全文检索
    QueryBuilder basicQuery = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
    queryBuilder.withQuery(basicQuery);
    //2.通过sourceFilter设置返回的结果字段,我们只需要id,skus, subTitle
    queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
    
    //添加分类和品牌的聚合
    String categoryAggName = "categories";
    String brandAggName = "brands";
    queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
    queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
    //添加分类和品牌的聚合
    
    
    //分页 有query构建器的withPageAble来进行分页
    queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));
    //用继承了elasticSearchRepository的goodsRepository的search方法来查询并得到查询结果集
    //    AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
    AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
    
    List<Map<String, Object>> categories = getCategoryAggResult(goodsPage.getAggregation(categoryAggName));
    List<Brand> brands = getBrandAggResult(goodsPage.getAggregation(brandAggName));
    
    //判断 是否是一个分类, 只有一个分类时才做规格参数聚合
    List<Map<String, Object>> specs = new ArrayList<>();
    if (!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
      //对规格参数进行聚合
      specs = getParamAggResult((Long) categories.get(0).get("id"), basicQuery);
      
      
    }
    //用PageResult来封装结果并返回.
    return new SearchResult(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent(), categories, brands, specs);
  }
  
  /**
   * 根据查询条件查询规格参数
   *
   * @param cid
   * @param queryBasic
   * @return
   */
  private List<Map<String, Object>> getParamAggResult(Long cid, QueryBuilder queryBasic) {
    try {
      //不管是全局参数还是sku参数, 只要是搜索采纳数,根据分类id查询出来
      List<SpecParam> params = this.specificationClient.queryParamsByGid(null, cid, null, true);
      List<Map<String, Object>> specs = new ArrayList<>();
      NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
      queryBuilder.withQuery(queryBasic);
      //聚合规格参数
      params.forEach(p -> {
        String key = p.getName();
        queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs" + key + ".keyword"));
        
      });
      //查询
      Map<String, Aggregation> aggs = this.elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();
      //解析聚合结果
      params.forEach(param -> {
        HashMap<String, Object> spec = new HashMap<>();
        String key = param.getName();
        spec.put("k", key);
        StringTerms terms = (StringTerms) aggs.get(key);
        spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
        specs.add(spec);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * 解析分类的聚合结果集
   *
   * @param aggregation
   * @return
   */
  private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
    //获取聚合中的桶
    LongTerms terms = (LongTerms) aggregation;
    return terms.getBuckets().stream().map(bucket -> {
      Map<String, Object> map = new HashMap<>();
      long id = bucket.getKeyAsNumber().longValue();
      List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(id));
      map.put("id", id);
      map.put("name", names.get(0));
      return map;
    }).collect(Collectors.toList());
  }
  
  /**
   * 解析品牌的聚合结果集
   *
   * @param aggregation
   * @return
   */
  private List<Brand> getBrandAggResult(Aggregation aggregation) {
    LongTerms terms = (LongTerms) aggregation;
    return terms.getBuckets().stream().map(bucket -> {
      return this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
    }).collect(Collectors.toList());
  }
  
  /**
   * 我们需要在SearchService中拓展两个方法，创建和删除索引：
   *
   * @param id
   */
  public void save(Long id) throws IOException {
    //查询spu
    Spu spu = this.goodsClient.querySpuById(id);
    Goods goods = this.buildGoods(spu);
    this.goodsRepository.save(goods);
  }
  
  public void delete(Long id) {
    this.goodsRepository.deleteById(id);
  }
}

