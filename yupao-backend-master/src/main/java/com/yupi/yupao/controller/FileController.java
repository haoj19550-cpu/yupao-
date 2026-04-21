package com.yupi.yupao.controller;

import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    /**
     * 上传文件（头像等）
     *
     * @param multipartFile 文件
     * @return 文件访问地址
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return ResultUtils.<String>error(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }

        // 限制文件大小 5MB
        long maxFileSize = 5 * 1024 * 1024L;
        if (multipartFile.getSize() > maxFileSize) {
            return ResultUtils.<String>error(ErrorCode.PARAMS_ERROR, "文件大小不能超过 5MB");
        }

        // 校验文件类型（仅允许图片）
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResultUtils.<String>error(ErrorCode.PARAMS_ERROR, "只支持图片文件");
        }

        // 获取原始文件名和后缀
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 上传目录：项目根目录下的 uploads/ 目录
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            // 保存文件到本地
            File destFile = new File(uploadDir + fileName);
            multipartFile.transferTo(destFile);
            log.info("文件上传成功: {}", destFile.getAbsolutePath());
            // 返回可访问的 URL 路径（不含 /api，由前端 axios baseURL 自动拼接）
            return ResultUtils.success("/file/" + fileName);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResultUtils.<String>error(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
    }
}
