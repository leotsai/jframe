<%--
  Created by IntelliJ IDEA.
  User: yezi
  Date: 2018/1/16
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${!empty model.list}">
    <c:forEach items="${model.list}" var="item">
        <tr data-id="${item.id}">
            <td class="center">${item.rank}</td>
            <td class="center">${item.name}</td>
            <td class="center ">${item.phone}</td>
            <td class="center">
                <c:forEach items="${item.roles}" var="it">
                    ${it.name eq "ADMIN.SUPER_ADMIN_ROLE"?"超级管理员":it.name}&nbsp
                </c:forEach>
            </td>
            <td class="center">${item.departmentName}</td>
            <td class="center">${item.position}</td>
            <td class="center">${item.weixin}</td>
            <td class="center">${item.gender.text}</td>
            <td class="center">${item.birthday}</td>
            <td class="center">
                <a class="btn-edit" href="/admin/employee/detail?employeeId=${item.id}">查看</a>
            </td>
        </tr>

    </c:forEach>

</c:if>

<c:if test="${empty model.list}">
    <tr>
        <td colspan="10" class="empty">没有记录</td>
    </tr>
</c:if>

${model.renderPager()}


