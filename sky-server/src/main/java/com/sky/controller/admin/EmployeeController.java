package com.sky.controller.admin;

import com.github.pagehelper.PageInfo;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation("新增员工")
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO){
        log.info("接收到的员工信息：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success("员工新增成功");
    }

    @ApiOperation("员工信息分页查询")
    @GetMapping("/selectPage")
    public Result<PageInfo<Employee>> selectPage(EmployeePageQueryDTO employeePageQueryDTO){
        PageInfo<Employee> employeePageInfo = employeeService.selectPage(employeePageQueryDTO);
        return Result.success(employeePageInfo);
    }

    @ApiOperation("启用，禁用员工状态")
    @PostMapping("/status/{status}")
    public Result startAndStop(@PathVariable("status") Integer status,
                               @RequestParam("id") Long id){
        log.info("参数：{}, {}", status, id);
        employeeService.startAndStop(status, id);
        return Result.success("修改成功");
    }

    @ApiOperation("根据id查询员工信息")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @ApiOperation("修改员工信息")
    @PutMapping
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("参数：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success("修改成功");
    }

}
