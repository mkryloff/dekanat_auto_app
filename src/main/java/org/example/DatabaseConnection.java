package org.example;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection
{
  public static Connection createConnection(String configPath) throws IOException, SQLException
  {
    try (Reader r = new FileReader(configPath))
    {
      Properties props = new Properties();
      props.load(r);

      final String host = props.getProperty("Host");
      final String port = props.getProperty("Port");
      final String dbName = props.getProperty("Database");
      final String user = props.getProperty("User");
      final String password = props.getProperty("Password");

      String connectionURL = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
      return DriverManager.getConnection(connectionURL, user, password);
    }
  }
}
