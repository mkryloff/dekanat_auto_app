package org.example.entities;

public record MarkEntity(
    Integer id,
    Integer studentID,
    String studentName,
    Integer subjectID,
    String subjectName,
    Integer teacherID,
    String teacherName,
    Integer value
)
{}
