package com.banana.visual.service;

import com.upyun.UpException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {

    /**
     * 1.上传文件到又拍云
     *
     * @param file
     * @param fileSuffix
     * @return
     */
    String uploadUpYun(MultipartFile file, String fileSuffix) throws IOException, UpException;
}
