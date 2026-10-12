package org.example.dataServices;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.DatabaseConnection;
import org.example.entities.SubjectEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectService
{
  public static List<SubjectEntity> loadData(Integer pageNum) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("SELECT * FROM subjects LIMIT 25 OFFSET ?");
      s.setInt(1, pageNum);
      ResultSet rs = s.executeQuery();
      List<SubjectEntity> queryRes = new ArrayList<>();
      while (rs.next())
      {
        queryRes.add(new SubjectEntity(rs.getInt("id"), rs.getString("name")));
      }
      return queryRes;
    }
  }

  public static Node createView()
  {
    TableView<SubjectEntity> view = new TableView<>();
    TableColumn<SubjectEntity, Integer> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());

    TableColumn<SubjectEntity, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name()));
    view.getColumns().add(idCol);
    view.getColumns().add(nameCol);
    ObservableList<SubjectEntity> data = FXCollections.observableArrayList();
    try
    {
      data.addAll(SubjectService.loadData(0));
      view.setItems(data);
    }
    catch (SQLException | IOException e)
    {
      throw new RuntimeException(e);
    }
    return view;
  }
}
