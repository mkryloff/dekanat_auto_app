package org.example.pages;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ReportsPage implements Page
{
  @Override
  public Node build(String role)
  {
    TabPane tp = new TabPane();
    Tab statsByGroups = new Tab("Средний балл по группам");
    Tab statsByYears = new Tab("Средний балл по годам");

    tp.getTabs().addAll(statsByGroups, statsByYears);
    return tp;
  }
}
