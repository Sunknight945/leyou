package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author ovo
 */
@Service
public class GoodsCreateHtml {
  
  
  @Autowired
  GoodsService goodsService;
  @Autowired
  TemplateEngine engine;
  
  @Value("${Leyou.path.ItemPagePath}")
  private String ItemPagePath;
  
  public void createHtml(Long id) throws Exception {
    // 创建上下文，
    Context context = new Context();
    // 把数据加入上下文
    context.setVariables(this.goodsService.loadDate(id));
    
    // 创建输出流，关联到一个临时文件
    File temp = new File(id + ".html");
    // 目标页面文件
    File dest = createPath(id);
    // 备份原页面文件
    File bak = new File(id + "_bak.html");
    try (PrintWriter writer = new PrintWriter(temp, "UTF-8")) {
      // 利用thymeleaf模板引擎生成 静态页面
      engine.process("item", context, writer);
      
      if (dest.exists()) {
        // 如果目标文件已经存在，先备份
        dest.renameTo(bak);
      }
      // 将新页面覆盖旧页面
      FileCopyUtils.copy(temp, dest);
      // 成功后将备份页面删除
      bak.delete();
    } catch (IOException e) {
      // 失败后，将备份页面恢复
      bak.renameTo(dest);
      // 重新抛出异常，声明页面生成失败
      throw new Exception(e);
    } finally {
      // 删除临时页面
      if (temp.exists()) {
        temp.delete();
      }
    }
  }
  
  private File createPath(Long id) {
    if (id == null) {
      return null;
    }
    File dest = new File(this.ItemPagePath);
    if (!dest.exists()) {
      dest.mkdirs();
    }
    return new File(dest, id + ".html");
  }
  
  public void deleteHtml(Long id) {
    File file = new File(ItemPagePath + id+".html");
    if (file.exists()) {
      file.delete();
    }
  }
  
}
 

