<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="/modules/admin/js/config/index.js"></script>
<link rel="stylesheet" href="/modules/admin/css/pages/config/config.css"/>
<div class="page">
    <div class="content">
        <table id="grid" class="grid">
            <colgroup>
                <col width="60px"/>
                <col width="120px"/>
                <col width="200px"/>
                <col width="100px"/>
                <col width="80px"/>
            </colgroup>
            <thead>
            <tr>
                <th class="center rank">序号</th>
                <th class=" ">唯一标识</th>
                <th class="">值</th>
                <th class=" ">备注</th>
                <th class=" ">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${not empty model.configs}">
                <c:forEach items="${model.configs}" var="item" varStatus="stat">
                    <tr class="data" data-key="${item.key}" data-type="${item.type}" data-source="${item.csvSource}">
                        <td class="center">${stat.index+1}</td>
                        <td>${item.key}</td>
                        <td class="value">${item.value}</td>
                        <td>${item.note}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.type=='JSON'}">
                                    <a class="btn-edit json" href="javascript:;">编辑</a>
                                </c:when>
                                <c:when test="${item.type=='BOOLEAN'}">
                                    <a href="javascript:;" class="switch ${item.value?'':'switch-off'}">
                                        <span class="slider"></span>
                                    </a>
                                </c:when>
                                <c:when test="${item.type=='CHECKBOX'}">
                                    <a class="btn-edit checkbox" href="javascript:;">编辑</a>
                                </c:when>
                                <c:when test="${item.type=='SELECT'}">
                                    <select class="txt select">
                                        <c:forEach items="${item.sources}" var="source">
                                            <option ${item.value==source?'selected':''}
                                                    value="${source}">${source}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn-edit" href="javascript:;">编辑</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${empty model.configs}">
                <tr>
                    <td colspan="5" class="empty">没有记录</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <div>
        <a class="btn btn-save" id="btnSave" href="javascript:;">保存</a>
    </div>
</div>