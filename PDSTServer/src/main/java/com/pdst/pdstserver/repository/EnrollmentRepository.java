package com.pdst.pdstserver.repository;

import com.pdst.pdstserver.model.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findAllByAccountId(Pageable pageable, int accountId);
    List<Enrollment> findAllByAccountId(int accountId);
    List<Enrollment> findAllByCourseId(int courseId);
    Integer countAllByCourseId(int courseId);

    @Query(value = "select MAX(en.createdTime) " +
            "from Enrollment en " +
            "where en.courseId = ?1 and en.accountId = ?2"
    )
    String getMaxDateEnrollmentByAccountIdAndCourseId(Integer courseId, Integer accountId);

    @Query(value = "select count(e) from Enrollment e where e.accountId = ?1 and e.courseId = ?2")
    Long checkEnrollmentExisted(Integer traineeId, Integer courseId);
}
