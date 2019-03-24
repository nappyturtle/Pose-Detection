package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.dtos.SuggestionDTOFrontEnd;
import com.pdst.pdstserver.handlers.SendRequest;
import com.pdst.pdstserver.models.Account;
import com.pdst.pdstserver.models.Course;
import com.pdst.pdstserver.models.Suggestion;
import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.models.Video;
import com.pdst.pdstserver.repositories.AccountRepository;
import com.pdst.pdstserver.repositories.CourseRepository;
import com.pdst.pdstserver.repositories.SuggestionRepository;
import com.pdst.pdstserver.repositories.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SuggestionServiceImpl implements SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final VideoRepository videoRepository;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, VideoRepository videoRepository
    ,AccountRepository accountRepository,CourseRepository courseRepository) {
        this.suggestionRepository = suggestionRepository;
        this.videoRepository = videoRepository;
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
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
                dto.setStatus(suggestion.getStatus());
                listDTO.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDTO;
    }

    @Override
    public boolean updateSuggestionStatus(int id, String status) {
        Suggestion suggestion = suggestionRepository.getOne(id);
        suggestion.setStatus(status);
        suggestionRepository.saveAndFlush(suggestion);
        return true;
    }

    @Override
    public List<SuggestionDTO> getSuggestionByTrainer(int page, int size, int trainerId, int traineeId) {
        System.out.println("page - size : "+page + " - "+size);
        List<Suggestion> list = suggestionRepository.findAllByAccountId(new PageRequest(page,size,Sort.Direction.DESC,"createdTime"),traineeId);
        List<SuggestionDTO> listDTO = new ArrayList<>();

        try {
            for (int i = 0; i < list.size(); i++) {
                Suggestion suggestion = list.get(i);
                System.out.println("id = " + suggestion.getId());

                Video video = videoRepository.findVideoById(suggestion.getVideoId());
                Course course = courseRepository.findCourseById(video.getCourseId());
                Account account = accountRepository.findAccountById(course.getAccountId());
                if(account.getId() == trainerId) {
                    SuggestionDTO dto = new SuggestionDTO();
                    dto.setId(suggestion.getId());
                    dto.setAccountId(suggestion.getAccountId());
                    dto.setVideoName(video.getTitle());
                    dto.setVideoId(suggestion.getVideoId());
                    dto.setThumnailUrl(suggestion.getThumnailUrl());
                    dto.setCreatedTime(suggestion.getCreatedTime());
                    dto.setStatus(suggestion.getStatus());
                    dto.setUrlVideoTrainee(suggestion.getTraineeVideo());
                    listDTO.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDTO;
    }

    @Override
    public List<SuggestionDTOFrontEnd> getAllSuggestionByStaffOrAdmin() {
        List<Suggestion> suggestions = suggestionRepository.findAll();
        List<SuggestionDTOFrontEnd> suggestionDTOFrontEnds = new ArrayList<>();
        for(Suggestion su : suggestions){
            SuggestionDTOFrontEnd suggestionDTOFrontEnd = new SuggestionDTOFrontEnd();
            Account account = accountRepository.findAccountById(su.getAccountId());
            Video video = videoRepository.findVideoById(su.getVideoId());
            suggestionDTOFrontEnd.setId(su.getId());
            suggestionDTOFrontEnd.setVideoname(video.getTitle());
            suggestionDTOFrontEnd.setAccountname(account.getUsername());
            suggestionDTOFrontEnd.setThumnailUrl(su.getThumnailUrl());
            suggestionDTOFrontEnd.setStatus(su.getStatus());
            suggestionDTOFrontEnds.add(suggestionDTOFrontEnd);
        }
        return suggestionDTOFrontEnds;
    }

    @Override
    public boolean editStatusSuggestionByStaffOrAdmin(SuggestionDTOFrontEnd dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Suggestion suggestion = suggestionRepository.getOne(dto.getId());
        suggestion.setStatus(dto.getStatus());
        suggestion.setUpdatedTime(sdf.format(date));
        Suggestion suggestionRes = suggestionRepository.save(suggestion);
        if(suggestionRes != null){
            return true;
        }
        return false;
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
        dbInserted.setTraineeVideo(suggestion.getUrlVideoTrainee());
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
    public List<SuggestionDTO> getSuggestionByTrainee(int page, int size, int id) {
        System.out.println("page - size : "+page + " - "+size);
        List<Suggestion> list = suggestionRepository.findAllByAccountId(new PageRequest(page,size,Sort.Direction.DESC,"createdTime"),id);
        System.out.println("list = "+list.isEmpty());
//
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
                dto.setUrlVideoTrainee(suggestion.getTraineeVideo());
                listDTO.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDTO;
    }
}
