package com.github.sisi.service;

import com.github.sisi.domain.Course;
import com.github.sisi.domain.User;
import com.github.sisi.domain.UserCourse;
import com.github.sisi.repository.CourseRepository;
import com.github.sisi.repository.UserCourseRepository;
import com.github.sisi.repository.UserRepository;
import com.github.sisi.service.dto.CourseDTO;
import com.github.sisi.service.mapper.CourseMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private UserCourseRepository userCourseRepository;
    private CourseMapper courseMapper;
    public void enrollCourse(String userName, String courseName) {
        //1+2 check user & course exist
        UserCourse userCourse = getUserCourse(userName, courseName);
        //3. Check if UserCourse not exists -> save to DB
        Optional<UserCourse> optionalUserCourse = userCourseRepository.findOneByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
        optionalUserCourse.ifPresent(existingUserCourse -> {
            throw new IllegalArgumentException("UserCourse already exists: " + existingUserCourse.toString());
        });
        // 4. save new relationship (userCourse) into user_course table
        userCourseRepository.save(userCourse);
    }

    /**
     * Helper function to check:
     * 1. Check if User exists -> I want it exists
     * 2. Check if Course exists -> I want it exists
     * return such userCourse with User and Course
     */
    private UserCourse getUserCourse(String userName, String courseName) {
        //1. User exists?
        Optional<User> optionalUser = userRepository.findOneByLogin(userName);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));

        //2. Course exists?
        Optional<Course> optionalCourse = courseRepository.findOneByCourseName(courseName);
        Course course = optionalCourse.orElseThrow(() -> new IllegalArgumentException("No such course: " + courseName));

        //3. Return such UserCourse.
        return new UserCourse(user, course);

    }

    public List<CourseDTO> getAllCourses() {
        //TODO: implement get all courses logic
        List<Course> courses = courseRepository.findAll();
//        This is not suggested in real world
//        List<CourseDTO> courseDTOList = new ArrayList<>();
//        for (Course course: courses){
//            courseDTOList.add(courseMapper.convert(course));
//        }
        // Java 8 特性 stream
        return courses.stream().map(course -> courseMapper.convert(course)).collect(Collectors.toList());

    }

    public List<CourseDTO> getEnrolledCourses(String userName) {
        //1. User exists?
        Optional<User> optionalUser = userRepository.findOneByLogin(userName);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));
        //2. find all UserCourses by user
        List<UserCourse> userCourseList = userCourseRepository.findAllByUser(user);
        return userCourseList.stream()
            .map(userCourse -> userCourse.getCourse())
            .map(course -> courseMapper.convert(course))
            .collect(Collectors.toList());
    }

    public void dropCourse(String userName, String courseName) {
        //1+2 check user & course exist, current entity does not have ID
        UserCourse userCourse = getUserCourse(userName, courseName);
        //3. Delete such userCourse
        userCourseRepository.deleteByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
    }
}
