package com.kimo.assignment.Service;

import com.kimo.assignment.DAO.CourseDAO;
import com.kimo.assignment.Entity.Chapters;
import com.kimo.assignment.Entity.Course;
import com.kimo.assignment.Exception.NotFound;
import com.kimo.assignment.Exception.RequestParamNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    CourseDAO courseDAO;

    @Value("${course-file}")
    String courseFile;

    public Object getAllCourses(int page, String options) {



        int pageSize = 20; // The desired page size

        Pageable pageable = PageRequest.of(page, pageSize);
        Query query = new Query();
        Aggregation aggregation = null;
        switch (options){
            case "date":
                query.with(Sort.by(Sort.Direction.DESC, "date")).with(pageable);
                break;
            case "Alphabetical":
                query.with(Sort.by(Sort.Direction.ASC, "name")).with(pageable);
                break;
            case "rating": aggregation = Aggregation.newAggregation(
                        Aggregation.unwind("chapters"),
                        Aggregation.group("_id")
                                .sum("chapters.rating").as("totalRating")
                                .first("name").as("name")
                                .first("date").as("date")
                                .first("description").as("description")
                                .first("domain").as("domain")
                                .push("chapters").as("chapters"),
                    Aggregation.sort(Sort.Direction.DESC, "totalRating")
                );

                break;
            default:
                throw new RequestParamNotValidException("Not a valid options - (developer note : check the getAllOptions api to get the options)");
        }
        List<Course> courseList;
        if(options.equals("rating")){
            courseList = mongoTemplate.aggregate(aggregation, "course", Course.class).getMappedResults();
        } else {
            courseList = mongoTemplate.find(query,Course.class);
        }
        if(!courseList.isEmpty()){
            return courseList;
        } else {
            throw new NotFound("No Data");
        }

    }


    public Object getChapterInfo(String id, String chapterName) {
        Optional<Course> optionalCourse = courseDAO.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Optional<Chapters> optionalChapter = course.getChapters().stream()
                    .filter(chapter -> chapter.getName().equals(chapterName))
                    .findFirst();
            if (optionalChapter.isPresent()) {
                return optionalChapter.get();
            } else {
                throw new NotFound("Given chapterName not having data");
            }
        } else {
            throw new NotFound("Given Course Id not having data");
        }
    }

    public Object getCourseOverView(String id) {
        Optional<Course> optionalCourse = courseDAO.findById(id);
        if(optionalCourse.isPresent()){
            return optionalCourse.get();
        } else {
            throw new NotFound("Course Not Found for Id: "+id);
        }

    }

    public void dataEntry() throws IOException {
        /*Fetch Data from Course File*/
        Resource resource = new ClassPathResource(courseFile);
        ObjectMapper objectMapper = new ObjectMapper();
        Course[] courses = objectMapper.readValue(resource.getFile(), Course[].class);
        /*drop collection*/
        dropCollection();
        /*insert one by one*/
        for(Course c: courses){
            mongoTemplate.save(c);
        }
    }

    public Object dropCourse() {
        /*drop collection*/
        dropCollection();
        return "Success";
    }

    public Object rateChapter(String courseId, String chapterName, int rating) {
        /*fetch data*/
        Optional<Course> optionalCourse = courseDAO.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Optional<Chapters> optionalChapter = course.getChapters().stream()
                    .filter(chapter -> chapter.getName().equals(chapterName))
                    .findFirst();
            if (optionalChapter.isPresent()) {
                Chapters chapter = optionalChapter.get();
                // Update the rating of the chapter
                chapter.setRating(rating);
                // Save the updated course back to the database
                mongoTemplate.save(course);
                return "Chapter rating updated successfully.";
            } else {
                throw new NotFound("Cant able to find the course, Course Id: "+courseId);
            }
        } else {
            throw new NotFound("Cant able to find the course, Course Id: "+courseId);
        }
    }


    public Object getOptions() {
        List<String> input = new ArrayList<>();
        input.add("Alphabetical");
        input.add("date");
        input.add("rating");
        return input;
    }

    public void dropCollection(){
        mongoTemplate.dropCollection("test");
    }

}
