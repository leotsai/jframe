(function() {
    var uploader = null;
    $(document).ready(function () {
        $("#navRegister").addClass("selected");

        $("#btnRegister").click(function () {
            if ($(this).hasClass("disabled")) {
                return;
            }
            register();
        });
        $("#cbAgree").click(function() {
            setBtnRegisterStatus();
        });
        initUploader();
    });

    function initUploader(){
        uploader = new WebImageUploader("#imgUploader", []);
        uploader.maxImages = 1;
        uploader.imageType = 189;
        uploader.showTip = false;
        uploader.cropCenter = false;
        uploader.maxLength = 80;
        uploader.size = "W200";
        uploader.init();
    }
    function register() {
        if (!$("#cbAgree").is(":checked")) {
            mvcApp.notification.alert("tip", "You have to agree to the Agreement");
            return;
        }

        var imgs = uploader.getImageKeys();
        if (imgs == null) {
            return;
        }
        if (imgs.length == 0) {
            mvcApp.notification.alertError("Please upload avartar");
            return;
        }
        $("#hfImageKey").val(imgs[0]);

        $("#btnRegister").cssDisable().html("Processing...");
        $("#cbAgree").attr("disabled", "disabled");

        mvcApp.notification.alertInfo("register success")
    }
    function setBtnRegisterStatus()
    {
        if ($("#cbAgree").is(":checked")) {
            $("#btnRegister").cssEnable();
        } else {
            $("#btnRegister").cssDisable();
        }
    }
})();