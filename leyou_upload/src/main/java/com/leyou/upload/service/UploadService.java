package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author ovo
 */
@Service
public class UploadService {
  @Autowired
  FastFileStorageClient fastFileStorageClient;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
  private static final List<String> IMAGE_TYPE = Arrays.asList("image/gif", "image/jpeg", "image/png");
  
  public String uploadImage(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    String contentType = file.getContentType();
    if (!IMAGE_TYPE.contains(contentType)) {
      LOGGER.info("upload image error 这个 {} 文件类型不合法.", originalFilename);
      return null;
    }
    
    String domianName = null;
    try {
      BufferedImage read = ImageIO.read(file.getInputStream());
      if (read == null) {
        LOGGER.info("{}图片不存在", originalFilename);
        return null;
      }
      //保存到服务器
      StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), StringUtils.substringAfterLast(originalFilename, "."), null);
      String fullPath = storePath.getFullPath();
      domianName = "http://image.leyou.com/";
      System.out.println(fullPath);
      return domianName + fullPath;
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.error("服务器内部错误{}, {}", originalFilename,e.getMessage());
      return null;
    }
  }
}
 

