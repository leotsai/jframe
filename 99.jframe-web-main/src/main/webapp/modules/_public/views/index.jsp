<%@ page import="org.jframe.infrastructure.helpers.DateHelper" %>
<%@ page import="org.jframe.core.extensions.JDate" %>
<%@ page import="java.util.Calendar" %><%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: 2017/1/6
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
<head>
    <meta name="keywords" content="architecture, spring mvc, solution architecture, source code, open source"/>
    <title>${model.title}</title>

    <link href="/_storage/css/jqueryui/base/jquery-ui.css" rel="stylesheet"/>
    <link href="/modules/_public/css/core.css" rel="stylesheet"/>
    <link href="/modules/_public/css/pad.css" rel="stylesheet" media="screen and (max-width:1024px)"/>

    <script src="/_storage/js/jquery/jquery-1.7.2.min.js"></script>
    <script src="/_storage/js/jquery/jquery-ui-1.8.20.min.js"></script>

</head>
<body>
<div id="wrapper">
    <div id="header">
        <div class="inner">
            <a id="logo" href="/">
                jframe.org
            </a>
            <div id="slogan">
                The website for <a target="_blank" href="https://github.com/leotsai/jframe">https://github.com/leotsai/jframe</a>
                <a class="btn" target="_blank" href="https://github.com/leotsai/mvcsolution">go to github</a>
            </div>
        </div>
        <div id="mainNav" class="clearfix">
            <div class="inner">
                <ul id="mainMenu" class="menu clearfix">
                    <li id="navHome"><a href="/demo">Demo Home</a></li>
                </ul>
                <ul id="loginStatus" class="menu clearfix">
                    <li id="navLogin"><a href="/admin/login">Demo Login</a></li>
                    <li id="navRegister"><a href="/demo/register">Demo Register</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div id="main">
        <div class="inner">
            <h1>this is index page</h1>
        </div>
    </div>
    <div id="footer">
        <div class="inner">
            &copy;@(2013 - <%=Calendar.getInstance().get(Calendar.YEAR)%>)
        </div>
    </div>
</div>
</body>
</html>
