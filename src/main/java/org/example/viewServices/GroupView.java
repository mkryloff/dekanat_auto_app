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
import org.example.dataServices.GroupService;
import org.example.dialogForms.GroupDialogForm;
import org.example.entities.GroupEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class GroupView
{
  private TableView<GroupEntity> view;
  private final ObservableList<GroupEntity> data = FXCollections.observableArrayList();
  private final String role;
  private Integer currentPage = 0;

  public GroupView(String role)
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
    TableColumn<GroupEntity, Integer> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<GroupEntity, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name()));
    this.view.getColumns().add(idCol);
    this.view.getColumns().add(nameCol);
    try
    {
      this.data.addAll(GroupService.loadData(0));
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
    GroupEntity selected = this.view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      new Alert(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ГРУППУ").showAndWait();
      return;
    }
    Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
    Optional<ButtonType> result = confirmDelete.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK)
    {
      try
      {
        GroupService.deleteGroup(selected.id());
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
    GroupDialogForm form = new GroupDialogForm(null);
    Optional<GroupEntity> result = form.showAndWait();
    result.ifPresent(groupEntity ->
    {
      try
      {
        GroupService.createGroup(groupEntity.name());
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
    GroupEntity selected = view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      new Alert(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ГРУППУ").showAndWait();
      return;
    }
    GroupDialogForm form = new GroupDialogForm(selected);
    Optional<GroupEntity> result = form.showAndWait();
    result.ifPresent(groupEntity ->
    {
      try
      {
        GroupService.updateGroup(groupEntity.id() ,groupEntity.name());
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
    this.data.setAll(GroupService.loadData(currentPage));
  }
}
