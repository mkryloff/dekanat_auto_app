package org.example.dataServices;

import org.example.DatabaseConnection;
import org.example.entities.statisticsEntities.CrossAnalysisEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrossAnalysisService
{
  public static List<CrossAnalysisEntity> loadData(Integer yearFrom, Integer yearTo, String entityName) throws SQLException, IOException
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("SELECT * FROM get_cross_analysis(?, ?, ?)");
      s.setInt(1, yearFrom);
      s.setInt(2, yearTo);
      s.setString(3, entityName);
      ResultSet rs = s.executeQuery();
      List<CrossAnalysisEntity> queryRes = new ArrayList<>();
      while (rs.next())
      {
        queryRes.add(new CrossAnalysisEntity(rs.getInt("year"), rs.getString("entity_name"), rs.getFloat("avg_mark")));
      }
      return queryRes;
    }
  }
}
