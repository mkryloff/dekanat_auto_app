package org.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Notifications
{
  public static void notify(Alert.AlertType alertType, String message)
  {
    Alert alert = new Alert(alertType);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static Optional<ButtonType> notifyWithReturn(Alert.AlertType alertType, String message)
  {
    Alert alert = new Alert(alertType);
    alert.setHeaderText(null);
    alert.setContentText(message);
    return alert.showAndWait();
  }
}
