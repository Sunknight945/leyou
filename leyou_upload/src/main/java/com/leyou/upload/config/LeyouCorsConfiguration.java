package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author ovo
 */
@Configuration
public class LeyouCorsConfiguration {
  @Bean
  public CorsFilter corsFilter() {
    //初始化cors配置对象
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedHeader("*");
    //初始化cors配制源对象
    UrlBasedCorsConfigurationSource CorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    CorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
    //返回corsFilter实例,参数: cors配置源对象
    return new CorsFilter(CorsConfigurationSource);
  }
}
 

