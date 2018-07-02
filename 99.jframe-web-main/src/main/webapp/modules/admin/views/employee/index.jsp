<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="/_storage/js/jquery/jquery.validate.min.js"></script>
<script src="/modules/admin/js/employee/index.js?v=4"></script>
<link rel="stylesheet" href="/modules/admin/css/pages/employee/employee.css" />
<div class="page employee-index">
    <div class="side-tab-list">
        <ul class="tab-headers">
            <li class="selected type-organization" data-type="organization"><a href="javascript:;">组织架构</a></li>
            <c:if test="${canSeeRole}">
                <li class="type-role" data-type="role"><a href="javascript:;">角色设置</a></li>
            </c:if>
        </ul>
        <ul class="tab-contents tab-side-contents">
            <li class="selected">
                <div class="items">
                    <c:if test="${superAdmin}">
                        <div class="items-1"><a href="javascript:;" class="btn-super-manager">超级管理员<span id="superAdminCount" /></a></div>
                    </c:if>
                    <div class="items-2">
                        <h5>五品库（杭州）电子商务有限公司</h5>
                        <ul class="department-list">
                            <c:forEach items="${model.departments}" var="item">
                                <li class="${model.department.id==item.id?'selected':''}"><a data-id="${item.id}" data-name="${item.name}" href="javascript:;">${item.name}<span></span></a></li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="items-3">
                        <c:if test="${canAddDepartment}">
                            <a href="javascript:;" id="btnAddDepartment" class="btn btn-add-department">新建部门</a>
                        </c:if>
                    </div>
                </div>
            </li>
            <li>
                <ul class="role-list">
                    <c:forEach items="${model.roles}" var="role">
                        <li data-id="${role.id}">
                            <a href="javascript:;">${role.name}</a>
                        </li>
                    </c:forEach>
                </ul>
                <c:if test="${canEditRole}">
                    <div class="items-3"><a href="javascript:;" id="btnAddRole" class="btn btn-add-department">添加角色</a></div>
                </c:if>
            </li>
        </ul>
    </div>
    <div class="manage-content">
        <ul class="tab-contents tab-main-contents">
            <li class="selected">
                <div id="">
                    <form id="formSearch"><input type="hidden" name="departmentId" id="departmentId" value="${model.department.id}"></form>
                    <div class="info-cont">
                        <h3><span id="departmentName">${model.department.name}</span></h3>
                        <div class="handle-cont">
                            <c:if test="${canEditDepartment}">
                                <a href="javascript:;" id="updateName" class="right-border">修改名字</a>
                            </c:if>
                            <c:if test="${canDeleteDepartment}">
                                <a href="javascript:;" id="deleteDepartment">删除该部门</a>
                            </c:if>
                        </div>
                    </div>
                    <div class="content-table">
                        <c:if test="${canAddEmployee}">
                            <a href="javascript:;" class="btn add-btn" id="btnAdd"><span>+</span>新增成员</a>
                        </c:if>

                        <table id="grid" class="grid">
                            <colgroup>
                                <col width=""/>
                                <col width="150px"/>
                                <col width="150px"/>
                                <col width="150px"/>
                                <col width="150px"/>
                                <col width="120px"/>
                                <col width=""/>
                                <col width=""/>
                                <col width=""/>
                                <col width=""/>
                            </colgroup>
                            <thead>
                            <tr>
                                <th class="center ">ID</th>
                                <th class="center ">姓名</th>
                                <th class="center">绑定账号</th>
                                <th class="center ">角色</th>
                                <th class="center ">部门</th>
                                <th class="center">职位</th>
                                <th class="center">微信</th>
                                <th class="center">性别</th>
                                <th class="center">生日</th>
                                <th class="center">权限</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr><td colspan="10" class="loading">加载中...</td></tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="pager"></div>
                </div>
            </li>
            <li>
                <div id="roleSetting" class="role-setting"> </div>
            </li>
        </ul>
    </div>
</div>