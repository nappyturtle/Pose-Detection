package com.group9.pdst.controller;

import com.group9.pdst.model.FileInfo;
import com.group9.pdst.service.FileStorage;
import com.group9.pdst.utils.ConstantUtilities;
import com.group9.pdst.utils.OpenBrowserUtilities;
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


    @PostMapping(value = "/sliceVideo")
    public String sliceVideo(@RequestBody FileInfo fileInfo) {
        System.out.println("name = "+fileInfo.getFoldernameTrainer());
        System.out.println("url = "+fileInfo.getVideoUrl());

        System.out.println(" da vao getVideoUrl Controller ");
        //List<FileInfo> list = null;
        try {


            // tạo folder KietPT-123456 trong thư mục filestorage
            fileStorageService.createFolder(fileInfo.getFoldernameTrainer());

            // cắt video vs 3 param
            // + url
            // + tên video
            // + tên folder(KietPT-123456)
            fileStorageService.slideImageToCreateDataset(fileInfo.getVideoUrl(),fileInfo.getFoldernameTrainer());

            // lấy danh sách các file trong thư mục filestorage/KietPT-123456
            //list = fileStorageService.getFileFromLocalStorage(folderName);
            OpenBrowserUtilities.openBrowser(ConstantUtilities.domain + "uploadImage.html?name=" + fileInfo.getFoldernameTrainer());
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

    @PostMapping(value = "/sliceVideoToSuggest")
    public String sliceVideoToSuggest(@RequestBody FileInfo fileInfo) {
        System.out.println("trainer = "+fileInfo.getFoldernameTrainer());
        System.out.println("trainee = "+fileInfo.getFoldernameTrainee());
        System.out.println("suggestionId = "+fileInfo.getSuggestionId());
        System.out.println("url = "+fileInfo.getVideoUrl());

        System.out.println(" da vao getVideoUrl Controller ");
        //List<FileInfo> list = null;
        try {


            // tạo folder KietPT-123456 trong thư mục filestorage
            fileStorageService.createFolder(fileInfo.getFoldernameTrainee());

            // cắt video vs 3 param
            // + url
            // + tên video
            // + tên folder(KietPT-123456)
            fileStorageService.slideImageToSuggest(fileInfo.getVideoUrl(),fileInfo.getFoldernameTrainee());

            // lấy danh sách các file trong thư mục filestorage/KietPT-123456
            //list = fileStorageService.getFileFromLocalStorage(folderName);
            OpenBrowserUtilities.openBrowser(ConstantUtilities.domain + "uploadImageToSuggest.html?trainer=" +
                    fileInfo.getFoldernameTrainer()+"&trainee="+fileInfo.getFoldernameTrainee()
            +"&suggestionId="+fileInfo.getSuggestionId());
        } catch (Exception e) {
            logger.info("Something Wrong in /sliceVideoToSuggest !!!! " + e.getMessage());
        }
        return "Successfully";
    }


}
