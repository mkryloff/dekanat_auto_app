package org.example.entities;

import org.jetbrains.annotations.NotNull;

public record SubjectEntity(
    Integer id,
    String name
) implements Entity
{
  @Override
  @NotNull
  public String toString()
  {
    return this.name;
  }
}
