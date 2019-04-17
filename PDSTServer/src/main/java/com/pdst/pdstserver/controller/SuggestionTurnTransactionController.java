package com.pdst.pdstserver.controller;

import com.pdst.pdstserver.service.SuggestionTurnTransactionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SuggestionTurnTransactionController.BASE_URL)
public class SuggestionTurnTransactionController {
    public static final String BASE_URL = "suggestionturn";
    private final SuggestionTurnTransactionService suggestionTurnTransactionService;

    public SuggestionTurnTransactionController(SuggestionTurnTransactionService suggestionTurnTransactionService) {
        this.suggestionTurnTransactionService = suggestionTurnTransactionService;
    }
}
