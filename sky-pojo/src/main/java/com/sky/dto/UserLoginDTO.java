package com.sky.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
@ApiModel(description = "微信登录时传递的数据模型")
public class UserLoginDTO implements Serializable {

    private String code;

}
