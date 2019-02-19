package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.models.SuggestionTest;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.SuggestionRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<SuggestionDTO> listTemp = new ArrayList<>();
        String videoThumnail = null;
        try {
            for (int i = 0; i < list.size(); i++) {
                Suggestion suggestion = list.get(i);
                System.out.println("id = " + suggestion.getId());

                videoThumnail = videoRepository.findById(suggestion.getVideoId()).get().getThumnailUrl();
                System.out.println("test = " + videoThumnail);
                SuggestionDTO tmp = new SuggestionDTO();
                tmp.setId(suggestion.getId());
                tmp.setAccountId(suggestion.getAccountId());
                tmp.setName(suggestion.getName());
                tmp.setVideoId(suggestion.getVideoId());
                tmp.setThumnailUrl(videoThumnail);
                tmp.setCreatedTime(suggestion.getCreatedTime());
                listTemp.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTemp;
    }

    @Override
    public boolean createSuggestion(SuggestionTest suggestion) {


        Suggestion dbInserted = new Suggestion();
        dbInserted.setAccountId(suggestion.getAccountId());
        dbInserted.setName(suggestion.getName());
        dbInserted.setVideoId(suggestion.getVideoId());
        dbInserted.setCreatedTime(suggestion.getCreatedTime());

        Suggestion savedSuggestion = suggestionRepository.save(dbInserted);
        Video trainerVideo = videoRepository.findVideoById(savedSuggestion.getVideoId());

        System.out.println(suggestion.getThumnailUrl());
        // gửi request đến service để cắt video
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    SendRequest sendRequest = new SendRequest();
                    sendRequest.sendRequestToSuggest(trainerVideo, savedSuggestion.getName(),
                            savedSuggestion.getId(),suggestion.getThumnailUrl());
                    System.out.println("da goi request to create suggestion");
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
}
