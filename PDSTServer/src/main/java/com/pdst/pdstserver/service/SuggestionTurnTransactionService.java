package com.pdst.pdstserver.service;


import com.pdst.pdstserver.model.SuggestionTurnTransaction;

import java.util.List;

public interface SuggestionTurnTransactionService {
    List<SuggestionTurnTransaction> getSuggestionTurnByAccountIdAndVideoId(int accountId, int videoId);
    SuggestionTurnTransaction createFreeSuggestionTurnTransactionByAccountIdAndVideoId(SuggestionTurnTransaction suggestionTurnTransaction);
    SuggestionTurnTransaction createNewTransaction(SuggestionTurnTransaction suggestionTurnTransaction);
}
