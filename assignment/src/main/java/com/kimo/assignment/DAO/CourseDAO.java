package com.kimo.assignment.DAO;

import com.kimo.assignment.Entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseDAO extends MongoRepository<Course,String> {

}
