package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.VideoTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideoTransactionRepository extends JpaRepository<VideoTransaction, Integer> {
}
