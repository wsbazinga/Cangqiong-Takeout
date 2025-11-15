package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@Api(tags = "公共controller")
@RequestMapping("/admin/common")
@RequiredArgsConstructor
public class CommonController {

    private final AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传接口")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}", file);

        // 原始文件后缀名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用uuid创建新的文件名，拼接后缀名
        String newFileName = UUID.randomUUID().toString() + extension;

        // 文件上传
        try {
            String fileUrl = aliOssUtil.upload(file.getBytes(), newFileName);
            return Result.success(fileUrl);
        } catch (IOException e) {
            log.info("文件上传失败", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

}
