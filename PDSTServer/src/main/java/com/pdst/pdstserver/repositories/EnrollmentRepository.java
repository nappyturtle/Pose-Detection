package com.pdst.pdstserver.repositories;

import com.pdst.pdstserver.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
