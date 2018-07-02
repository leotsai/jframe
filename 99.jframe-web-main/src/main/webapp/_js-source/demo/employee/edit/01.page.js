(function () {

    var urls = {
        matchPhone: "/demo/employee/matchPhone",
        save: "/demo/employee/save",
        index: "/demo/employee"
    };

    $(document).ready(function () {

        mvcApp.utils.bindDateTimePicker('birthday', 'off');


        $('#bindPhone').keyup(function () {
            $("#matchPhone").removeClass('btn-success').text('匹配账号');
        });

        $("#matchPhone").click(function () {
            if ($(this).hasClass('btn-success') === true) {
                return;
            }
            var bindPhone = $("#bindPhone").val();
            if (bindPhone == '' || bindPhone.length != 11) {
                mvcApp.notification.alertInfo("请输入正确的手机号");
                return;
            }
            mvcApp.ajax.post(urls.matchPhone, "phone=" + bindPhone, function (data) {
                if (data.success) {
                    if (data.value == null) {
                        $("#messsage").text("该账号不存在，请到五品库官方商城注册账号后再进行绑定手机号");
                    } else {
                        $("#hfUserId").val(data.value.id);
                        $("#phone").val(data.value.username);
                        $(".phone").text(data.value.username);
                        $("#name").val(data.value.trueName);
                        $("#messsage").html("");
                        $('#phone').valid();
                        $("#matchPhone").addClass('btn-success').text('');
                    }
                } else {
                    $("#messsage").html(data.message);
                }
            });
        });

        $(".btnBack").click(function () {
            window.location = urls.index + "?departmentId=" + $("#departmentId").val();
        });

        $('.choose-employee').click(function () {
            if ($(this).hasClass('selected') === true) {
                $(this).removeClass('selected');
            } else {
                $(this).addClass('selected');
            }
        });

        $("#btnSave").click(function () {
            if ($('#formEmployee').valid() === false) {
                return;
            }
            if ($("#departmentId").val() == null) {
                mvcApp.notification.alertInfo("请先添加部门");
                return;
            }
            if ($("#name").val().length > 10) {
                mvcApp.notification.alertInfo("真实姓名长度不能超过10");
                return;
            }
            if ($("#weixin").val().length > 100) {
                mvcApp.notification.alertInfo("微信号长度超过限制");
                return;
            }
            var cvsRoleIds = "";
            $(".choose-employee").each(function () {
                if ($(this).hasClass('selected')) {
                    cvsRoleIds += $(this).attr('data-roleId') + ",";
                }
            });
            if (cvsRoleIds == "" || cvsRoleIds.length == 0) {
                mvcApp.notification.alertInfo("请选择角色");
                return;
            }
            mvcApp.ajax.busyPost(urls.save, $("#formEmployee").serialize() + "&cvsRoleIds=" + cvsRoleIds, function () {
                mvcApp.notification.toastSuccess("用户授权成功");
                window.location = urls.index + "?departmentId=" + $("#departmentId").val();
            }, "保存中...", true);
        });

        mvcApp.utils.bindValidate('#formEmployee');
    });

})();