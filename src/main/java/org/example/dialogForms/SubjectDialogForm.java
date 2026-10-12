package org.example.dialogForms;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.entities.SubjectEntity;

public class SubjectDialogForm extends Dialog<SubjectEntity>
{
  private final TextField subjectNameField = new TextField();

  public SubjectDialogForm(SubjectEntity existing)
  {
    ButtonType submitButton = new ButtonType("СОХРАНИТЬ", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButton = new ButtonType("ОТМЕНА", ButtonBar.ButtonData.CANCEL_CLOSE);

    getDialogPane().getButtonTypes().addAll(submitButton, cancelButton);
    if (existing != null)
    {
      this.subjectNameField.setText(existing.name());
    }

    GridPane layout = new GridPane();
    layout.setVgap(10);
    layout.setHgap(10);

    layout.add(new Label("НАЗВАНИЕ ПРЕДМЕТА"), 0, 0);
    layout.add(this.subjectNameField, 1, 0);

    getDialogPane().setContent(layout);

    setResultConverter(button -> {
      if (button == submitButton)
      {
        if (existing != null)
        {
          return new SubjectEntity(existing.id(), this.subjectNameField.getText().trim());
        }
        return new SubjectEntity(null, this.subjectNameField.getText().trim());
      }
      return null;
    });
  }
}
