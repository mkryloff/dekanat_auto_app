package org.example;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.windows.LoginWindow;

import java.util.Objects;

public class DekanatApp extends Application
{
  @Override
  public void start(Stage primaryStage)
  {
    primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/mainIcon.jpg"))));
    new LoginWindow(primaryStage).display();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}