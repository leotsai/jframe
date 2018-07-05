<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: 2017/9/25
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<link href="/modules/app/css/pages/app-message.css" rel="stylesheet" />
<div class="page">
    <div class="header">
        <a href="javascript:void(0);" class="btn-back"></a>
        ${model.title}
    </div>
    <div class="content inner">
        <div class="line-icon">
            <span class="icon-${model.icon}"></span>
        </div>
        <div class="line-title">${model.title}</div>
        <div class="line-message">
            ${model.error}
        </div>
        <div class="line-btn">
            <a class="btn" href="${model.buttonUrl}">
            ${model.buttonText}
            </a>
        </div>
    </div>
</div>

