package com.github.sisi.domain;


import liquibase.pro.packaged.M;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_course")
@Data
@NoArgsConstructor
public class UserCourse {
    //用于新增relation 学生选课时
    public UserCourse(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName ="id")
    // the id in user is the user_id column in user_course
    @ManyToOne //多对一 one is the one id in user, many means many times in current table
    private User user;

    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne //多对一
    private Course course;
}
