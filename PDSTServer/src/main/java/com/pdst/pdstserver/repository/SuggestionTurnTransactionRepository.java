package com.pdst.pdstserver.repository;

import com.pdst.pdstserver.model.SuggestionTurnTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionTurnTransactionRepository extends JpaRepository<SuggestionTurnTransaction, Integer> {
    List<SuggestionTurnTransaction> findAllByAccountIdEqualsAndVideoIdEquals(int accountId, int videoId);
}
