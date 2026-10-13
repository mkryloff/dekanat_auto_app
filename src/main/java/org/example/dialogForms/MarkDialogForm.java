package org.example.dialogForms;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.Notifications;
import org.example.dataServices.PersonService;
import org.example.dataServices.SubjectService;
import org.example.entities.MarkEntity;
import org.example.entities.PersonEntity;
import org.example.entities.SubjectEntity;

import java.io.IOException;
import java.sql.SQLException;

public class MarkDialogForm extends Dialog<MarkEntity>
{
  private final ComboBox<PersonEntity> studentField = new ComboBox<>();
  private final ComboBox<SubjectEntity> subjectField = new ComboBox<>();
  private final ComboBox<PersonEntity> teacherField = new ComboBox<>();
  private final TextField valueField = new TextField();

  public MarkDialogForm(MarkEntity existing)
  {
    try
    {
      this.studentField.getItems().addAll(PersonService.loadDataByType("S", 0));
      this.subjectField.getItems().addAll(SubjectService.loadData(0));
      this.teacherField.getItems().addAll(PersonService.loadDataByType("P", 0));
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
      this.studentField.getItems().stream()
          .filter(entity -> entity.id().equals(existing.studentID()))
          .findFirst()
          .ifPresent(studentField::setValue);

      this.subjectField.getItems().stream()
          .filter(entity -> entity.id().equals(existing.subjectID()))
          .findFirst()
          .ifPresent(subjectField::setValue);

      this.teacherField.getItems().stream()
          .filter(entity -> entity.id().equals(existing.teacherID()))
          .findFirst()
          .ifPresent(teacherField::setValue);

      this.valueField.setText(existing.value().toString());
    }

    GridPane layout = new GridPane();
    layout.setVgap(10);
    layout.setHgap(10);

    layout.add(new Label("ФИО УЧЕНИКА:"), 0, 0);
    layout.add(this.studentField, 1, 0);

    layout.add(new Label("НАЗВАНИЕ ПРЕДМЕТА:"), 0, 1);
    layout.add(this.subjectField, 1, 1);

    layout.add(new Label("ФИО УЧИТЕЛЯ:"), 0, 2);
    layout.add(this.teacherField, 1, 2);

    layout.add(new Label("ОЦЕНКА:"), 0, 3);
    layout.add(this.valueField, 1, 3);

    getDialogPane().setContent(layout);

    setResultConverter(button -> {
      if (button == submitButton)
      {
        if (existing != null)
        {
          return new MarkEntity(
              existing.id(),
              this.studentField.getValue().id(),
              this.studentField.getValue().fullName(),
              this.subjectField.getValue().id(),
              this.subjectField.getValue().name(),
              this.teacherField.getValue().id(),
              this.teacherField.getValue().fullName(),
              Integer.parseInt(this.valueField.getText())
          );
        }
        return new MarkEntity(
            null,
            this.studentField.getValue().id(),
            this.studentField.getValue().fullName(),
            this.subjectField.getValue().id(),
            this.subjectField.getValue().name(),
            this.teacherField.getValue().id(),
            this.teacherField.getValue().fullName(),
            Integer.parseInt(this.valueField.getText())
        );
      }
      return null;
    });
  }
}
