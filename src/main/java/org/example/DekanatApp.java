package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DekanatApp extends Application
{
//    public static void main(String[] args) throws SQLException
//    {
//      Connection conn = DriverManager.getConnection("jdbc:postgresql://172.29.20.230:5432/dekanat", "postgres", "12345");
//      ResultSet resultOfQuery =  conn.createStatement().executeQuery("SELECT * FROM groups");
//      while (resultOfQuery.next()) {
//        System.out.println(resultOfQuery.getString("name"));
//      }
//    }

  @Override
  public void start(Stage primaryStage) {
    Label label = new Label("Привет, JavaFX!");
    StackPane root = new StackPane(label);
    Scene scene = new Scene(root, 400, 300);

    primaryStage.setTitle("Деканат");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}