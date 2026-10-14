package org.example.viewServices;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.Notifications;
import org.example.dataServices.MarkService;
import org.example.dataServices.PersonService;
import org.example.dataServices.SubjectService;
import org.example.dialogForms.MarkDialogForm;
import org.example.entities.MarkEntity;
import org.example.entities.PersonEntity;
import org.example.entities.SubjectEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class MarkView
{
  private TableView<MarkEntity> view;
  private final ObservableList<MarkEntity> data = FXCollections.observableArrayList();
  private final String role;
  private final ComboBox<PersonEntity> studentFilter = new ComboBox<>();
  private final ComboBox<SubjectEntity> subjectFilter = new ComboBox<>();
  private final ComboBox<PersonEntity> teacherFilter = new ComboBox<>();
  private Integer currentPage = 0;

  public MarkView(String role)
  {
    this.role = role;
  }

  public Node createView()
  {
    HBox toolBar = new HBox();

    try
    {
      studentFilter.getItems().add(null);
      studentFilter.getItems().addAll(PersonService.loadDataByType("S", 0));
      subjectFilter.getItems().add(null);
      subjectFilter.getItems().addAll((SubjectService.loadData(0)));
      teacherFilter.getItems().add(null);
      teacherFilter.getItems().addAll(PersonService.loadDataByType("P", 0));
    }
    catch (SQLException | IOException e)
    {
      Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
    }

    studentFilter.valueProperty().addListener((o, a, b) ->  {
      try
      {
        data.setAll(MarkService.loadFilteredData(studentFilter.getValue(), subjectFilter.getValue(), teacherFilter.getValue()));
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });

    subjectFilter.valueProperty().addListener((o, a, b) ->  {
      try
      {
        data.setAll(MarkService.loadFilteredData(studentFilter.getValue(), subjectFilter.getValue(), teacherFilter.getValue()));
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });

    teacherFilter.valueProperty().addListener((o, a, b) ->  {
      try
      {
        data.setAll(MarkService.loadFilteredData(studentFilter.getValue(), subjectFilter.getValue(), teacherFilter.getValue()));
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });

    Button deleteButton = new Button("УДАЛИТЬ");
    deleteButton.setOnAction(e -> handleDelete());

    Button editButton = new Button("ИЗМЕНИТЬ");
    editButton.setOnAction(e -> handleEdit());

    Button createButton = new Button("ДОБАВИТЬ");
    createButton.setOnAction(e -> handleCreate());

    toolBar.getChildren().addAll(studentFilter, subjectFilter, teacherFilter, deleteButton, editButton, createButton);
    toolBar.setAlignment(Pos.CENTER_RIGHT);

    if (this.role.equals("user"))
    {
      toolBar.setVisible(false);
    }

    this.view = new TableView<>();
    TableColumn<MarkEntity, Integer> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<MarkEntity, String> studentNameCol = new TableColumn<>("studentName");
    studentNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().studentName()));

    TableColumn<MarkEntity, String> subjectNameCol = new TableColumn<>("subjectName");
    subjectNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().subjectName()));

    TableColumn<MarkEntity, String> teacherNameCol = new TableColumn<>("teacherName");
    teacherNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().teacherName()));

    TableColumn<MarkEntity, Integer> value = new TableColumn<>("value");
    value.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().value()).asObject());

    view.getColumns().addAll(idCol, studentNameCol, subjectNameCol, teacherNameCol, value);

    try
    {
      this.data.addAll(MarkService.loadData(0));
      this.view.setItems(this.data);
    }
    catch (SQLException | IOException e)
    {
      Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
    }

    VBox root = new VBox(10, toolBar, this.view);
    VBox.setVgrow(this.view, Priority.ALWAYS);
    return root;
  }

  private void handleDelete()
  {
    MarkEntity selected = this.view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      Notifications.notify(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ЗАПИСЬ");
      return;
    }
    Optional<ButtonType> result = Notifications.notifyWithReturn(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить?");
    if (result.isPresent() && result.get() == ButtonType.OK)
    {
      try
      {
        MarkService.deleteMark(selected.id());
        refreshView();
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    }
  }

  private void handleCreate()
  {
    MarkDialogForm form = new MarkDialogForm(null);
    Optional<MarkEntity> result = form.showAndWait();
    result.ifPresent(markEntity ->
    {
      try
      {
        MarkService.createMark(
            markEntity.studentID(),
            markEntity.subjectID(),
            markEntity.teacherID(),
            markEntity.value()
        );
        refreshView();
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });
  }

  private void handleEdit()
  {
    MarkEntity selected = view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      Notifications.notify(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ПЕРСОНУ");
      return;
    }
    MarkDialogForm form = new MarkDialogForm(selected);
    Optional<MarkEntity> result = form.showAndWait();
    result.ifPresent(markEntity ->
    {
      try
      {
        MarkService.updateMark(
            markEntity.id(),
            markEntity.studentID(),
            markEntity.subjectID(),
            markEntity.teacherID(),
            markEntity.value()
        );
        refreshView();
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });
  }

  private void refreshView() throws SQLException, IOException
  {
    this.data.setAll(MarkService.loadData(currentPage));
    resetFilters();
  }

  private void resetFilters()
  {
    studentFilter.setValue(null);
    subjectFilter.setValue(null);
    teacherFilter.setValue(null);
  }
}
