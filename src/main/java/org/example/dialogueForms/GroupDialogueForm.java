package org.example.dialogueForms;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.entities.GroupEntity;

public class GroupDialogueForm extends Dialog<GroupEntity>
{
  private final TextField groupNameField = new TextField();

  public GroupDialogueForm(GroupEntity existing)
  {
    ButtonType submitButton = new ButtonType("СОХРАНИТЬ", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButton = new ButtonType("ОТМЕНА", ButtonBar.ButtonData.CANCEL_CLOSE);

    getDialogPane().getButtonTypes().addAll(submitButton, cancelButton);
    if (existing != null)
    {
      groupNameField.setText(existing.name());
    }

    GridPane layout = new GridPane();
    layout.setVgap(10);
    layout.setHgap(10);

    layout.add(new Label("НАЗВАНИЕ ГРУППЫ"), 0, 0);
    layout.add(groupNameField, 1, 0);

    getDialogPane().setContent(layout);

    setResultConverter(button -> {
      if (button == submitButton)
      {
        if (existing != null)
        {
          return new GroupEntity(existing.id(), groupNameField.getText().trim());
        }
        return new GroupEntity(null, groupNameField.getText().trim());
      }
      return null;
    });
  }


}
