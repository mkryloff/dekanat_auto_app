package org.example.pages;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.example.dataServices.PersonService;
import org.example.dataServices.SubjectService;
import org.example.viewServices.GroupView;


public class RefbooksPage implements Page
{
  @Override
  public Node build(String role)
  {
    TabPane tp = new TabPane();
    Tab groupTab = new Tab("Groups", new GroupView(role).createView());
    Tab subjectTab = new Tab("Subjects", SubjectService.createView());
    Tab personTab = new Tab("People", PersonService.createView());
    tp.getTabs().addAll(groupTab, subjectTab, personTab);
    return tp;
  }
}
