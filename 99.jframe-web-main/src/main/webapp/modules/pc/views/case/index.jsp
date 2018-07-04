<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1>cases</h1>

<ul>
    <c:forEach items="${model.cases.list}" var="item">
        <li data-id="${item.id}">
            <h2><a href="/pc/case/detail/${item.id}">${item.title}</a></h2>
            <p>${item.leadin}</p>
        </li>
    </c:forEach>
</ul>


<div class="pager">
    ${model.cases.render()}

    <%--<ul>--%>
        <%--<c:if test="${model.cases.hasPreviousPage()}">--%>
            <%--<li class="previous"><a href="/pc/case?i=${model.cases.pageIndex-1}">prev</a></li>--%>
        <%--</c:if>--%>


        <%--<c:forEach var="i" begin="0" end="10" step="1">--%>
            <%--<li>--%>
                <%--<a href="/pc/case?i=${model.cases.pageIndex + i}">${model.cases.pageIndex + i + 1}</a>--%>
            <%--</li>--%>
        <%--</c:forEach>--%>

        <%--<c:if test="${model.cases.hasNextPage()}">--%>
            <%--<li class="next"><a href="/pc/case?i=${model.cases.pageIndex + 1}">next</a></li>--%>
        <%--</c:if>--%>
    <%--</ul>--%>
</div>