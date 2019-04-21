package com.pdst.pdstserver.impl;

import com.pdst.pdstserver.model.SuggestionTurnTransaction;
import com.pdst.pdstserver.repository.SuggestionTurnTransactionRepository;
import com.pdst.pdstserver.service.SuggestionTurnTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionTurnTransactionServiceImpl implements SuggestionTurnTransactionService {
    private final SuggestionTurnTransactionRepository suggestionTurnTransactionRepository;

    public SuggestionTurnTransactionServiceImpl(SuggestionTurnTransactionRepository suggestionTurnTransactionRepository) {
        this.suggestionTurnTransactionRepository = suggestionTurnTransactionRepository;
    }

    @Override
    public List<SuggestionTurnTransaction> getSuggestionTurnByAccountIdAndVideoId(int accountId, int videoId) {
        return suggestionTurnTransactionRepository.findAllByAccountIdEqualsAndVideoIdEquals(accountId, videoId);
    }

    @Override
    public SuggestionTurnTransaction createFreeSuggestionTurnTransactionByAccountIdAndVideoId(SuggestionTurnTransaction suggestionTurnTransaction) {
        return suggestionTurnTransactionRepository.save(suggestionTurnTransaction);
    }

    @Override
    public SuggestionTurnTransaction createNewTransaction(SuggestionTurnTransaction suggestionTurnTransaction) {
        return suggestionTurnTransactionRepository.save(suggestionTurnTransaction);
    }
}
