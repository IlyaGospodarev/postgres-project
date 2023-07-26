<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
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
                <th>First Name</th>
                <th>Last Name</th>
                <th>Age</th>
            </tr>
        </thead>
        <tbody>
            <%-- Loop through the user list provided by the servlet --%>
            <c:forEach items="${userList}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Add New User</h2>
    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="add">
        First Name: <input type="text" name="firstName" required><br>
        Last Name: <input type="text" name="lastName" required><br>
        Age: <input type="number" name="age" required><br>
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
        First Name: <input type="text" name="firstName" required><br>
        Last Name: <input type="text" name="lastName" required><br>
        Age: <input type="number" name="age" required><br>
        <input type="submit" value="Update User">
    </form>
</body>
</html>
