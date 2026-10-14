package org.example.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
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
    mainStage.setTitle("Деканат");
    this.root = new BorderPane();
    HBox menuBar = new HBox();

    HBox initialCenter = new HBox(new Label("ВЫБЕРИТЕ РАЗДЕЛ"));
    root.setCenter(initialCenter);

    ToggleButton switchToReferenceBooks = new ToggleButton("СПРАВОЧНИКИ");
    switchToReferenceBooks.setOnAction(e -> handleSwitchPage("refbooks"));

    ToggleButton switchToJournals = new ToggleButton("ЖУРНАЛЫ");
    switchToJournals.setOnAction(e -> handleSwitchPage("journals"));

    ToggleButton switchToReports = new ToggleButton("ОТЧЕТЫ");
    switchToReports.setOnAction(e -> handleSwitchPage("reports"));

    menuBar.getChildren().addAll(switchToReferenceBooks, switchToJournals, switchToReports);

    HBox statusBar = new HBox();
    statusBar.setSpacing(10);
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
        root.setCenter(new RefbooksPage().build(this.userRole));
        mainStage.setTitle("Справочники");
        break;
      case "journals":
        root.setCenter(new JournalsPage().build(this.userRole));
        mainStage.setTitle("Журналы");
        break;
      case "reports":
        root.setCenter(new ReportsPage().build(this.userRole));
        mainStage.setTitle("Отчеты");
        break;
    }
  }
}
