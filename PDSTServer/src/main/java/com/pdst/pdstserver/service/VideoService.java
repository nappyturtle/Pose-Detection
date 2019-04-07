package com.pdst.pdstserver.service;

import com.pdst.pdstserver.dto.VideoDTO;
import com.pdst.pdstserver.dto.VideoDTOFrontEnd;
import com.pdst.pdstserver.model.Video;

import java.util.List;
import java.util.Optional;

public interface VideoService {
    List<Video> getAllVideos();
    List<VideoDTO> getAllVideosOrderByDate(int page, int size);
    List<VideoDTO> getAllVideosByTopNumOfView(int page,int size);
    Optional<Video> getVideoById(int id);
    boolean createVideo(Video video);
    boolean editVideo(Video video);
    List<VideoDTO> getAllVideosRelatedByCourseId(int courseId, int currentVideoId);
    List<VideoDTO> getAllBoughtVideosByCourseId(int page, int size, int traineeId, int courseId);
    List<VideoDTO> getAllVideoByCourseIdToEdit(int courseId);
    List<VideoDTO> getAllBoughtVideoRelated(int traineeId, int courseId, int videoId);
    int countVideosByCourseId(int courseId);
    List<Video>getAllFreeVideosByAccount(int accountId);
    List<VideoDTO> getAllFreeVideos();
    List<VideoDTOFrontEnd> getAllVideoByStaffOrAdmin();
    boolean editVideoStatusByStaffOrAdmin(VideoDTOFrontEnd dto);
    int countAllVideos();
    VideoDTOFrontEnd getVideoDetailById(int videoId);
    List<VideoDTO> getAllUnBoughtVideoByCourseId(int page, int size,int traineeId,int courseId);
    boolean changeNumberOfViewByVideoId(int videoId);
}
