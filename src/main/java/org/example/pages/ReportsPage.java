package org.example.pages;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.example.viewServices.statisticsViews.CrossAnalysisView;

public class ReportsPage implements Page
{
  @Override
  public Node build(String role)
  {
    TabPane tp = new TabPane();
    Tab statsByYears = new Tab("Средний балл по годам", new CrossAnalysisView().createView());

    tp.getTabs().add(statsByYears);
    return tp;
  }
}
