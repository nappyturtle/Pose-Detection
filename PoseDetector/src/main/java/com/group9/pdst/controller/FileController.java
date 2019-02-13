package com.group9.pdst.controller;

import com.group9.pdst.model.FileInfo;
import com.group9.pdst.service.FileStorage;
import com.group9.pdst.utils.StartBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private FileStorage fileStorageService;

//    @RequestMapping(value = "/sliceVideo", method = RequestMethod.POST)
//    public ResponseEntity<List<FileInfo>> sliceVideo(@RequestBody String jsonString) {
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(" da vao getVideoUrl Controller ");
//        List<FileInfo> list = null;
//        try {
//            FileInfo fileInfo = mapper.readValue(jsonString, FileInfo.class);
//            System.out.println("videoUrl = " + fileInfo.getVideoUrl());
//            System.out.println("videoName = " + fileInfo.getVideoName());
//            String[] videoNameTmp = fileInfo.getVideoName().split("/");
//            System.out.println(videoNameTmp[2]);
//            String folderName = fileInfo.getVideoName().substring(0, fileInfo.getVideoName().indexOf("/", 1));
//            System.out.println("folderName = " + folderName);
//            // tạo folder KietPT-123456 trong thư mục filestorage
//            fileStorageService.createFolder(folderName);
//
//            // cắt video vs 3 param
//            // + url
//            // + tên video
//            // + tên folder(KietPT-123456)
//            fileStorageService.slideImageToImageFrames(fileInfo.getVideoUrl(),videoNameTmp[2],folderName);
//
//            // lấy danh sách các file trong thư mục filestorage/KietPT-123456
//            list = fileStorageService.getFileFromLocalStorage(folderName);
//        } catch (IOException e) {
//            logger.info("Something Wrong in /sliceVideo !!!! " + e.getMessage());
//        }
//
//        return ResponseEntity.ok(list);
//    }


    @PostMapping(value = "/sliceVideo")
    public String sliceVideo(@RequestBody FileInfo fileInfo) {
        System.out.println("name = "+fileInfo.getFoldername());
        System.out.println("url = "+fileInfo.getVideoUrl());

        System.out.println(" da vao getVideoUrl Controller ");
        //List<FileInfo> list = null;
        try {


            // tạo folder KietPT-123456 trong thư mục filestorage
            fileStorageService.createFolder(fileInfo.getFoldername());

            // cắt video vs 3 param
            // + url
            // + tên video
            // + tên folder(KietPT-123456)
            fileStorageService.slideImageToImageFrames(fileInfo.getVideoUrl(),fileInfo.getFoldername());

            // lấy danh sách các file trong thư mục filestorage/KietPT-123456
            //list = fileStorageService.getFileFromLocalStorage(folderName);

            StartBrowser browser = new StartBrowser();
            browser.openBrowser(fileInfo.getFoldername());
        } catch (Exception e) {
            logger.info("Something Wrong in /sliceVideo !!!! " + e.getMessage());
        }


        return "Successfully";
    }

    @GetMapping(value = "/uploadImage")
    public ResponseEntity<List<FileInfo>> uploadImageToStorage(@RequestParam(value = "name") String folderName) {
        System.out.println("da vao upload image controller");
        System.out.println("folderName = "+folderName);
        List<FileInfo> list = fileStorageService.getFileFromLocalStorage(folderName);
        return ResponseEntity.ok(list);
    }


}
