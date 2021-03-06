package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ovo
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WOri {
  public static void main(String[] args) {
    SpringApplication.run(WOri.class, args);
  }
}
 

