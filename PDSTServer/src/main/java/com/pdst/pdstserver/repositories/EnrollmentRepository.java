package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findAllByAccountId(Pageable pageable, int accountId);
    List<Enrollment> findAllByAccountId(int accountId);
    Integer countAllByCourseId(int courseId);
}
