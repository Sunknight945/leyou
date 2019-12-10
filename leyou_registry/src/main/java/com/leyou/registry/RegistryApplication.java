package com.leyou.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author ovo
 */
@SpringBootApplication
@EnableEurekaServer
public class RegistryApplication {
  public static void main(String[] args) {
    SpringApplication.run(RegistryApplication.class, args);
  }
  
}
 

