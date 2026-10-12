package org.example.dialogForms;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.Notifications;
import org.example.dataServices.GroupService;
import org.example.entities.GroupEntity;
import org.example.entities.PersonEntity;

import java.io.IOException;
import java.sql.SQLException;

public class PersonDialogForm extends Dialog<PersonEntity>
{
  private final TextField firstNameField = new TextField();
  private final TextField lastNameField = new TextField();
  private final TextField fatherNameField = new TextField();
  private final ComboBox<GroupEntity> groupField = new ComboBox<>();
  private final ComboBox<String> typeField = new ComboBox<>();

  public PersonDialogForm(PersonEntity existing)
  {
    try
    {
      this.groupField.getItems().add(null);
      this.groupField.getItems().addAll(GroupService.loadData(0));

      this.typeField.getItems().addAll("S", "P");
    }
    catch (SQLException | IOException e)
    {
      Notifications.notify(Alert.AlertType.ERROR, "CANNOT OPEN FORM " + e.getMessage());
    }

    ButtonType submitButton = new ButtonType("СОХРАНИТЬ", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButton = new ButtonType("ОТМЕНА", ButtonBar.ButtonData.CANCEL_CLOSE);

    getDialogPane().getButtonTypes().addAll(submitButton, cancelButton);
    if (existing != null)
    {
      this.firstNameField.setText(existing.firstName());
      this.lastNameField.setText(existing.lastName());
      this.fatherNameField.setText(existing.fatherName());
      if (existing.groupID() != null)
      {
        this.groupField.setValue(new GroupEntity(existing.groupID(), existing.groupName()));
      }
      else
      {
        this.groupField.setValue(null);
      }
      this.typeField.setValue(existing.type());
    }

    GridPane layout = new GridPane();
    layout.setVgap(10);
    layout.setHgap(10);

    layout.add(new Label("ИМЯ:"), 0, 0);
    layout.add(this.firstNameField, 1, 0);

    layout.add(new Label("ФАМИЛИЯ:"), 0, 1);
    layout.add(this.lastNameField, 1, 1);

    layout.add(new Label("ОТЧЕСТВО:"), 0, 2);
    layout.add(this.fatherNameField, 1, 2);

    layout.add(new Label("ГРУППА:"), 0, 3);
    layout.add(this.groupField, 1, 3);

    layout.add(new Label("СТАТУС:"), 0, 4);
    layout.add(this.typeField, 1, 4);

    getDialogPane().setContent(layout);

    setResultConverter(button -> {
      if (button == submitButton)
      {
        if (existing != null)
        {
          return new PersonEntity(
              existing.id(),
              this.firstNameField.getText().trim(),
              this.lastNameField.getText().trim(),
              this.fatherNameField.getText().trim(),
              this.groupField.getValue().id(),
              this.groupField.getValue().name(),
              this.typeField.getValue()
          );
        }
        return new PersonEntity(
            null,
            this.firstNameField.getText().trim(),
            this.lastNameField.getText().trim(),
            this.fatherNameField.getText().trim(),
            this.groupField.getValue().id(),
            this.groupField.getValue().name(),
            this.typeField.getValue()
        );
      }
      return null;
    });
  }
}
