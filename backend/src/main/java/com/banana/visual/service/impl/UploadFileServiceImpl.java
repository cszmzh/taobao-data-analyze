package com.banana.visual.service.impl;

import com.banana.visual.config.UpyunConfig;
import com.banana.visual.service.UploadFileService;
import com.upyun.RestManager;
import com.upyun.UpException;
import com.upyun.UpYunUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public String uploadUpYun(MultipartFile multipartFile, String fileSuffix) throws IOException, UpException {

        // 初始化
        RestManager manager = new RestManager(UpyunConfig.BUCKET_NAME, UpyunConfig.OPERATOR, UpyunConfig.PASSWORD);
        manager.setTimeout(60);
        manager.setApiDomain(RestManager.ED_AUTO);

        // 图片名称
        String fileName = UUID.randomUUID() + fileSuffix;
        // 完整上传路径
        String filePath = UpyunConfig.FILE_URL + fileName;

        // MultipartFile 转 File
        File file = new File(UpyunConfig.LOCAL_TEMP_STATIC_PATH + "TEMP_FILE");
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);

        // 指定参数
        Map<String, String> params = new HashMap<>();
        params.put(RestManager.PARAMS.CONTENT_MD5.getValue(), UpYunUtils.md5(file, 1024));

        // 上传图片
        manager.writeFile(filePath, file, params);

        // 清除临时文件
        if (file.exists()) {
            file.delete();
        }

        return UpyunConfig.DOMAIN_URL + filePath.substring(1);
    }
}
