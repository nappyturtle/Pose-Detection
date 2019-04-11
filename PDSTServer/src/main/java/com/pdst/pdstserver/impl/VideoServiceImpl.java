package com.pdst.pdstserver.impl;

import com.pdst.pdstserver.dto.VideoDTO;
import com.pdst.pdstserver.dto.VideoDTOFrontEnd;
import com.pdst.pdstserver.model.Enrollment;
import com.pdst.pdstserver.model.Video;
import com.pdst.pdstserver.repository.EnrollmentRepository;
import com.pdst.pdstserver.repository.VideoRepository;
import com.pdst.pdstserver.service.VideoService;
import com.pdst.pdstserver.utils.SendRequest;
import com.pdst.pdstserver.model.Account;
import com.pdst.pdstserver.model.Course;
import com.pdst.pdstserver.repository.AccountRepository;
import com.pdst.pdstserver.repository.CourseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Comparator.comparing;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public VideoServiceImpl(VideoRepository videoRepository, AccountRepository accountRepository,
                            CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.videoRepository = videoRepository;
        this.accountRepository = accountRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }


    @Override
    public List<Video> getAllVideos() {
        List<Video> listTemp = videoRepository.findAll();
        List<Video> videos = new ArrayList<>();
        for (Video video : listTemp) {
            Course course = courseRepository.getOne(video.getCourseId());
            video.setFolderName(course.getName());
            videos.add(video);
        }
        return videos;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các video free cho trang home
     * tao dataset
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllVideosOrderByDate(int page, int size) {
        System.out.println("page - size = " + page + " - " + size);

        List<Video> videos = videoRepository.getAllVideoByCourseFree
                (PageRequest.of(page, size, Sort.by("createdTime").descending()));
        List<VideoDTO> dtos = new ArrayList<>();

        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            //VuVG - 15/03/2019 - Chỉ lấy các video free
            //if(course.getPrice() == 0) {
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dto.setAccountId(account.getId());
            dtos.add(dto);
            //}
        }
        return dtos;
    }

    @Override
    public Optional<Video> getVideoById(int id) {
        return Optional.empty();
    }


    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để tạo video, kèm theo đó là gửi request đến service handler để tiến hành cắt video
     * tao dataset
     * - dùng cho mobile
     */
    @Override
    public boolean createVideo(Video video) {
        // lưu video vào db
        try {
            Video videoRequest = videoRepository.save(video);
            // gửi request đến service để cắt video
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        SendRequest sendRequest = new SendRequest();
                        sendRequest.sendRequestToCreateDataset(videoRequest);
                        System.out.println("da goi request to create data set");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            System.out.println("da insert xong");
            //return videoRequest;
            if (videoRequest == null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để edit video
     * - dùng cho mobile
     */
    @Override
    public boolean editVideo(Video video) {
        Video videoTemp = videoRepository.findVideoById(video.getId());
        if (video.getStatus().equals(videoTemp.getStatus())) {
            videoTemp.setTitle(video.getTitle());
            videoTemp.setThumnailUrl(video.getThumnailUrl());
            videoTemp.setUpdatedTime(video.getUpdatedTime());
        } else {
            videoTemp.setTitle(video.getTitle());
            videoTemp.setThumnailUrl(video.getThumnailUrl());
            videoTemp.setUpdatedTime(video.getUpdatedTime());
            videoTemp.setPrevStatus(videoTemp.getStatus());
            videoTemp.setStatus(video.getStatus());
        }
        Video videoRes = videoRepository.save(videoTemp);

        if (videoRes != null) {
            return true;
        }
        return false;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các video free cho màn hình trang home, list video kèm theo bên dưới
     * - lấy top 6 những bỏ thằng đang xem nên chỉ còn 5
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllVideosRelatedByCourseId(int courseId, int currentVideoId) {
        List<Video> listTemp = videoRepository.findTop6ByCourseIdAndStatusOrderByCreatedTimeDesc
                (courseId,"active");

        List<VideoDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).getId() == currentVideoId) {
                listTemp.remove(i);
            }
        }
        for (Video video : listTemp) {
            Course course = courseRepository.findCourseById(courseId);
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dto.setAccountId(account.getId());
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các video vs course đã mua và tổng price của enrollment >= price của
     * course hiện tại
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllBoughtVideosByCourseId(int page, int size, int traineeId, int courseId) {

        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(traineeId);
        Course course = courseRepository.findCourseById(courseId);
        Account trainer = accountRepository.findAccountById(course.getAccountId());
        List<VideoDTO> dtos = new ArrayList<>();
        int totalPriceEnrollment = 0;
        for (Enrollment enrollment : enrollmentList) {
            totalPriceEnrollment += enrollment.getPrice();
        }
        if (totalPriceEnrollment >= course.getPrice()) {
            List<Video> videoUpdated = videoRepository.
                    findAllByCourseIdAndStatus(PageRequest.of(page, size, Sort.by("createdTime").descending()), courseId,"active");
            for (Video video : videoUpdated) {
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(trainer.getUsername());
                dto.setImgUrl(trainer.getImgUrl());
                dtos.add(dto);
            }
        }

        return dtos;

    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các video để edit, dùng cho mobile
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllVideoByCourseIdToEdit(int courseId) {
        List<Video> videos = videoRepository.findAllByCourseIdAndStatus(courseId,"active");
        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các video theo courseId, kiểm tra trong bảng enrollment lấy ra
     * danh sách các enrollment của của accountId và courseId,
     * cộng price của từng thằng lại để kiểm tra
     * + ( nếu < price của courseId hiện tại => chưa mua thêm video trong course đó
     * =>lấy danh sách các video có ngày tạo nhỏ hơn ngày mua course(enrollment date) cuối cùng)
     * + ( nếu >= price của courseId hiện tại => đã mua thêm video trong course đó
     * =>lấy hết các video đó)
     * <p>
     * - lấy top 6 những bỏ thằng đang xem nên chỉ còn 5
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllBoughtVideoRelated(int traineeId, int courseId, int currentVideoId) {

        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(traineeId);
        Course course = courseRepository.findCourseById(courseId);
        Account trainer = accountRepository.findAccountById(course.getAccountId());
        List<VideoDTO> dtos = new ArrayList<>();
        String maxDate = enrollmentRepository.getMaxDateEnrollmentByAccountIdAndCourseId(courseId, traineeId);
        System.out.println("maxDate = " + maxDate);
        int totalPriceEnrollment = 0;
        for (Enrollment enrollment : enrollmentList) {
            totalPriceEnrollment += enrollment.getPrice();
        }
        if (totalPriceEnrollment < course.getPrice()) {

            List<Video> listTemp = videoRepository.findTop6ByCreatedTimeLessThanAndCourseIdOrderByCreatedTimeDesc(maxDate, courseId);
            for (int i = 0; i < listTemp.size(); i++) {
                if (listTemp.get(i).getId() == currentVideoId) {
                    listTemp.remove(i);
                }
            }
            for (Video video : listTemp) {
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(trainer.getUsername());
                dto.setImgUrl(trainer.getImgUrl());
                dto.setAccountId(trainer.getId());
                dtos.add(dto);
            }
        } else {
            List<Video> listTemp = videoRepository.findTop6ByCourseIdAndStatusOrderByCreatedTimeDesc
                    (courseId,"active");
            for (int i = 0; i < listTemp.size(); i++) {
                if (listTemp.get(i).getId() == currentVideoId) {
                    listTemp.remove(i);
                }
            }
            for (Video video : listTemp) {
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(trainer.getUsername());
                dto.setImgUrl(trainer.getImgUrl());
                dto.setAccountId(trainer.getId());
                dtos.add(dto);
            }
        }

        return dtos;
    }

    @Override
    public int countVideosByCourseId(int courseId) {
        return videoRepository.countVideoByCourseIdAndStatus(courseId,"active");
    }

    @Override
    public List<Video> getAllFreeVideosByAccount(int accountId) {
        int freeCourseId = -1;
        freeCourseId = courseRepository.getFreeCourseIdByAccountId(accountId);
        List<Video> list = new ArrayList<>();
        list = videoRepository.findAllByCourseIdAndStatusOrderByCreatedTimeDesc(freeCourseId,"active");
        return list;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách các video free và sort theo số lượt view
     * tao dataset
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllVideosByTopNumOfView(int page, int size) {
        System.out.println("page - size = " + page + " - " + size);
        List<Video> videos = videoRepository.getAllVideoByCourseFree
                (PageRequest.of(page, size, Sort.by("numOfView").descending()));

        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
//            Account account = accountRepository.findAccountById(video.getAccountId());
            //VuVG - 15/03/2019 - Chỉ lấy các video free
            //if (course.getPrice() == 0) {
            Account account = accountRepository.findAccountById(course.getAccountId());
            VideoDTO dto = new VideoDTO();
            dto.setVideo(video);
            dto.setUsername(account.getUsername());
            dto.setImgUrl(account.getImgUrl());
            dto.setAccountId(account.getId());
            dtos.add(dto);
            //}
        }
        return dtos;
    }

    @Override
    public List<VideoDTO> getAllFreeVideos() {
        List<Video> videos = videoRepository.findAll();
        List<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            if (course.getPrice() == 0) {
                Account account = accountRepository.findAccountById(course.getAccountId());
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(account.getUsername());
                dto.setImgUrl(account.getImgUrl());
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy danh sách video cho web
     * tao dataset
     * - dùng cho web
     */
    @Override
    public List<VideoDTOFrontEnd> getAllVideoByStaffOrAdmin() {
        List<Video> listTemp = videoRepository.findAll();
        List<VideoDTOFrontEnd> listVideoDTOFrontEnd = new ArrayList<>();

        int count = 0;
        for (Video video : listTemp) {
            Course course = courseRepository.findCourseById(video.getCourseId());
            VideoDTOFrontEnd videoDTOFrontEnd = new VideoDTOFrontEnd(count + 1, video.getId(), video.getTitle(), video.getNumOfView()
                    , course.getName());
            listVideoDTOFrontEnd.add(videoDTOFrontEnd);
            count++;
        }
        return listVideoDTOFrontEnd;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để edit video cho web
     * tao dataset
     * - dùng cho web
     */

    @Override
    public boolean editVideoStatusByStaffOrAdmin(VideoDTOFrontEnd dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Video video = videoRepository.findVideoById(dto.getId());
        //video.setTitle(dto.getTitle());
        //video.setNumOfView(dto.getNumOfView());
        video.setStatus(dto.getStatus());
        video.setTitle(dto.getTitle());
        video.setUpdatedTime(sdf.format(date));
        Video videRes = videoRepository.save(video);
        if (videRes != null) {
            return true;
        }
        return false;
    }

    @Override
    public int countAllVideos() {
        return videoRepository.countAllVideos();
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để lấy video detail cho web
     * tao dataset
     * - dùng cho web
     */
    @Override
    public VideoDTOFrontEnd getVideoDetailById(int videoId) {
        Video video = videoRepository.findVideoById(videoId);
        Course course = courseRepository.findCourseById(video.getCourseId());
        Account account = accountRepository.findAccountById(course.getAccountId());
        VideoDTOFrontEnd dto = new VideoDTOFrontEnd();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setThumnail(video.getThumnailUrl());
        dto.setContent(video.getContentUrl());
        dto.setNumOfView(video.getNumOfView());
        dto.setCoursename(course.getName());
        dto.setStatus(video.getStatus());
        if (account.getFullname() != null) {
            dto.setTrainerName(account.getFullname());
        } else {
            dto.setTrainerName(account.getUsername());
        }
        return dto;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để hiển thị những video mới trainer
     * - xét trong bảng enrollment lấy ra record có courseId và accountId(traineeId) đó, tính tổng price
     * (nếu price < price của course hiện tại thì trainee đó chưa mua thêm video của course đó
     * - lấy danh sách những video mới của trainer đó có ngày tạo nhỏ hơn ngày mua course cuối cùng(enrollment date)
     * <p>
     * - dùng cho mobile
     */
    @Override
    public List<VideoDTO> getAllUnBoughtVideoByCourseId(int page, int size, int traineeId, int courseId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByAccountId(traineeId);
        Course course = courseRepository.findCourseById(courseId);
        Account trainer = accountRepository.findAccountById(course.getAccountId());
        List<VideoDTO> dtos = new ArrayList<>();
        String maxDate = enrollmentRepository.getMaxDateEnrollmentByAccountIdAndCourseId(courseId, traineeId);
        System.out.println("maxDate = " + maxDate);
        int totalPriceEnrollment = 0;
        for (Enrollment enrollment : enrollmentList) {
            totalPriceEnrollment += enrollment.getPrice();
        }
        if (totalPriceEnrollment < course.getPrice()) {
            List<Video> videoUpdated = videoRepository.
                    findAllByCreatedTimeLessThanAndCourseId
                            (PageRequest.of(page, size, Sort.by("createdTime").descending()), maxDate, courseId);
            for (Video video : videoUpdated) {
                VideoDTO dto = new VideoDTO();
                dto.setVideo(video);
                dto.setUsername(trainer.getUsername());
                dto.setImgUrl(trainer.getImgUrl());
                dtos.add(dto);
            }
        }

        return dtos;
    }

    /**
     * @author KietPT
     * @since 6/4/2019
     * <p>
     * - hàm này dùng để tăng số lượt xem cho video
     * - dùng cho mobile
     */
    @Override
    public boolean changeNumberOfViewByVideoId(int videoId) {
        Video video = videoRepository.findVideoById(videoId);
        video.setNumOfView(video.getNumOfView() + 1);
        Video videoRes = videoRepository.save(video);
        if (videoRes != null) {
            return true;
        }
        return false;
    }
}
