package com.sky.controller.admin;

import com.github.pagehelper.PageInfo;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.utils.RedisUtil;
import com.sky.vo.DishVO;
import com.sky.entity.Dish;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    private final RedisUtil redisUtil;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 清理redis中的缓存数据
        String redisKey = "dish_" + dishDTO.getCategoryId();
        redisUtil.delete(redisKey);

        return Result.success("菜品添加成功");
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        //PageInfo<DishVO> pageInfo = dishService.pageQuery(dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品信息")
    public Result deleteBatch(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);

        // 删除 redis 中所有的菜品缓存数据清理掉
        redisUtil.cleanCache("dish_*");

        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息")
    public Result<DishVO> getById(@PathVariable("id") Long id){
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.updateWithFlavors(dishDTO);

        // 删除 redis 中所有的菜品缓存数据清理掉
        redisUtil.cleanCache("dish_*");

        return Result.success("修改成功");
    }

    @PostMapping("/status/{status}")
    @ApiOperation("起售停售菜品信息")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("起售停售菜品信息：{}", status);

        dishService.startOrStop(status, id);

        // 删除 redis 中所有的菜品缓存数据清理掉
        redisUtil.cleanCache("dish_*");

        return Result.success("修改成功");
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(@RequestParam("categoryId") Long categoryId){
        log.info("分类id: {}", categoryId);

        List<Dish> dishList = dishService.listByCategoryId(categoryId);
        return Result.success(dishList);
    }

}
