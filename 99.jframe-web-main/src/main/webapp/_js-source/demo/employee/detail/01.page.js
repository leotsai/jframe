(function () {

    var urls = {
        index:"/demo/employee",
        edit:"/demo/employee/edit",
        delete:"/demo/employee/delete",
        dataLog: function dataLog(username) {
            return "/demo/dataLog?userName=" + username;
        }
    };

    $(document).ready(function () {
        $("#btnDelete").click(function () {
            mvcApp.notification.messageBox("删除成员", "确认删除该成员？", {
                "确定": function () {
                    $(this).dialog("close");
                    mvcApp.ajax.busyPost(urls.delete,"employeeId="+$("#hfEmployeeId").val(),function (data) {
                        if(data.success){
                            window.location = urls.index+"?departmentId="+$("#hfDepartmentId").val();
                        }else {
                            mvcApp.notification.alertInfo(data.message);
                        }
                    },"提交中。。。")
                },
                "取消": function () {
                    $(this).dialog("close");
                }
            });
        });

        $("#btnLog").click(function(){
            var username = $(this).attr("data-username");
            window.location = urls.dataLog(username);
        });
    });

})();