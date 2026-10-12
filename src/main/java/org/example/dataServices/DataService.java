package org.example.dataServices;

import org.example.entities.Entity;

import java.util.List;

public interface DataService
{
  List<? extends Entity> loadData();
}


