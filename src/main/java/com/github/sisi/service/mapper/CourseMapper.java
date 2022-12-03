package com.github.sisi.service.mapper;

import com.github.sisi.domain.Course;
import com.github.sisi.service.dto.CourseDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *  Convert entity(model) class Course to CourseDTO
 */
@Service
public class CourseMapper {
    public CourseDTO convert(Course course){
        return CourseDTO.builder()
            .courseName(course.getCourseName())
            .courseContent(course.getCourseContent())
            .courseLocation(course.getCourseLocation())
            .teacherId(course.getTeacherId())
            .build();
    }
}
