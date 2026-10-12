package org.example.dataServices;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.DatabaseConnection;
import org.example.entities.PersonEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonService
{
  public static List<PersonEntity> loadData(Integer pageNum) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String query = """
          SELECT
            p.id,
            p.first_name,
            p.last_name,
            p.father_name,
            g.id AS group_id,
            g.name AS group_name,
            p.type
          FROM people AS p
          LEFT JOIN groups AS g
            ON p.group_id = g.id
          LIMIT 25
          OFFSET ?
          """;

      PreparedStatement s = conn.prepareStatement(query);
      s.setInt(1, pageNum);
      ResultSet rs = s.executeQuery();
      List<PersonEntity> queryRes = new ArrayList<>();
      while (rs.next())
      {
        queryRes.add(new PersonEntity(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("father_name"),
            rs.getInt("group_id"),
            rs.getString("group_name"),
            rs.getString("type")
        ));
      }
      return queryRes;
    }
  }

  public static Node createView()
  {
    TableView<PersonEntity> view = new TableView<>();
    TableColumn<PersonEntity, Integer> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<PersonEntity, String> firstNameCol = new TableColumn<>("firstName");
    firstNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().firstName()));

    TableColumn<PersonEntity, String> lastNameCol = new TableColumn<>("lastName");
    lastNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().lastName()));

    TableColumn<PersonEntity, String> fatherNameCol = new TableColumn<>("fatherName");
    fatherNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().fatherName()));

    TableColumn<PersonEntity, Integer> groupIdCol = new TableColumn<>("groupID");
    groupIdCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<PersonEntity, String> groupNameCol = new TableColumn<>("groupName");
    groupNameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().groupName()));

    TableColumn<PersonEntity, String> typeCol = new TableColumn<>("type");
    typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().type()));

    view.getColumns().addAll(idCol, firstNameCol, lastNameCol, fatherNameCol, groupIdCol, groupNameCol, typeCol);
    ObservableList<PersonEntity> data = FXCollections.observableArrayList();
    try
    {
      data.addAll(PersonService.loadData(0));
      view.setItems(data);
    }
    catch (SQLException | IOException e)
    {
      throw new RuntimeException(e);
    }
    return view;
  }
}
