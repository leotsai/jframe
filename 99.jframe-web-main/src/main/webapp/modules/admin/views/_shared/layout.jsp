<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: 2017/1/6
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${model.title}</title>
    <link href="/_storage/css/jqueryui/base/jquery-ui.css" rel="stylesheet"/>
    <link href="/_storage/css/jqueryui/base/calendar.css?v=1" rel="stylesheet"/>
    <link href="/modules/admin/css/core.css" rel="stylesheet"/>
    <link href="/modules/admin/css/pad.css" rel="stylesheet" media="screen and (max-width:1024px)"/>

    <script src="/_storage/js/jquery/jquery-1.7.2.min.js"></script>
    <script src="/_storage/js/jquery/jquery-ui-1.8.20.min.js"></script>
    <script src="/_storage/js/jquery/calendar.js"></script>

    <script src="/res/js-configs"></script>
    <script src="/modules/admin/js/core.js"></script>
</head>
<body>
<div id="wrapper">
    <div class="header">
        <a href="javascript:;" id="logo"></a>
        <ul id="utils">

            <c:if test="${not empty model.topMenu}">
                <c:forEach items="${model.topMenu}" var="item">
                    <li id="nav-${item.id}" class="${item.id == model.currentPage.parent.parent.id?'selected':''}">
                        <a href="${item.url}">
                            <span>${item.text}</span>
                        </a>
                    </li>
                </c:forEach>
            </c:if>

        </ul>
        <div id="quit">
            <a href="/logout">退 出</a>
        </div>
    </div>
    <div id="side" class="side">
        <div class="side-list">
            <ul id="sidebar">
                <c:if test="${not empty model.leftMenu}">
                    <c:forEach items="${model.leftMenu}" var="item">
                        <li id="nav-${item.id}" class="${item.id == model.currentPage.parent.id?'selected':''}">
                            <a href="${item.url}">${item.text}
                                <c:if test="${not empty item.children}">
                                    <span class="arrow"></span>
                                </c:if>
                            </a>
                            <ul class="nav-sub-items">
                                <c:if test="${not empty item.children}">
                                    <c:forEach items="${item.children}" var="subItem">
                                        <li class="${subItem.id == model.currentPage.id?'selected':''}">
                                            <a href="${not empty subItem.url?subItem.url:'javascript:;'}">${subItem.text}</a>
                                        </li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </li>
                    </c:forEach>
                </c:if>
            </ul>
        </div>
        <a id="btnToggle" href="javascript:;"><i class="icon-arrow"></i></a>
    </div>
    <div id="topBar">
        <div class="top-content">
            <h2>${model.title}</h2>
            <span class="currentTime"></span>
        </div>
    </div>

    <div id="main">
        <div class="main-w1">
            <div class="main-w2">
                <div class="body-content"><tiles:insertAttribute name="body"/></div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
