<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shopping List</title>
    </head>
    <body>
        <h1>Shopping List</h1>
        <p>Hello, ${username} <a href="?action=logout">Logout</a></p>
        <h2>List</h2>
        <form method="POST" action="">
            <label>Add item:</label>
            <input type="text" name="name">
            <input type="hidden" name="action" value="add">
            <input type="submit" value="Add">
        </form>
        <form method="POST" action="">
            <c:choose>
                <c:when test="${empty items}">
                    Your cart is empty.
                </c:when>
                <c:otherwise>
                    <table>
                        <c:forEach items="${items}" var="item">
                            <tr>
                                <td><input type='radio' name='selected' value='${item}'></td>
                                <td>${item}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    <input type="hidden" name="action" value="delete">
                    <input type="submit" value="Delete">
                    <c:if test="${totalPages > 1}">
                        <p>
                            <c:if test="${actualPage > 1}">
                                <a href="?action=page_backward"><<<</a>
                            </c:if>
                            Page ${actualPage} of ${totalPages}
                            <c:if test="${actualPage < totalPages}">
                                <a href="?action=page_forward">>>></a>
                            </c:if>
                        </p>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </form>
    </body>
</html>
