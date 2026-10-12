package org.example.entities;

public record PersonEntity(
    Integer id,
    String firstName,
    String lastName,
    String fatherName,
    Integer groupID,
    String groupName,
    String type
)
{}
