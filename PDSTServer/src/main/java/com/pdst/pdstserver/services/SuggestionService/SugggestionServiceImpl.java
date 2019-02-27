package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.SuggestionRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SugggestionServiceImpl implements SugggestionService {
    private final SuggestionRepository suggestionRepository;
    private final VideoRepository videoRepository;

    public SugggestionServiceImpl(SuggestionRepository suggestionRepository, VideoRepository videoRepository) {
        this.suggestionRepository = suggestionRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public List<SuggestionDTO> getAllSuggestions() {
        List<Suggestion> list = suggestionRepository.findAll();
        List<SuggestionDTO> listDTO = new ArrayList<>();

        try {
            for (int i = 0; i < list.size(); i++) {
                Suggestion suggestion = list.get(i);
                System.out.println("id = " + suggestion.getId());

                Optional<Video> video = videoRepository.findById(suggestion.getVideoId());
                System.out.println("test = " + video.get().getThumnailUrl());
                SuggestionDTO dto = new SuggestionDTO();
                dto.setId(suggestion.getId());
                dto.setAccountId(suggestion.getAccountId());
                dto.setVideoName(video.get().getTitle());
                dto.setVideoId(suggestion.getVideoId());
                dto.setThumnailUrl(video.get().getThumnailUrl());
                dto.setCreatedTime(suggestion.getCreatedTime());
                listDTO.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDTO;
    }

    @Override
    public boolean createSuggestion(SuggestionDTO suggestion) {

        System.out.println("traine: = "+suggestion.getFoldernameTrainee());
        System.out.println("url = "+suggestion.getUrlVideoTrainee());
        Suggestion dbInserted = new Suggestion();
        dbInserted.setAccountId(suggestion.getAccountId());
        dbInserted.setVideoId(suggestion.getVideoId());
        dbInserted.setCreatedTime(suggestion.getCreatedTime());
        dbInserted.setStatus(suggestion.getStatus());
        dbInserted.setThumnailUrl(suggestion.getThumnailUrl());
        Suggestion savedSuggestion = suggestionRepository.save(dbInserted);
        System.out.println("videoId = "+savedSuggestion.getVideoId());
        Video videoRequest = videoRepository.findVideoById(savedSuggestion.getVideoId());



        // gửi request đến service để cắt video
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    SendRequest sendRequest = new SendRequest();
                    sendRequest.sendRequestToSuggest(videoRequest, suggestion.getFoldernameTrainee(),
                            savedSuggestion.getId(),suggestion.getUrlVideoTrainee());
                    System.out.println("da goi request to make suggestion");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        if (savedSuggestion == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<SuggestionDTO> getSuggestionByTrainee(int id) {
        List<Suggestion> list = suggestionRepository.findAllByAccountIdOrderByCreatedTimeDesc(id);
        List<SuggestionDTO> listDTO = new ArrayList<>();

        try {
            for (int i = 0; i < list.size(); i++) {
                Suggestion suggestion = list.get(i);
                System.out.println("id = " + suggestion.getId());

                Optional<Video> video = videoRepository.findById(suggestion.getVideoId());
                SuggestionDTO dto = new SuggestionDTO();
                dto.setId(suggestion.getId());
                dto.setAccountId(suggestion.getAccountId());
                dto.setVideoName(video.get().getTitle());
                dto.setVideoId(suggestion.getVideoId());
                dto.setThumnailUrl(suggestion.getThumnailUrl());
                dto.setCreatedTime(suggestion.getCreatedTime());
                dto.setStatus(suggestion.getStatus());
                listDTO.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDTO;
    }
}
