package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

    //@Query("SELECT v.folder_name FROM Video v WHERE v.id = ?1")
    //String findFoldernameById(Integer id);

    Video findVideoById(Integer id);
}
