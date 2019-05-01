package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.model.SuggestionTurnTransaction;
import com.pdst.pdstserver.service.SuggestionService;
import com.pdst.pdstserver.service.SuggestionTurnTransactionService;
import com.pdst.pdstserver.utils.Constant;
import com.pdst.pdstserver.utils.TimeHelper;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SuggestionTurnTransactionController.BASE_URL)
public class SuggestionTurnTransactionController {
    public static final String BASE_URL = "suggestionturn";
    private final SuggestionTurnTransactionService suggestionTurnTransactionService;
    private final SuggestionService suggestionService;

    public SuggestionTurnTransactionController(SuggestionTurnTransactionService suggestionTurnTransactionService,
                                               SuggestionService suggestionService) {
        this.suggestionTurnTransactionService = suggestionTurnTransactionService;
        this.suggestionService = suggestionService;
    }

    @GetMapping("isAvailableTurn")
    public boolean isAvailableTurn(@RequestParam(value = "accountId") int accountId, @RequestParam(value = "videoId") int videoId) {
        List<SuggestionTurnTransaction> suggestionTurnTransactions = suggestionTurnTransactionService.getSuggestionTurnByAccountIdAndVideoId(accountId, videoId);
        if (suggestionTurnTransactions.size() == 0) {
            SuggestionTurnTransaction freeSuggestionTurn = new SuggestionTurnTransaction();
            freeSuggestionTurn.setAccountId(accountId);
            freeSuggestionTurn.setPrice(0);
            freeSuggestionTurn.setVideoId(videoId);
            freeSuggestionTurn.setCreatedTime(TimeHelper.getCurrentTime());
            freeSuggestionTurn.setSuggestionTurn(Constant.FREE_SUGGESTION_TURN_FOR_TRAINEE);
            SuggestionTurnTransaction newFreeSuggestionTurn = suggestionTurnTransactionService.createFreeSuggestionTurnTransactionByAccountIdAndVideoId(freeSuggestionTurn);
            if (newFreeSuggestionTurn != null) {
                return true;
            } else {
                return false;
            }
        } else {
            int totalAvailableTurn = 0;
            for (SuggestionTurnTransaction suggestionTurnTransaction : suggestionTurnTransactions) {
                totalAvailableTurn += suggestionTurnTransaction.getSuggestionTurn();
            }
            int usedTurn = suggestionService.countAllSuggestionByAccountIdAndVideoId(accountId, videoId);
            if (usedTurn < totalAvailableTurn) {
                return true;
            } else {
                return false;
            }
        }
    }

    @PostMapping("buySuggestionTurn")
    public SuggestionTurnTransaction buySuggestionTurn(@RequestBody SuggestionTurnTransaction suggestionTurnTransaction){
        return suggestionTurnTransactionService.createNewTransaction(suggestionTurnTransaction);
    }
}
