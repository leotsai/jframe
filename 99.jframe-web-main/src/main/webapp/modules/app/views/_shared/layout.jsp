<%--
  Created by IntelliJ IDEA.
  User: leo
  Date: 2017-09-13
  Time: 8:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no,minimal-ui" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />

    <title>${model.title}</title>
    <link href="/modules/app/css/core.css?v=2" rel="stylesheet" />
    <script src="/_storage/js/jquery/jquery-1.7.2.min.js"></script>
    <script src="/res/js-configs"></script>
    <script src="/modules/app/js/core.js?v=9"></script>
    <script>
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "https://hm.baidu.com/hm.js?2a7820db25d7eca62abc85a06af76f84";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
</head>
<body>
<div id="body" class="">
    <tiles:insertAttribute name="body"/>
</div>
</body>
</html>

