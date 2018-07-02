/**
 * Created by Leo on 2017/1/9.
 */
(function () {
    var urls = {
        doLogin: "/admin/login/doLogin"
    };
    var returnUrl = '';

    $(document).ready(function () {
        $("#navLogin").addClass("selected");
        var $hfReturnUrl = $("#hfReturnUrl");
        if ($hfReturnUrl.length > 0) {
            returnUrl = $hfReturnUrl.val();
        }

        $("#btnLogin").click(function () {
            if ($(this).hasClass("disabled")) {
                return;
            }
            doLogin();
        });
        mvcApp.utils.onEnterKeydown("#txtPassword", function () {
            if ($("#btnLogin").hasClass("disabled")) {
                return;
            }
            doLogin();
        });

        $('.form-group input').focus(function () {
            $(this).closest('.form-group').addClass('focus');
        }).blur(function () {
            if ($(this).val()) {
                $(this).closest('.form-group').addClass('focus');
            } else {
                $(this).closest('.form-group').removeClass('focus');
            }
        });
    });

    function doLogin() {
        var $form = $("#btnLogin").closest("form");
        var $btn = $("#btnLogin");
        mvcApp.ajax.busyPost(urls.doLogin, $form.serialize(), function (result) {
            if (result.success) {
                $btn.html("Login succeeded...");
                window.location = returnUrl === "" ? "/" : returnUrl;
            } else {
                $btn.cssEnable().html("Login");
                mvcApp.notification.alertError(result.message);
            }
        }, "Login...", true);
    }
})();