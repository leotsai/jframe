
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${model.title}</title>
    <meta name="keywords" content="${model.keywords}" />
    <meta name="description" content="${model.description}" />

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
                <a class="btn" target="_blank" href="https://github.com/leotsai/jframe">go to github</a>
            </div>
        </div>
        <div id="mainNav" class="clearfix">
            <div class="inner">
                <ul id="mainMenu" class="menu clearfix">
                    <li id="navHome"><a href="/">home</a></li>
                    <li id="navCase"><a href="/pc/case">cases</a></li>
                    <li id="navAbout"><a href="/pc/home/about">about</a></li>
                </ul>
                <ul id="loginStatus" class="menu clearfix">
                    <li id="navLogin"><a href="/login">login</a></li>
                    <li id="navRegister"><a href="/register">register</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div id="main">
        <div class="inner">
            <c:choose>
                <c:when test="${model.html == null}">
                    <tiles:insertAttribute name="body" />
                </c:when>
                <c:otherwise>
                    ${model.html}
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div id="footer">
        <div class="inner">
            &copy;2018
        </div>
    </div>
</div>
</body>
</html>
