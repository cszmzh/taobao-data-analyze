package com.banana.visual.controller;

import com.banana.visual.VO.ResultVO;
import com.banana.visual.service.UploadFileService;
import com.banana.visual.utils.Base64DecodedMultipartFile;
import com.upyun.UpException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 上传文件控制层
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/16
 */
@RestController
@RequestMapping("/upload")
public class UploadFileController {

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping("/base64Img")
    public ResultVO uploadUpYun(@RequestParam("file") String base64) throws IOException, UpException {
        String fileURL = uploadFileService.uploadUpYun(Base64DecodedMultipartFile.base64ToMultipart(base64), ".png");
        return new ResultVO(HttpStatus.SC_OK, "success", fileURL);
    }
}
