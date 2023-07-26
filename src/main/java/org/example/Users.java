package org.example;// EmployeeServlet.java

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;


public class Users extends HttpServlet {
    private static final String JDBC_URL = "jdbc:postgresql://192.168.0.126:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            out.println("<html><body><h2>Users List</h2><ul>");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String surname = resultSet.getString("surname");
                out.println("<li>User ID: " + id + ", Name: " + name + ", Age: " + age + ", Surname: " + surname + "</li>");
            }

            out.println("</ul></body></html>");

            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }
}
