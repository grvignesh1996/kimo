package com.kimo.assignment.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "course")
public class Course {

    @Id
    String _id;
    String name;
    Long date;
    String description;
    List<String> domain;
    List<Chapters> chapters;

}
