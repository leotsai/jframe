<%--
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
    <meta name="keyword" content="architecture, spring mvc, solution architecture, source code, open source"/>
    <title>${model.title}</title>

    <link href="/_storage/css/jqueryui/base/jquery-ui.css" rel="stylesheet"/>
    <link href="/modules/demo/css/core.css" rel="stylesheet"/>
    <link href="/modules/demo/css/pad.css" rel="stylesheet" media="screen and (max-width:1024px)"/>

    <script src="/_storage/js/jquery/jquery-1.7.2.min.js"></script>
    <script src="/_storage/js/jquery/jquery-ui-1.8.20.min.js"></script>
    <script src="/modules/demo/js/core.js"></script>

</head>
<body>
<div id="wrapper">
    <div id="header">
        <div class="inner">
            <a id="logo" href="/">
                jframe.org
            </a>
            <div id="slogan">
                The demo website for <a target="_blank" href="https://github.com/leotsai/jframe">https://github.com/leotsai/jframe</a>
                <a class="btn" target="_blank" href="https://github.com/leotsai/mvcsolution">go to github</a>
            </div>
            <a id="linkAdmin" class="btn" href="/admin">Admin Control Panel</a>
        </div>
        <div id="mainNav" class="clearfix">
            <div class="inner">
                <ul id="mainMenu" class="menu clearfix">
                    <li id="navHome"><a href="#">Home</a></li>
                    <li id="navDoc"><a href="#">Documentation</a></li>
                    <li id="navSource"><a href="#">Get The Source Code</a></li>
                    <li id="navContact"><a href="#">Contact Me</a></li>
                </ul>
                <ul id="loginStatus" class="menu clearfix">
                    <li id="navLogin"><a href="/login">Login</a></li>
                    <li id="navRegister"><a href="/register">Register</a></li>
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
            &copy;@("2013 - " + DateTime.Now.Year)
        </div>
    </div>
</div>
</body>
</html>
