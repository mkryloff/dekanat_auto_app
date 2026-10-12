package org.example.entities;

import org.jetbrains.annotations.NotNull;

public record GroupEntity(
    Integer id,
    String name
) implements Entity
{
  @NotNull
  @Override
  public String toString()
  {
    return this.name;
  }
}
