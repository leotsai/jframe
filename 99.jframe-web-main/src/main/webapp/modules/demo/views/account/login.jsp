
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/modules/demo/js/account/login.js"></script>
<input type="hidden" id="hfReturnUrl" value="${model.value}"/>
<div class="panel">
    <div class="panel-title">
        ${model.title}
    </div>
    <div class="panel-content">
        <form id="formLogin" method="POST">
            <table class="form">
                <colgroup>
                    <col width="120px"/>
                    <col width="auto" />
                </colgroup>
                <tr>
                    <td class="label">Username：</td>
                    <td>
                        <input type="text" class="txt" name="username" placeholder="your email address"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Password：</td>
                    <td>
                        <input type="password" id="txtPassword" class="txt" name="password"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"></td>
                    <td>
                        <a href="javascript:" id="btnLogin" class="btn">Login</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
