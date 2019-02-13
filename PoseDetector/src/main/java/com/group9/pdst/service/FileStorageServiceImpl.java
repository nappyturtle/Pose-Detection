package com.group9.pdst.service;


import com.group9.pdst.handler.VideoFrameExtracter;
import com.group9.pdst.model.FileInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.jcodec.api.JCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorage{

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Path fileStorageLocation = Paths.get("filestorage");

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

//    @Autowired
//    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
//        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
//                .toAbsolutePath().normalize();
//
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            logger.info("Could not create the directory where the uploaded files will be stored." + ex.getMessage());
//
//        }
//    }


    @Override
    public List<FileInfo> getFileFromLocalStorage(String folderName){
        Path path = Paths.get(fileStorageLocation+"/"+folderName);
        File dir = new File(String.valueOf(path.toFile()));
        List<FileInfo> list = new ArrayList<>();

        if (dir.isDirectory()) { // make sure it's a directory
            for (File f : dir.listFiles(IMAGE_FILTER)) {
                try {
                    String fileString = encodeFileToBase64Binary(f);
                    System.out.println("image: " + f.getName());
                    System.out.println(" size  : " + f.length());
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFilename(f.getName().replace(".png",""));
                    fileInfo.setContent(fileString);
                    fileInfo.setFoldername(folderName);
                    list.add(fileInfo);
                } catch (Exception ex) {
                    logger.info("File "+ f.getName() +"not found !!!!! " + ex.getMessage());
                }
            }
        }

        return list;
    }


    @Override
    public void deleteAll() {

        if(fileStorageLocation.toFile().exists()){
            if(FileSystemUtils.deleteRecursively(fileStorageLocation.toFile())){
                System.out.println("xoa thanh cong");
            }else{
                System.out.println("Xoa khong thanh cong");
            }
        }else{
            System.out.println("Problem occurs when deleting the directory : " + fileStorageLocation);
        }

    }

    @Override
    public void createFolder(String folderName) {
        try {
            if(folderName != null){
                System.out.println("Tesssss = "+folderName);
                Path dir = Paths.get(fileStorageLocation  + "/"+folderName);
                Files.createDirectory(dir);
            }
        } catch (IOException e) {
            logger.info("Could not create the directory where the uploaded files will be stored." + e.getMessage());
        }
        System.out.println("da tao thu muc");
    }


    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            logger.info("FileNotFoundException in Base64Binary !!!! "+e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.info("IOException in Base64Binary !!!! "+e.getMessage());
        }

        return encodedfile;
    }

    @Override
    public void slideImageToCreateDataset(String urlVideo,String folderName) {
        VideoFrameExtracter videoFrameExtracter = new VideoFrameExtracter();

        System.out.println("da vao slideImage Hanlder = " + urlVideo);
        try {

            URL url = new URL(urlVideo);
            File file = new File(fileStorageLocation+"/"+ folderName + "\\"+"test.mp4");
            FileUtils.copyURLToFile(url, file);
            File imageFrame = videoFrameExtracter.createImageToCreateDataset(file, Paths.get(fileStorageLocation + "/" + folderName));
            System.out.println("input file name : " + file.getAbsolutePath());
            System.out.println("output video frame file name  : " + imageFrame.getAbsolutePath());
        } catch (JCodecException e) {
            System.out.println("JCodec error occurred while extracting image : " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException error occurred while extracting image : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error occurred while extracting image : " + e.getMessage());
        }
    }

    @Override
    public void slideImageToSuggest(String urlVideo,String folderName) {
        VideoFrameExtracter videoFrameExtracter = new VideoFrameExtracter();

        System.out.println("da vao slideImage Hanlder = " + urlVideo);
        try {

            URL url = new URL(urlVideo);
            File file = new File(fileStorageLocation+"/"+ folderName + "\\"+"test.mp4");
            FileUtils.copyURLToFile(url, file);
            File imageFrame = videoFrameExtracter.createImageToSuggest(file, Paths.get(fileStorageLocation + "/" + folderName));
            System.out.println("input file name : " + file.getAbsolutePath());
            System.out.println("output video frame file name  : " + imageFrame.getAbsolutePath());
        } catch (JCodecException e) {
            System.out.println("JCodec error occurred while extracting image : " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException error occurred while extracting image : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error occurred while extracting image : " + e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(fileStorageLocation);
        } catch (IOException e) {
            logger.info("Could not create the directory where the uploaded files will be stored." + e.getMessage());
        }
        System.out.println("da tao thu muc");
    }


}
