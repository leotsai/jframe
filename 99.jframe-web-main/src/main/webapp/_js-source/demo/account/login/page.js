/**
 * Created by Leo on 2017/1/9.
 */
(function() {
    var urls = {
        login: "/login"
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
    });

    function doLogin() {
        var $form = $("#btnLogin").closest("form");
        var $btn = $("#btnLogin");
        $btn.cssDisable().html("Login...");
        mvcApp.ajax.post(urls.login, $form.serialize(), function (result) {
            if (result.success) {
                $btn.html("Login succeeded...");
                window.location = returnUrl === "" ? "/admin" : returnUrl;
            } else {
                $btn.cssEnable().html("Login");
                mvcApp.notification.alertError(result.message);
            }
        });
    }
})();