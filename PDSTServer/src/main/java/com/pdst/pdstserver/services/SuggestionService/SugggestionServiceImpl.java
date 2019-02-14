package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.repositories.SuggestionRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
