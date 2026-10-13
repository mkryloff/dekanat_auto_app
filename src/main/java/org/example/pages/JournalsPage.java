package org.example.pages;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.example.viewServices.MarkView;

public class JournalsPage implements Page
{
  @Override
  public Node build(String role)
  {
    TabPane tp = new TabPane();
    Tab subjectTab = new Tab("Marks", new MarkView(role).createView());
    tp.getTabs().add(subjectTab);
    return tp;
  }
}
