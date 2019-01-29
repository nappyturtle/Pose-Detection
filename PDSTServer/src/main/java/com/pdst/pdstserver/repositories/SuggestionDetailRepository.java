package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.SuggestionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionDetailRepository extends JpaRepository<SuggestionDetail, Integer> {
}
