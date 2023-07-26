<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
</head>
<body>
    <h1>User List</h1>

    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Age</th>
                <th>Surname</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${userList}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.surname}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Add New User</h2>
    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="add">
        Name: <input type="text" name="name" required><br>
        Age: <input type="number" name="age" required><br>
        Surname: <input type="text" name="surname" required><br>
        <input type="submit" value="Add User">
    </form>

    <h2>Delete User</h2>
    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="delete">
        User ID: <input type="number" name="id" required><br>
        <input type="submit" value="Delete User">
    </form>

    <h2>Update User</h2>
    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="update">
        User ID: <input type="number" name="id" required><br>
        Name: <input type="text" name="firstName" required><br>
        Age: <input type="number" name="age" required><br>
        Surname: <input type="text" name="surname" required><br>
        <input type="submit" value="Update User">
    </form>
</body>
</html>
