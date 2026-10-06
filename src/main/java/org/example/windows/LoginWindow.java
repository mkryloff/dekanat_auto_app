package org.example.windows;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.AuthService;
import org.example.Notifications;

public class LoginWindow
{
  private final Stage mainStage;
  private final AuthService auth;
  private final TextField loginField = new TextField();
  private final PasswordField passwordField = new PasswordField();

  public LoginWindow(Stage mainStage)
  {
    this.mainStage = mainStage;
    this.auth = new AuthService();
  }

  public void display()
  {
    Button checkAuthButton = new Button("ВОЙТИ");
    checkAuthButton.setOnAction(e -> handleSubmit());

    Label mainLabel = new Label("АВТОРИЗАЦИЯ");

    loginField.setPromptText("ЛОГИН");
    passwordField.setPromptText("ПАРОЛЬ");

    GridPane root = new GridPane();
    root.setHgap(10);
    root.setVgap(10);
    root.setAlignment(Pos.TOP_CENTER);
    root.add(mainLabel, 1, 0);
    root.add(loginField, 1, 1);
    root.add(passwordField, 1, 2);
    root.add(checkAuthButton, 0, 3, 3, 1);
    GridPane.setHalignment(checkAuthButton, HPos.CENTER);
    GridPane.setHalignment(mainLabel, HPos.CENTER);

    Scene loginScene = new Scene(root, 400, 600);
    mainStage.setScene(loginScene);
  }

  private void handleSubmit()
  {
    if (loginField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty())
    {
        Notifications.notifyError("Заполнены не все необходимые поля");
        return;
    }
    AuthService.AuthResult result = auth.loginUser(loginField.getText(), passwordField.getText());
    if (result.isSuccess())
    {
      new MainWindow(mainStage).display();
    }
    else
    {
      Notifications.notifyError(result.message());
    }
  }
}
