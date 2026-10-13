package org.example.pages;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.example.viewServices.GroupView;
import org.example.viewServices.PersonView;
import org.example.viewServices.SubjectView;


public class RefbooksPage implements Page
{
  @Override
  public Node build(String role)
  {
    TabPane tp = new TabPane();
    Tab groupTab = new Tab("Groups", new GroupView(role).createView());
    Tab subjectTab = new Tab("Subjects", new SubjectView(role).createView());
    Tab personTab = new Tab("People", new PersonView(role).createView());
    tp.getTabs().addAll(groupTab, subjectTab, personTab);
    return tp;
  }
}
