package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ovo
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
