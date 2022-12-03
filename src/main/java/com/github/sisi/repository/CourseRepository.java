package com.github.sisi.repository;

import com.github.sisi.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course/*ORM model class*/, Long/*primary key type*/> {

    Optional<Course> findOneByCourseName(String courseName);
}
