<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="formRoleEditor">
    <div class="info-cont">
        <div class="top-title ${empty model.id ?'hide':''}">
            <h3><span>${model.name}</span></h3>
            <div class="note"><span>备注：${model.description}</span></div>
            <c:if test="${canEditRole}">
                <div id="btnEditRole"><a id="btnUpdateRole" href="javascript:;">修改</a></div>
            </c:if>
        </div>

        <div id="editRole" class="edit-role ${not empty model.id ?'hide':''}">
            <div>
                <label><span class="required">*</span>角色名称</label>
                <input class="txt" type="text" name="name" maxlength="50" value="${model.name}" required data-msg="请填写角色名称"/>
            </div>
            <div class="group1">
                <label>备注</label>
                <textarea name="description" maxlength="150" cols="60" rows="10" placeholder="您可以对该角色进行工作范畴描述(150字符以内)">${model.description}</textarea>
            </div>
        </div>

        <input type="hidden" id="hfRoleId" name="id" value="${model.id}"/>
        <input type="hidden" id="hfCsvCodes" name="csvCodes"/>
        <div class="handle-btn">
            <c:if test="${canSeeDict}">
                <a href="javascript:;" class="btn-permission">权限字典<span id="dictCount"></span></a>
            </c:if>
        </div>
    </div>
    <div class="role-setting-box">
        <h5>权限设置</h5>
        <c:if test="${not empty model.allPermissions}">
            <c:forEach items="${model.allPermissions}" var="item">
                <ul class="permission-list">
                    <li>
                        <div class="permission-part">
                            <div class="part-all ${item.selected ? 'selected':'' } ">
                                <h6><span>${item.text}</span></h6>
                                <a href="javascript:;">全选</a>
                            </div>
                            <c:forEach items="${item.children}" var="subItem">
                                <ul class="part-list">
                                    <c:forEach items="${subItem.children}" var="child">
                                        <li ${child.selected ? 'class=selected':'' } data-csvCodes="${child.value}">
                                            <a href="javascript:;">${child.text}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:forEach>
                        </div>
                    </li>
                </ul>
            </c:forEach>
            <c:if test="${canEditRole}">
                <div class="btn-handle">
                    <a href="javascript:;" id="btnSavePermission" class="btn selected">${empty model.id ?'确认添加  ':'确认修改'}</a>
                    <c:if test="${empty model.id}">
                        <a href="javascript:;" id="btnCancel" class="btn">取消</a>
                    </c:if>
                </div>
            </c:if>
        </c:if>
        <c:if test="${empty model}">
            <div class="empty">没有记录</div>
        </c:if>
    </div>
</form>