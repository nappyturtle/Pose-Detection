package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Suggestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {

    List<Suggestion> findAllByAccountId(Pageable pageable,int id);
}
