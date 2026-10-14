package org.example.dataServices;

import org.example.DatabaseConnection;
import org.example.entities.MarkEntity;
import org.example.entities.PersonEntity;
import org.example.entities.SubjectEntity;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarkService
{
  public static List<MarkEntity> loadData(Integer pageNum) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String query = """
          SELECT
            m.id,
            p1.id AS student_id,
            p1.first_name || ' ' || p1.last_name || ' ' || p1.father_name AS student_name,
            s.id AS subject_id,
            s.name AS subject_name,
            p2.id AS teacher_id,
            p2.first_name || ' ' || p2.last_name || ' ' || p2.father_name AS teacher_name,
            m.value
          FROM marks AS m
          JOIN people AS p1
            ON p1.id = m.student_id
          JOIN people AS p2
            ON p2.id = m.teacher_id
          JOIN subjects AS s
            ON s.id = m.subject_id
          LIMIT 25
          OFFSET ?
          """;

      PreparedStatement s = conn.prepareStatement(query);
      s.setInt(1, pageNum);
      ResultSet rs = s.executeQuery();
      List<MarkEntity> queryRes = new ArrayList<>();
      while (rs.next())
      {
        queryRes.add(new MarkEntity(
            rs.getInt("id"),
            rs.getInt("student_id"),
            rs.getString("student_name"),
            rs.getInt("subject_id"),
            rs.getString("subject_name"),
            rs.getInt("teacher_id"),
            rs.getString("teacher_name"),
            rs.getInt("value")
        ));
      }
      return queryRes;
    }
  }

  public static List<MarkEntity> loadFilteredData(
      PersonEntity studentFilter,
      SubjectEntity subjectFilter,
      PersonEntity teacherFilter
  ) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String query = """
          SELECT
            m.id,
            p1.id AS student_id,
            p1.first_name || ' ' || p1.last_name || ' ' || p1.father_name AS student_name,
            s.id AS subject_id,
            s.name AS subject_name,
            p2.id AS teacher_id,
            p2.first_name || ' ' || p2.last_name || ' ' || p2.father_name AS teacher_name,
            m.value
          FROM marks AS m
          JOIN people AS p1
            ON p1.id = m.student_id
          JOIN people AS p2
            ON p2.id = m.teacher_id
          JOIN subjects AS s
            ON s.id = m.subject_id
          WHERE 1 = 1
          """;


      if (studentFilter != null)
      {
        query += " AND p1.id = ?";
      }
      if (subjectFilter != null)
      {
        query += " AND s.id = ?";
      }
      if (teacherFilter != null)
      {
        query += " AND p2.id = ?";
      }

      PreparedStatement s = conn.prepareStatement(query);

      int idx = 1;
      if (studentFilter != null)
      {
        s.setInt(idx, studentFilter.id());
        idx++;
      }
      if (subjectFilter != null)
      {
        s.setInt(idx, subjectFilter.id());
        idx++;
      }
      if (teacherFilter != null)
      {
        s.setInt(idx, teacherFilter.id());
      }

      ResultSet rs = s.executeQuery();
      List<MarkEntity> queryRes = new ArrayList<>();
      while (rs.next())
      {
        queryRes.add(new MarkEntity(
            rs.getInt("id"),
            rs.getInt("student_id"),
            rs.getString("student_name"),
            rs.getInt("subject_id"),
            rs.getString("subject_name"),
            rs.getInt("teacher_id"),
            rs.getString("teacher_name"),
            rs.getInt("value")
        ));
      }
      return queryRes;
    }
  }

  public static void deleteMark(Integer id) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("DELETE FROM marks WHERE id = ?");
      s.setInt(1, id);
      s.execute();
    }
  }

  public static void updateMark(
      Integer id,
      Integer studentID,
      Integer subjectID,
      Integer teacherID,
      Integer value
  ) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String query = """
          UPDATE marks
          SET
            student_id = ?,
            subject_id = ?,
            teacher_id = ?,
            value = ?
          WHERE id = ?
          """;

      PreparedStatement s = conn.prepareStatement(query);
      s.setInt(1, studentID);
      s.setInt(2, subjectID);
      s.setInt(3, teacherID);
      s.setInt(4, value);
      s.setInt(5, id);
      s.execute();
    }
  }

  public static void createMark(
      Integer studentID,
      Integer subjectID,
      Integer teacherID,
      Integer value
  ) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      String sql = """
          INSERT INTO marks (student_id, subject_id, teacher_id, value)
          VALUES (?, ?, ?, ?, ?)
          """;
      PreparedStatement s = conn.prepareStatement(sql);
      s.setInt(1, studentID);
      s.setInt(2, subjectID);
      s.setInt(3, teacherID);
      s.setInt(4, value);
      s.execute();
    }
  }
}
