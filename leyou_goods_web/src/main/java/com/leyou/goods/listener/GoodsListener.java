package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsCreateHtml;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ovo
 */


@Component
public class GoodsListener {
  @Autowired
  GoodsCreateHtml goodsCreateHtml;
  
  @RabbitListener(bindings = @QueueBinding(
      value = @Queue(value = "leyou.item.save.queue", durable = "true"),
      exchange = @Exchange(value = "leyou.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
      key = {"item.insert", "item.update"}))
  public void save(Long id) throws Exception {
    if (id == null) {
      return;
    }
    //这里直接抛出异常可以让rabittmq 自己检测到异常从而指导 ack 确认不成功
    this.goodsCreateHtml.createHtml(id);
  }
  
  
  
  @RabbitListener(bindings = @QueueBinding(
      value = @Queue(value = "leyou.item.delete.queue", durable = "true"),
      exchange = @Exchange(value = "leyou.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
      key =   "item.delete"))
  public void delete(Long id) throws Exception {
    if (id == null) {
      return;
    }
    //这里直接抛出异常可以让rabittmq 自己检测到异常从而指导 ack 确认不成功
    this.goodsCreateHtml.deleteHtml(id);
  }
  
}

 

