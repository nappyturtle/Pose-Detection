package com.pdst.pdstserver.repository;

import com.pdst.pdstserver.model.Suggestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {

    List<Suggestion> findAllByAccountId(Pageable pageable,int id);
    Suggestion findSuggestionById(int suggestionId);
    List<Suggestion> findAllByOrderByCreatedTimeDesc();
    int countAllByAccountIdAndVideoId(int accountId, int videoId);
}
