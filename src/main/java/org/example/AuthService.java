package org.example;

import java.io.IOException;
import java.sql.*;

import org.mindrot.*;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService
{
  public record AuthResult(String role,Boolean isSuccess, String message)
  {}

  public AuthResult loginUser(final String login, final String password)
  {
    try (Connection conn = DatabaseConnection.createConnection("config.ini"))
    {
      PreparedStatement s = conn.prepareStatement("SELECT password, role FROM private.users WHERE login = ?");
      s.setString(1, login);
      ResultSet res = s.executeQuery();
      if (res.next())
      {
        if (BCrypt.checkpw(password, res.getString("password")))
        {
          return new AuthResult(res.getString("role"), true, "success");
        }
        else
        {
          return new AuthResult(null, false, "ERROR: Incorrect password or login");
        }
      }
      else
      {
        return new AuthResult(null, false, "ERROR: Incorrect login");
      }
    }
    catch (SQLException | IOException e)
    {
      return new AuthResult(null, false, "ERROR: " + e.getMessage());
    }
  }
}
