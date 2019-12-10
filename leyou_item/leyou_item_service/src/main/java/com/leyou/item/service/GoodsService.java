package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @author ovo
 */
@Service
public class GoodsService {
  @Autowired
  private SpuMapper spuMapper;
  @Autowired
  private BrandMapper brandMapper;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private SpuDetailMapper spuDetailMapper;
  @Autowired
  private SkuMapper skuMapper;
  @Autowired
  private StockMapper stockMapper;
  
  @Autowired
  private AmqpTemplate amqpTemplate;
  
  public static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);
  
  
  public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
    Example example = new Example(Spu.class);
    Example.Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(key)) {
      criteria.andLike("title", "%" + key + "%");
    }
    if (saleable != null) {
      criteria.andEqualTo("saleable", saleable);
    }
    PageHelper.startPage(page, rows);
    List<Spu> spuList = spuMapper.selectByExample(example);
    PageInfo<Spu> pageInfo = new PageInfo<>(spuList);
    List<SpuBo> spuBoList = spuList.stream().map(spu -> {
      SpuBo spuBo = new SpuBo();
      copyProperties(spu, spuBo);
      
      spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
      spuBo.setCname(StringUtils.join(this.categoryService.queryCategoriesNamesByCids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())), "/"));
      return spuBo;
    }).collect(Collectors.toList());
    return new PageResult<>(pageInfo.getTotal(), spuBoList);
    
  }
  
  /**
   * 新增商品
   *
   * @param spuBo
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveGoods(SpuBo spuBo) {
    //1.spu
    spuBo.setId(null);
    spuBo.setSaleable(true);
    spuBo.setValid(true);
    spuBo.setCreateTime(new Date());
    spuBo.setLastUpdateTime(spuBo.getCreateTime());
    this.spuMapper.insertSelective(spuBo);
    //2.sku_detail
    SpuDetail spuDetail = spuBo.getSpuDetail();
    spuDetail.setSpuId(spuBo.getId());
    this.spuDetailMapper.insertSelective(spuDetail);
    //3.sku
    saveSkuAndStock(spuBo);
    //发送消息
    this.sendMessage(spuBo.getId(), "insert");
  }
  
  private void saveSkuAndStock(SpuBo spuBo) {
    for (Sku sku : spuBo.getSkus()) {
      sku.setId(null);
      sku.setSpuId(spuBo.getId());
      sku.setCreateTime(new Date());
      sku.setLastUpdateTime(sku.getCreateTime());
      this.skuMapper.insertSelective(sku);
      //新增库存stock
      Stock stock = new Stock();
      stock.setSkuId(sku.getId());
      stock.setStock(sku.getStock());
      stockMapper.insertSelective(stock);
    }
  }
  
  @Transactional(rollbackFor = Exception.class)
  public void updateGoods(SpuBo spuBo) {
    //1.删stock
    Sku record = new Sku();
    record.setSpuId(spuBo.getId());
    List<Sku> skuList = this.skuMapper.select(record);
    //2.删sku
    skuList.forEach(sku -> stockMapper.deleteByPrimaryKey(sku.getId()));
    Sku sku = new Sku();
    sku.setSpuId(spuBo.getId());
    this.skuMapper.delete(sku);
    //3.增sku4.增stock
    this.saveSkuAndStock(spuBo);
    //5.更spu和spuDetail
    spuBo.setCreateTime(null);
    spuBo.setLastUpdateTime(new Date());
    spuBo.setValid(null);
    spuBo.setSaleable(null);
    this.spuMapper.updateByPrimaryKeySelective(spuBo);
    this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
    //发送消息
    this.sendMessage(spuBo.getId(), "update");
    
  }
  
  public SpuDetail querySpuDetailBySpuId(Long spuId) {
    return spuDetailMapper.selectByPrimaryKey(spuId);
  }
  
  public List<Sku> querySkuListBySpuId(Long spuId) {
    Sku sku = new Sku();
    sku.setSpuId(spuId);
    return this.skuMapper.select(sku);
  }
  
  public Spu querySpuById(Long id) {
    return this.spuMapper.selectByPrimaryKey(id);
  }
  
  
  public void sendMessage(Long id, String type) {
    if (id == null) {
      return;
    }
    try {
      this.amqpTemplate.convertAndSend("item." + type, id);
    } catch (Exception e) {
      LOGGER.error("{}商品的消息发送失败:商品的id{}", type, id, e);
      e.printStackTrace();
    }
  }
  
  
  public Sku querySkuBySkuId(long id) {
    return this.skuMapper.selectByPrimaryKey(id);
  }
}
  
  


