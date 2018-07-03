<%--
  Created by IntelliJ IDEA.
  User: yezi
  Date: 2018/1/19
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/_storage/js/jquery/jquery.validate.min.js"></script>
<script src="/modules/admin/js/employee/edit.js?v=3"></script>
<link rel="stylesheet" href="/modules/admin/css/pages/employee/employee.css" />

<div class="page">
    <div class="info-cont">
        <a class="btn btnBack" href="javascript:;">返回</a>
        <h3><span>${model.title}</span></h3>
    </div>
    <div class="group-info ${!empty model.employee?"hide":""} phone-number">
        <h4>绑定手机号</h4>
        <label for="bindPhone"><span class="require">*</span>手机号码</label>
        <input class="txt" type="text" id="bindPhone" name="bindPhone" value="" placeholder="请输入绑定账号">
        <a id="matchPhone" class="btn" href="javascript:;">匹配账号</a>
        <span id="messsage" class="message"></span>
    </div>
    <div class="group-info">
        <h4>成员信息</h4>
        <form id="formEmployee" class="editor-form">
            <input type="hidden" id="hfUserId" name="userId" value="${model.employee.userId}">
            <input type="hidden" id="hfEmployeeId" name="id" value="${model.employee.id}">
            <div class="form-group-info">
                <label for="name"><span class="require">*</span>真实姓名</label>
                <input class="txt" type="text" id="name" name="name" value="${model.employee.name}" placeholder="请输入真实姓名" required data-msg="请输入真实姓名">
            </div>
            <div class="form-group-info">
                <label for="departmentId">所属部门</label>
                <select id="departmentId" name="departmentId" class="txt">
                    <c:if test="${empty model.department}">
                        <c:forEach items="${model.departments}" var="item">
                            <option value="${item.id}"  ${model.employee.departmentId==item.id?'selected="selected"':''}>${item.name}</option>
                        </c:forEach>
                    </c:if>
                    <c:if test="${!empty model.department}">
                        <c:forEach items="${model.departments}" var="item">
                            <option value="${item.id}"  ${model.department.id==item.id?'selected="selected"':''}>${item.name}</option>
                        </c:forEach>
                    </c:if>
                </select>
            </div>
            <div class="form-group-info">
                <label for="name"><span class="require">*</span>手机号码</label>
                <span class="phone">${model.employee.phone}</span>
                <input type="text" id="phone" name="phone" value="${model.employee.phone}" class="hide" required data-msg="请先匹配手机号">
            </div>
            <div class="form-group-info">
                <label for="name">职位</label>
                <input class="txt" type="text" id="position" name="position"  value="${model.employee.position}" >
            </div>
            <div class="form-group-info" style="position: inherit;">
                <label for="gender">性别</label>
                <select id="gender" name="gender" class="txt select-wd1">
                    <c:forEach items="${model.genders}" var="item">
                        <option value="${item}" ${model.employee.gender==item?'selected="selected"':''}>${item.text}</option>
                    </c:forEach>
                </select>
                <label for="birthday" class="wd">生日</label>
                <div style="display: inline-block"><input class="txt select-wd date" type="text" id="birthday" name="birthday"  value="${model.employee.birthday}" style="float: none;" ></div>
            </div>
            <div class="form-group-info">
                <label for="weixin">微信号</label>
                <input class="txt" type="text" id="weixin" name="weixin"  value="${model.employee.weixin}" >
            </div>
            <div class="form-group-info wd-auto">
                <label><span class="require">*</span>角色</label>
                <ul class="employee-list">
                    <c:forEach items="${model.systemRoles}" var="item" varStatus="stat">
                        <li>
                            <div class="choose-employee ${stat.last?'p-employee':''}
                                 <c:forEach items="${model.selectedRoles}" var="it">
                                 <c:if test="${item.id eq it.id}"> selected </c:if>
                                </c:forEach>
                                " data-roleId="${item.id}">
                                <span class="cell1">${item.name}</span>
                                <span class="cell2">${item.description}</span>
                                <span class="sys-icon">系统</span>
                            </div>
                        </li>
                    </c:forEach>
                    <c:forEach items="${model.userRoles}" var="item">
                        <li>
                            <div class="choose-employee
                                 <c:forEach items="${model.selectedRoles}" var="it">
                                 <c:if test="${item.id eq it.id}"> selected </c:if>
                                </c:forEach>
                                " data-roleId="${item.id}">
                                <span class="cell1">${item.name}</span>
                                <span class="cell2">${item.description}</span>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="form-group-info wd-auto center">
                <a id="btnSave" href="javascript:" class="btn selected">确认添加并绑定</a>
                <a  href="javascript:" class="btn btnBack">取消</a>
            </div>
        </form>
    </div>


</div>
