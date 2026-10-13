package org.example.dataServices;

import org.example.DatabaseConnection;
import org.example.entities.PersonEntity;

import java.io.IOException;
import java.sql.*;
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

  public static List<PersonEntity> loadDataByType(String type, Integer pageNum) throws SQLException, IOException
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
          WHERE p.type = ?
          LIMIT 25
          OFFSET ?
          """;

      PreparedStatement s = conn.prepareStatement(query);
      s.setString(1, type);
      s.setInt(2, pageNum * 25);
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

  public static void deletePerson(Integer id) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("DELETE FROM people WHERE id = ?");
      s.setInt(1, id);
      s.execute();
    }
  }

  public static void updatePerson(
      Integer id,
      String firstName,
      String lastName,
      String fatherName,
      Integer groupID,
      String type
  ) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String query = """
          UPDATE people
          SET
            first_name = ?,
            last_name = ?,
            father_name = ?,
            group_id = ?,
            type = ?
          WHERE id = ?
          """;

      PreparedStatement s = conn.prepareStatement(query);
      s.setString(1, firstName);
      s.setString(2, lastName);
      s.setString(3, fatherName);
      s.setObject(4, groupID, Types.INTEGER);
      s.setString(5, type);
      s.setInt(6, id);
      s.execute();
    }
  }

  public static void createPerson(
      String firstName,
      String lastName,
      String fatherName,
      Integer groupID,
      String type
  ) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String sql = """
          INSERT INTO people (first_name, last_name, father_name, group_id, type)
          VALUES (?, ?, ?, ?, ?)
          """;
      PreparedStatement s = conn.prepareStatement(sql);
      s.setString(1, firstName);
      s.setString(2, lastName);
      s.setString(3, fatherName);
      s.setObject(4, groupID, Types.INTEGER);
      s.setString(5, type);
      s.execute();
    }
  }
}
