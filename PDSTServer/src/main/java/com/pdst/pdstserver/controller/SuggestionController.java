package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.dto.SuggestionDTO;
import com.pdst.pdstserver.dto.SuggestionDTOFrontEnd;
import com.pdst.pdstserver.service.SuggestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SuggestionController.BASE_URL)
public class SuggestionController {
    public static final String BASE_URL = "suggestion";

    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("suggestions")
    public List<SuggestionDTO> getAllSuggestions() {

        return suggestionService.getAllSuggestions();
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để lấy thông tin suggestion
     - dùng cho web
     */
    @GetMapping("getSuggestionById/{suggestionId}")
    public SuggestionDTOFrontEnd getSuggestionById(@PathVariable(value = "suggestionId") int suggestionId) {
        return suggestionService.getSuggestionById(suggestionId);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để lấy danh sách suggestion
     - dùng cho web
     */
    @GetMapping("getAllSuggestionByStaffOrAdmin")
    public List<SuggestionDTOFrontEnd> getAllSuggestionByStaffOrAdmin() {

        return suggestionService.getAllSuggestionByStaffOrAdmin();
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để edit status suggestion(active,inactive, processing)
     - dùng cho web
     */
    @PutMapping("editStatusSuggestionByStaffOrAdmin")
    public boolean editStatusSuggestionByStaffOrAdmin(@RequestBody SuggestionDTOFrontEnd dto) {

        return suggestionService.editStatusSuggestionByStaffOrAdmin(dto);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để tao suggestion khi trainee/trainer tập theo 1 video nào đó, gửi video đó
     - dùng cho mobile
     */
    @PostMapping(value = "/create")
    public ResponseEntity<Void> createSuggestion(@RequestBody SuggestionDTO suggestion) {
        boolean flag = suggestionService.createSuggestion(suggestion);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để lấy danh sách suggestion
     - dùng cho mobile
     */
    @GetMapping("getSuggestionListById")
    public List<SuggestionDTO> getSuggestionListById(@RequestParam(value = "page") int page,
                                                     @RequestParam(value = "size") int size,
                                                     @RequestParam(value = "id") int id) {

        return suggestionService.getSuggestionListById(page,size,id);
    }

    /**
     @author  KietPT
     @since   6/4/2019

     - hàm này dùng để lấy danh sách suggestion của trainee được quản lí bởi trainer thông qua course và video
     - dùng cho mobile
     */
    @GetMapping("getSuggestionByTrainer")
    public List<SuggestionDTO> getSuggestionByTrainer(@RequestParam(value = "page") int page,
                                                      @RequestParam(value = "size") int size,
                                                      @RequestParam(value = "trainerId") int trainerId,
                                                      @RequestParam(value = "traineeId") int traineeId) {

        return suggestionService.getSuggestionByTrainer(page,size,trainerId,traineeId);
    }
}
