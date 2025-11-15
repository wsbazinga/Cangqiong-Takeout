package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Api(tags = "店铺信息")
@Slf4j
@RequestMapping("/admin/shop")
@RequiredArgsConstructor
public class ShopController {

    private final RedisUtil redisUtil;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable("status") Integer status){
        log.info("店铺状态设置为：{}", status == 1? "营业中": "打烊中");
        redisUtil.set("SHOP_STATUS", status);
        return Result.success("设置成功");
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus(){
        Integer shopStatus = (Integer) redisUtil.get("SHOP_STATUS");
        log.info("获取到的店铺状态为：{}", shopStatus == 1? "营业中": "打烊中");
        return Result.success(shopStatus);
    }

}
