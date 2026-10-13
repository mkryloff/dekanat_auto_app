package org.example.entities;

import org.jetbrains.annotations.NotNull;

public record PersonEntity(
    Integer id,
    String firstName,
    String lastName,
    String fatherName,
    Integer groupID,
    String groupName,
    String type
)
{
  @Override
  @NotNull
  public String toString()
  {
    return this.fullName();
  }

  public String fullName()
  {
    return this.firstName + " " + this.lastName + " " + this.fatherName;
  }
}
