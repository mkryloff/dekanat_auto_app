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
import org.example.dataServices.SubjectService;
import org.example.dialogForms.SubjectDialogForm;
import org.example.entities.SubjectEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class SubjectView
{
  private TableView<SubjectEntity> view;
  private final ObservableList<SubjectEntity> data = FXCollections.observableArrayList();
  private final String role;
  private Integer currentPage = 0;

  public SubjectView(String role)
  {
    this.role = role;
  }

  public Node createView()
  {
    HBox toolBar = new HBox();
    Button deleteButton = new Button("УДАЛИТЬ");
    deleteButton.setOnAction(e -> handleDelete());

    Button editButton = new Button("ИЗМЕНИТЬ");
    editButton.setOnAction(e -> handleEdit());

    Button createButton = new Button("ДОБАВИТЬ");
    createButton.setOnAction(e -> handleCreate());

    toolBar.getChildren().addAll(deleteButton, editButton, createButton);
    toolBar.setAlignment(Pos.CENTER_RIGHT);

    if (this.role.equals("user"))
    {
      toolBar.setVisible(false);
    }

    this.view = new TableView<>();
    TableColumn<SubjectEntity, Integer> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<SubjectEntity, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name()));
    this.view.getColumns().add(idCol);
    this.view.getColumns().add(nameCol);
    try
    {
      this.data.addAll(SubjectService.loadData(0));
      this.view.setItems(this.data);
    }
    catch (SQLException | IOException e)
    {
      throw new RuntimeException(e);
    }

    VBox root = new VBox(10, toolBar, this.view);
    VBox.setVgrow(this.view, Priority.ALWAYS);
    return root;
  }

  private void handleDelete()
  {
    SubjectEntity selected = this.view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      new Alert(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ПРЕДМЕТ").showAndWait();
      return;
    }
    Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
    Optional<ButtonType> result = confirmDelete.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK)
    {
      try
      {
        SubjectService.deleteSubject(selected.id());
        refreshView();
      }
      catch (SQLException | IOException e)
      {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
      }
    }
  }

  private void handleCreate()
  {
    SubjectDialogForm form = new SubjectDialogForm(null);
    Optional<SubjectEntity> result = form.showAndWait();
    result.ifPresent(subjectEntity ->
    {
      try
      {
        SubjectService.createSubject(subjectEntity.name());
        refreshView();
      }
      catch (SQLException | IOException e)
      {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
      }
    });
  }

  private void handleEdit()
  {
    SubjectEntity selected = view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      new Alert(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ПРЕДМЕТ").showAndWait();
      return;
    }
    SubjectDialogForm form = new SubjectDialogForm(selected);
    Optional<SubjectEntity> result = form.showAndWait();
    result.ifPresent(SubjectEntity ->
    {
      try
      {
        SubjectService.updateSubject(SubjectEntity.id() ,SubjectEntity.name());
        refreshView();
      }
      catch (SQLException | IOException e)
      {
        new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
      }
    });
  }

  private void refreshView() throws SQLException, IOException
  {
    this.data.setAll(SubjectService.loadData(currentPage));
  }
}
