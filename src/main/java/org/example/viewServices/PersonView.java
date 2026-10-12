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
import org.example.dataServices.PersonService;
import org.example.dialogForms.PersonDialogForm;
import org.example.entities.PersonEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class PersonView
{
  private TableView<PersonEntity> view;
  private final ObservableList<PersonEntity> data = FXCollections.observableArrayList();
  private final String role;
  private Integer currentPage = 0;

  public PersonView(String role)
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
    TableColumn<PersonEntity, Integer> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<PersonEntity, String> firstNameCol = new TableColumn<>("firstName");
    firstNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().firstName()));

    TableColumn<PersonEntity, String> lastNameCol = new TableColumn<>("lastName");
    lastNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().lastName()));

    TableColumn<PersonEntity, String> fatherNameCol = new TableColumn<>("fatherName");
    fatherNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().fatherName()));

    TableColumn<PersonEntity, String> groupNameCol = new TableColumn<>("groupName");
    groupNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().groupName()));

    TableColumn<PersonEntity, String> typeCol = new TableColumn<>("type");
    typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().type()));

    view.getColumns().addAll(idCol, firstNameCol, lastNameCol, fatherNameCol, groupNameCol, typeCol);

    try
    {
      this.data.addAll(PersonService.loadData(0));
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
    PersonEntity selected = this.view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      Notifications.notify(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ПЕРСОНУ");
      return;
    }
    Optional<ButtonType> result = Notifications.notifyWithReturn(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить?");
    if (result.isPresent() && result.get() == ButtonType.OK)
    {
      try
      {
        PersonService.deletePerson(selected.id());
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
    PersonDialogForm form = new PersonDialogForm(null);
    Optional<PersonEntity> result = form.showAndWait();
    result.ifPresent(personEntity ->
    {
      try
      {
        PersonService.createPerson(
            personEntity.firstName(),
            personEntity.lastName(),
            personEntity.fatherName(),
            personEntity.groupID(),
            personEntity.type()
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
    PersonEntity selected = view.getSelectionModel().getSelectedItem();
    if (selected == null)
    {
      Notifications.notify(Alert.AlertType.WARNING, "НЕОБХОДИМО ВЫБРАТЬ ПЕРСОНУ");
      return;
    }
    PersonDialogForm form = new PersonDialogForm(selected);
    Optional<PersonEntity> result = form.showAndWait();
    result.ifPresent(personEntity ->
    {
      try
      {
        PersonService.updatePerson(
            personEntity.id(),
            personEntity.firstName(),
            personEntity.lastName(),
            personEntity.fatherName(),
            personEntity.groupID(),
            personEntity.type()
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
    this.data.setAll(PersonService.loadData(currentPage));
  }
}
