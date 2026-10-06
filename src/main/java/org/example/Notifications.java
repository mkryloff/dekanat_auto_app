package org.example;

import javafx.scene.control.Alert;

public class Notifications
{
  public static void notifyError(String message)
  {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
