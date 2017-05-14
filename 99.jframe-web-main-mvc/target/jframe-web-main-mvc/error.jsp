<%--
  Created by IntelliJ IDEA.
  User: leo
  Date: 2017-05-13
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<h1>error page handler2</h1>

        <h1>request.getRequestURI: <%=  request.getRequestURI() %></h1>
        <%
            throw new Exception("dd");
        %>
</body>
</html>
