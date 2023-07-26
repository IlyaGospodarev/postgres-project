package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServlet extends HttpServlet {
    private HttpServletResponse response;

    private static final String JDBC_URL = "jdbc:postgresql://192.168.0.126:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String surname = resultSet.getString("surname");

                User user = new User(id, name, age, surname);
                userList.add(user);
            }

            request.setAttribute("userList", userList);
            request.getRequestDispatcher("webapp/user-list.jsp").forward(request, response);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching users");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing.");
            return;
        }

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            switch (action) {
                case "add":
                    addUser(request, connection);
                    break;
                case "delete":
                    deleteUser(request, connection);
                    break;
                case "update":
                    updateUser(request, connection);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);
                    break;
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
        }
    }

    private void addUser(HttpServletRequest request, Connection connection) throws SQLException, IOException {
        String name = request.getParameter("name");
        String ageString = request.getParameter("age");
        String surname = request.getParameter("surname");

        if (name == null || ageString == null || surname == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters for adding a user.");
            return;
        }

        int age = Integer.parseInt(ageString);

        String sql = "INSERT INTO users (name, age, surname) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, surname);
            statement.executeUpdate();
        }
    }

    private void deleteUser(HttpServletRequest request, Connection connection) throws SQLException, IOException {
        String idString = request.getParameter("id");

        if (idString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter 'id' for deleting a user.");
            return;
        }

        int id = Integer.parseInt(idString);

        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private void updateUser(HttpServletRequest request, Connection connection) throws SQLException, IOException {
        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String ageString = request.getParameter("age");
        String surname = request.getParameter("surname");


        if (idString == null || name == null || ageString == null || surname == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters for updating a user.");
            return;
        }

        int id = Integer.parseInt(idString);
        int age = Integer.parseInt(ageString);

        String sql = "UPDATE users SET name = ?, age = ?, surname = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, surname);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

}

