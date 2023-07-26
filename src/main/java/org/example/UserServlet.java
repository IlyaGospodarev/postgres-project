package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private HttpServletResponse response;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = new ArrayList<>();

        System.out.println("doGet: entry");

        try (Connection connection = PostgreSQLDB.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            System.out.println("inside try block:" );
            System.out.println(resultSet.getFetchSize());

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getInt("age"));
                userList.add(user);

                System.out.println(user);
            }

            request.setAttribute("userList", userList);
            request.getRequestDispatcher("webapp/user-list.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching users");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing.");
            return;
        }

        try (Connection connection = PostgreSQLDB.getConnection()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
        }
    }

    private void addUser(HttpServletRequest request, Connection connection) throws SQLException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String ageString = request.getParameter("age");

        if (firstName == null || lastName == null || ageString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters for adding a user.");
            return;
        }

        int age = Integer.parseInt(ageString);

        String sql = "INSERT INTO users (first_name, last_name, age) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
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
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String ageString = request.getParameter("age");

        if (idString == null || firstName == null || lastName == null || ageString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters for updating a user.");
            return;
        }

        int id = Integer.parseInt(idString);
        int age = Integer.parseInt(ageString);

        String sql = "UPDATE users SET first_name = ?, last_name = ?, age = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

}

