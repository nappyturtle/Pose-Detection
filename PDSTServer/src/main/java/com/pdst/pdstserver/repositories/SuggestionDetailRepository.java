package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.SuggestionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionDetailRepository extends JpaRepository<SuggestionDetail, Integer> {
    SuggestionDetail findById(int suggestionDetailId);
    List<SuggestionDetail> findAllBySuggestionIdAndStatus(int suggestionId, String status);
}
