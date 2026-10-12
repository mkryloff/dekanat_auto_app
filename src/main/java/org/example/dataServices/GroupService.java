package org.example.dataServices;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.example.DatabaseConnection;
import org.example.entities.GroupEntity;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupService
{
  public static List<GroupEntity> loadData(Integer pageNum) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("SELECT * FROM groups LIMIT 25 OFFSET ?");
      s.setInt(1, pageNum);
      ResultSet rs = s.executeQuery();
      List<GroupEntity> queryRes = new ArrayList<>();
      while (rs.next())
      {
        queryRes.add(new GroupEntity(rs.getInt("id"), rs.getString("name")));
      }
      return queryRes;
    }
  }

  public static void deleteGroup(Integer id) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("DELETE FROM groups WHERE id = ?");
      s.setInt(1, id);
      s.execute();
    }
  }

  public static void updateGroup(Integer id, String newName) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("UPDATE groups SET name = ? WHERE id = ?");
      s.setString(1, newName);
      s.setInt(2, id);
      s.execute();
    }
  }

  public static void createGroup(String newGroup) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("INSERT INTO groups (name) VALUES (?)");
      s.setString(1, newGroup);
      s.execute();
    }
  }
}
