package com.group9.pdst;

import com.group9.pdst.service.FileStorage;
import com.group9.pdst.utils.ConstantUtilities;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;
import javax.annotation.Resource;
import java.io.*;

@SpringBootApplication
public class PdstApplication implements CommandLineRunner {

    @Resource
    FileStorage fileStorage;
    public static void main(String[] args) {
        getConfig();
        SpringApplication.run(PdstApplication.class, args);
    }
    private static void getConfig() {
        ConstantUtilities.jedis = new Jedis("localhost");
        BufferedReader br = null;
        FileReader fr = null;
        try {
            File configFile = ResourceUtils.getFile("classpath:static/config.txt");
            fr = new FileReader(configFile);
            br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
                int splitPosition = line.indexOf("=");
                String key = line.substring(0, splitPosition);
                String value = line.substring(splitPosition+1, line.length());
                switch (key) {
                    case "imgSize":
                        ConstantUtilities.imgSize = Integer.parseInt(value);
                        break;
                }
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        fileStorage.deleteAll();
        fileStorage.init();
        ConstantUtilities.jedis.flushAll();
    }

}

