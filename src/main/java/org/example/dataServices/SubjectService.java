package org.example.dataServices;

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

  public static void deleteSubject(Integer id) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("DELETE FROM subjects WHERE id = ?");
      s.setInt(1, id);
      s.execute();
    }
  }

  public static void updateSubject(Integer id, String newName) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("UPDATE subjects SET name = ? WHERE id = ?");
      s.setString(1, newName);
      s.setInt(2, id);
      s.execute();
    }
  }

  public static void createSubject(String newSubject) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("INSERT INTO subjects (name) VALUES (?)");
      s.setString(1, newSubject);
      s.execute();
    }
  }
}
