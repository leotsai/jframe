(function () {

    var urls = {
        getDictCount: "/admin/permissionDict/getDictCount",
        saveRole: "/admin/roleSetting/saveRole",
        savePermission: "/admin/roleSetting/savePermission"
    };
    var csvCodes = [];

    page.editRole = function () {
        $(".note").addClass("hide");
        $("#editRole").removeClass("hide");
        $("#btnEditRole").addClass("hide");
    };

    page.savePermission = function () {
        if ($('#formRoleEditor').valid() === false) {
            mvcApp.notification.toast("请填写角色名称");
            return;
        }
        csvCodes = calculateCsvCodes();
        if(csvCodes == "" || csvCodes.length == 0){
            mvcApp.notification.toast("请为该角色选择权限");
            return;
        }
        $("#hfCsvCodes").val(csvCodes);
        var data = $("#formRoleEditor").serialize();
        mvcApp.ajax.busyPost(urls.savePermission, data, function () {
            mvcApp.notification.toastSuccess("保存成功");
            location.reload();
        }, "保存中...", true);
    };

    function calculateCsvCodes() {
        var csvCodes = [];
        $('.part-list li').each(function () {
            if ($(this).hasClass('selected')) {
                csvCodes.push($(this).attr('data-csvCodes'));
            }
        });
        console.log(csvCodes.join(','));
        return csvCodes.join(',');
    }

    page.loadDictCount = function () {
        mvcApp.ajax.post(urls.getDictCount, null, function (result) {
            if (result.value > 0) {
                $("#dictCount").text('（' + result.value + '）');
            }
        })
    }

})();