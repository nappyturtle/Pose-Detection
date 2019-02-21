package com.pdst.pdstserver.services.videoservice;

import com.pdst.pdstserver.dtos.VideoDTO;
import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Category;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.AccountRepository;
import com.pdst.pdstserver.repositories.CategoryRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    public VideoServiceImpl(VideoRepository videoRepository,AccountRepository accountRepository,
    CategoryRepository categoryRepository
    ) {
        this.videoRepository = videoRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;

    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public List<VideoDTO> getAllVideosOrderByDate() {

        List<Video> videoList = videoRepository.findAll(new Sort(Sort.Direction.DESC, "createdTime"));
        List<VideoDTO> listDTO = new ArrayList<>();
        for(int i = 0; i<videoList.size(); i++){
            Optional<Account> account = accountRepository.findById(videoList.get(i).getAccountId());
            Optional<Category> category = categoryRepository.findById(videoList.get(i).getCategoryId());
            VideoDTO dto = new VideoDTO();
            dto.setId(videoList.get(i).getId());
            dto.setTitle(videoList.get(i).getTitle());
            dto.setThumnailUrl(videoList.get(i).getThumnailUrl());
            dto.setContentUrl(videoList.get(i).getContentUrl());
            dto.setAccountId(videoList.get(i).getAccountId());
            dto.setCategoryId(videoList.get(i).getCategoryId());
            dto.setNumOfView(videoList.get(i).getNumOfView());
            dto.setStatus(videoList.get(i).getStatus());
            dto.setUsername(account.get().getUsername());
            dto.setCategoryName(category.get().getName());
            dto.setCreatedTime(videoList.get(i).getCreatedTime());
            dto.setImgUrl(account.get().getImgUrl());
            listDTO.add(dto);
        }
        return listDTO;
        //return videoRepository.findAll(new Sort(Sort.Direction.DESC, "createdTime"));
    }


    public Optional<Video> getVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    @Override
    public Video createVideo(Video video) {
        // lưu video vào db
        Video videoRequest = videoRepository.save(video);

        // gửi request đến service để cắt video
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    SendRequest sendRequest = new SendRequest();
                    sendRequest.sendRequestToCreateDataset(videoRequest);
                    System.out.println("da goi request to create data set");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        System.out.println("da insert xong");
        return videoRequest;


    }
}
