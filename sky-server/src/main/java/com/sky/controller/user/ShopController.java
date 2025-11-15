package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Api(tags = "店铺信息")
@Slf4j
@RequestMapping("/user/shop")
@RequiredArgsConstructor
public class ShopController {

    private final RedisUtil redisUtil;

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus(){
        Integer shopStatus = (Integer) redisUtil.get("SHOP_STATUS");
        log.info("获取到的店铺状态为：{}", shopStatus == 1? "营业中": "打烊中");
        return Result.success(shopStatus);
    }

}
