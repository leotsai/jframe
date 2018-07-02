<%--
  Created by IntelliJ IDEA.
  User: yezi
  Date: 2018/1/19
  Time: 11:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/modules/admin/js/employee/detail.js"></script>
<link rel="stylesheet" href="/modules/admin/css/pages/employee/employee-detail.css" >

<div class="page">
    <input type="hidden" id="hfEmployeeId" value="${model.employee.id}">
    <input type="hidden" id="hfDepartmentId" value="${model.employee.departmentId}">
    <div class="btn-handle">
        <a class="btn btn-back" id="btnBack" href="/admin/employee?departmentId=${model.employee.departmentId}">返回</a>
        <c:if test="${canEditEmployee}">
            <a class="btn" id="btnEdit" href="/admin/employee/edit?employeeId=${model.employee.id}">编辑</a>
        </c:if>
        <c:if test="${canDeleteEmployee}">
            <a class="btn" id="btnDelete" href="javascript:;">删除</a>
        </c:if>
        <a data-username="${model.employee.phone}" class="btn" id="btnLog" href="javascript:;">查看日志</a>
    </div>
    <div class="detail-info">
        <h3><span>${model.employee.name}</span></h3>
        <div class="user-info">
            <h4>成员信息</h4>
            <ul class="user-info-list">
                <li><label><span>*</span>真实姓名</label><span>${model.employee.name}</span></li>
                <li><label>所属部门</label><span>
                    <c:forEach items="${model.departments}" var="item">
                        <c:if test="${item.id==model.employee.departmentId}">${item.name}</c:if>
                    </c:forEach>
                    </span></li>
                <li><label>手机号码</label><span>${model.employee.phone}</span></li>
                <li><label>微信号</label><span>${model.employee.weixin}</span></li>
            </ul>
            <ul class="user-info-list">
                <li><label><span>*</span>职位</label><span>${model.employee.position}</span></li>
                <li><label>性别</label><span>${model.employee.gender.text}</span></li>
                <li><label>生日</label><span>${model.employee.birthday}</span></li>
            </ul>
        </div>
        <div class="employee-info">
            <c:forEach items="${model.rolePermissions}" var="item">
                <h4>角色-${item.key}</h4>
                <ul class="employee-info-list">
                    <c:forEach items="${item.value}" var="it">
                        <li>
                            <div class="name"><span>${it.key}</span></div>
                            <ul class="value-list">
                                <c:forEach items="${it.value}" var="i">
                                    <li><span>${i}</span></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </c:forEach>
        </div>
    </div>
</div>

