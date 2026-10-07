package org.example.windows;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.pages.JournalsPage;
import org.example.pages.RefbooksPage;
import org.example.pages.ReportsPage;

public class MainWindow
{
  private final Stage mainStage;
  private final String userRole;
  private BorderPane root;


  public MainWindow(Stage mainStage, String userRole)
  {
    this.mainStage = mainStage;
    this.userRole = userRole;
  }

  public void display()
  {
    this.root = new BorderPane();
    HBox menuBar = new HBox();

    Button switchToReferenceBooks = new Button("СПРАВОЧНИКИ");
    switchToReferenceBooks.setOnAction(e -> handleSwitchPage("refbooks"));

    Button switchToJournals = new Button("ЖУРНАЛЫ");
    switchToJournals.setOnAction(e -> handleSwitchPage("journals"));

    Button switchToReports = new Button("ОТЧЕТЫ");
    switchToReports.setOnAction(e -> handleSwitchPage("reports"));

    menuBar.getChildren().addAll(switchToJournals, switchToReferenceBooks, switchToReports);

    HBox statusBar = new HBox();
    Label userNameLabel = new Label("Пользователь: " + this.userRole);
    Label userRoleLabel = new Label("Роль: " + this.userRole);

    statusBar.getChildren().addAll(userRoleLabel, userNameLabel);
    root.setTop(menuBar);
    root.setBottom(statusBar);


    Scene mainScene = new Scene(root, 1280, 720);
    mainStage.setScene(mainScene);
  }

  private void handleSwitchPage(String pageToSwitch)
  {
    switch (pageToSwitch)
    {
      case "refbooks":
        root.setCenter(new RefbooksPage().build());
        break;
      case "journals":
        root.setCenter(new JournalsPage().build());
        break;
      case "reports":
        root.setCenter(new ReportsPage().build());
        break;
    }
  }
}
