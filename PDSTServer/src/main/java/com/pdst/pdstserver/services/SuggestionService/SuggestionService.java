package com.pdst.pdstserver.services.SuggestionService;

import com.pdst.pdstserver.dtos.SuggestionDTO;
import com.pdst.pdstserver.dtos.SuggestionDTOFrontEnd;

import java.util.List;

public interface SuggestionService {
    List<SuggestionDTO> getAllSuggestions();
    boolean createSuggestion(SuggestionDTO suggestion);
	List<SuggestionDTO> getSuggestionByTrainee(int page, int size, int id);
	boolean updateSuggestionStatus(int id, String status);
    List<SuggestionDTO> getSuggestionByTrainer(int page, int size, int trainerId, int traineeId);
    List<SuggestionDTOFrontEnd> getAllSuggestionByStaffOrAdmin();
    boolean editStatusSuggestionByStaffOrAdmin(int id, String status);
}
