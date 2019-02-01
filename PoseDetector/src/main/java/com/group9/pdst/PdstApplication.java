package com.group9.pdst;

import com.group9.pdst.utils.ConstantUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;

@SpringBootApplication
public class PdstApplication {

    public static void main(String[] args) {
        getConfig();
        SpringApplication.run(PdstApplication.class, args);
    }
    private static void getConfig() {
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
                System.out.println("Value: " + value);
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

}

