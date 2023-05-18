package com.kimo.assignment.Controller;

import com.kimo.assignment.Entity.Chapters;
import com.kimo.assignment.Response.BaseResponse;
import com.kimo.assignment.Service.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/course")
@Description("Course Controller class for KIMO Assignment")
@Log4j2
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping("/getOptions")
    public ResponseEntity<String> getOptions() {
        return new ResponseEntity(BaseResponse.builder().data(courseService.getOptions()).build(), HttpStatus.OK);
    }


    @GetMapping("/getAllCourses")
    public ResponseEntity<Object> getAllCourses(@RequestParam int page,@RequestParam String options) throws Exception {
        return new ResponseEntity(BaseResponse.builder().data(courseService.getAllCourses(page,options)).build(), HttpStatus.OK);
    }

    @GetMapping("/insertData")
    public void healthCheck() throws IOException {
        courseService.dataEntry();
    }

    @GetMapping("/getCourseOverView")
    public ResponseEntity<Object> getCourseOverView(@RequestParam String id) throws Exception {
        return new ResponseEntity(BaseResponse.builder().data(courseService.getCourseOverView(id)).build(),HttpStatus.OK);
    }

    @GetMapping("/{courseId}/chapters/{chapterName}")
    public ResponseEntity<Chapters> getChapterInformation(
            @PathVariable("courseId") String courseId,
            @PathVariable("chapterName") String chapterName) throws Exception {
       return new ResponseEntity(BaseResponse.builder().data(courseService.getChapterInfo(courseId,chapterName)).build(),HttpStatus.OK);
    }
    @PutMapping("/{courseId}/chapters/{chapterName}/rating")
    public ResponseEntity<String> rateChapter(
            @PathVariable("courseId") String courseId,
            @PathVariable("chapterName") String chapterName,
            @RequestParam("rating") int rating
    ) {
        return new ResponseEntity(BaseResponse.builder().data(courseService.rateChapter(courseId,chapterName,rating)).build(),HttpStatus.OK);
    }

    @DeleteMapping("/dropCourse")
    public ResponseEntity<Object> dropCourse(){
        return new ResponseEntity(BaseResponse.builder().data(courseService.dropCourse()).build(),HttpStatus.OK);
    }

}
