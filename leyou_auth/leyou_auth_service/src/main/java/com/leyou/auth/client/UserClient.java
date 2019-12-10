package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ovo
 */
@FeignClient("user-server")
public interface UserClient extends UserApi {

}