
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/modules/pc/js/account/register.js"></script>
<link href="/modules/pc/css/pages/account-register.css" rel="stylesheet" />
<div class="panel">
    <div class="panel-title">
        ${model.title}
    </div>
    <div class="panel-content">
        <div>
            <img src="/img/W200/189-2017-01-b0dd0a6361bd43eebcc759c447c9ec70.bmp">
        </div>
        <form id="formRegister" method="POST">
            <table class="form">
                <colgroup>
                    <col width="120px"/>
                    <col width="auto" />
                </colgroup>
                <tr>
                    <td class="label">Username：</td>
                    <td>
                        <input type="text" class="txt" id="txtUsername" name="username" placeholder="your email address"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Password：</td>
                    <td>
                        <input type="password" class="txt" name="password"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Avartar：</td>
                    <td>
                        <input type="hidden" value="" name="ImageKey" id="hfImageKey"/>
                        <div class="line-img">
                            <div id="imgUploader"></div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="label">Nickname：</td>
                    <td>
                        <input type="text" class="txt" name="nickname" placeholder="required" value="@Model.Model.NickName"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Age：</td>
                    <td>
                        <input type="text" class="txt" name="age" placeholder="required"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Signature：</td>
                    <td>
                        <textarea class="txt" name="signature" placeholder="required"></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="label">Gender：</td>
                    <td>
                        <input type="radio" name="gender" value="Male" id="cbMale" checked="checked" />
                        <label for="cbMale">Male</label>
                        <input type="radio" name="gender" value="Female" id="cbFemale" />
                        <label for="cbFemale">Female</label>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="checkbox" id="cbAdmin" value="true" name="registerAsAdmin" checked="checked" />
                        <label for="cbAdmin">Register as syetem admin</label>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="checkbox" id="cbAgree" checked="checked"/>
                        <label for="cbAgree">I agree to the <a href="javascript:;">Agreement</a></label>
                    </td>
                </tr>
                <tr>
                    <td class="label"></td>
                    <td>
                        <a href="javascript:;" id="btnRegister" class="btn">Submit</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
