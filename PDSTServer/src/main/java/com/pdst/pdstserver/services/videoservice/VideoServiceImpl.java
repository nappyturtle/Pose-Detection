package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public List<Video> getAllVideosOrderByDate() {
        return videoRepository.findAll(new Sort(Sort.Direction.DESC,"createdTime"));
    }


    public Optional<Video> getVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Video createVideo(Video video) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Video videoRequest = videoRepository.save(video);
        entityManager.getTransaction().begin();
        entityManager.persist(videoRequest);
        entityManager.getTransaction().commit();
//        try {
//            System.out.println("da vao de gui url");
//            URL url = new URL("http://localhost:8090/sliceVideo");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            con.setRequestProperty("Accept", "application/json");
//            con.setRequestMethod("POST");
//
//            JSONObject fileInfo = new JSONObject();
//
//            fileInfo.put("videoName","KietPT-456789/Video/1.mp4");
//            fileInfo.put("videoUrl", videoRequest.getContentUrl());
//
//
//            OutputStream os = con.getOutputStream();
//            os.write(fileInfo.toString().getBytes("UTF-8"));
//            //os.close();
//
//            StringBuilder sb = new StringBuilder();
//            int HttpResult = con.getResponseCode();
//            if (HttpResult == HttpURLConnection.HTTP_OK) {
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(con.getInputStream(), "utf-8"));
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                br.close();
//                System.out.println("" + sb.toString());
//            } else {
//                System.out.println(con.getResponseMessage());
//            }
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return videoRequest;


    }
}
