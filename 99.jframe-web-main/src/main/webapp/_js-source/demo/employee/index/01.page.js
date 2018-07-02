(function () {

    var urls = {
        adminIndex: "/demo",
        index: "/demo/employee",
        list: "/demo/employee/list",
        add: "/demo/employee/edit",
        detail: "/demo/employee/detail",
        addDepartment: "/demo/department/save",
        updateDepartment: "/demo/department/update",
        deleteDepartment: "/demo/department/delete",
        getPermissions: function getPermissions(id) {
            return "/demo/roleSetting/getPermissions?id=" + id;
        },
        permissionDict: "/demo/permissionDict",
        superAdmin: "/demo/adminSetting",
        getSuperAdminCount: "/demo/employee/getSuperAdminCount",
        getDepartmentEmployeeCount:"/demo/department/getDepartmentEmployeeCount"

    };
    window.TaskSearchHashQuery = function () {
        HashQuery.constructor.call(this);
        this.type = '';
    };
    TaskSearchHashQuery.constructor = TaskSearchHashQuery;
    TaskSearchHashQuery.prototype = new HashQuery();

    var hashQuery = null;

    var grid = null;
    var criteria = '';

    $(document).ready(function () {
        hashQuery = new TaskSearchHashQuery();
        hashQuery.parseFromLocation();

        $('.department-list>li a').click(function () {
            var $li = $(this).closest('li');
            $li.addClass('selected').siblings().removeClass('selected');
            $('.btn-super-manager').removeClass('selected');
            //加载数据
        });
        $('.role-list>li a').click(function () {
            var $li = $(this).closest('li');
            var id = $li.attr("data-id");
            $li.addClass('selected').siblings().removeClass('selected');
            //加载数据
            loadPermission(id);

        });
        $('.btn-super-manager').click(function () {
            $(this).addClass('selected');
            $('.department-list>li').removeClass('selected');
            //加载数据
            window.location = urls.superAdmin;
        });

        $('.tab-headers > li a').click(function () {
            var $li = $(this).closest('li');
            var type = $li.attr('data-type');
            var index = $li.index();
            $li.addClass('selected').siblings().removeClass('selected');
            $('.tab-side-contents>li').removeClass('selected');
            $('.tab-main-contents>li').removeClass('selected');
            $('.tab-side-contents>li').eq(index).addClass('selected');
            $('.tab-main-contents>li').eq(index).addClass('selected');
            if (index == 1) {
                $('.role-list>li a').eq(0).trigger("click");
            }
            hashQuery.type = type;
            hashQuery.updateLocation();
        });

        if (hashQuery.type) {
            $('.tab-headers > li.type-' + hashQuery.type + ' a').trigger('click');
        }

        $("#btnAdd").click(function () {
            window.location = urls.add+"?departmentId="+$("#departmentId").val();
        });
        $("#deleteDepartment").click(function () {
            mvcApp.notification.messageBox("删除部门", "确认删除该部门？", {
                "确定": function () {

                    mvcApp.ajax.busyPost(urls.deleteDepartment, "id=" + $("#departmentId").val(), function (data) {
                        if (data.success) {
                            window.location = urls.index;
                        } else {
                            mvcApp.notification.alertInfo(data.message);
                        }
                    }, "提交中。。。")
                    $(this).dialog("close");
                },
                "取消": function () {
                    $(this).dialog("close");
                }
            });
        });
        $("#updateName").click(function () {
            mvcApp.notification.dialog("updateNameDialog", "编辑部门", {
                width: 400,
                height: 200,
                resizable: false,
                draggable: false,
                buttons: {
                    "取消": function () {
                        $(this).dialog("close");
                    },
                    "确认": function () {
                        var name = $("#updateNameDialog .name").val();
                        if (name == null || name == '') {
                            mvcApp.notification.alert("提示", "部门名称不能为空");
                            return;
                        }
                        mvcApp.ajax.busyPost(urls.updateDepartment, "id=" + $("#departmentId").val() + "&name=" + name, function (data) {
                            if (data.success) {
                                window.location = urls.index + "?departmentId=" + $("#departmentId").val();
                            }
                        }, "提交中。。。")
                    }
                },
                open: function () {
                    var html = '<br><br><div class="verify">'
                        + '<label><span class="required">*</span>部门名称：</label>'
                        + '<input type="text" id="name" value="'+$("#departmentName").html()+'" class="name" placeholder="必填">'
                        + '</div>';
                    $("#updateNameDialog").html(html);
                }
            });
        });

        $("#btnAddRole").click(function () {
            $('.role-list>li').removeClass('selected');
            loadPermission(0);
        });
        initSuperAdminCount();

        grid = new Grid("#grid tbody", ".pager");
        grid.url = urls.list;
        grid.getDataFunc = function () {
            return criteria;
        };
        criteria = $("#formSearch").serialize();
        grid.init();
        grid.pageTo(0);

        $(".department-list li a").each(function () {
            $(this).click(function () {
                $("#departmentId").val($(this).attr("data-id"));
                $("#departmentName").html($(this).attr("data-name"));
                criteria = $("#formSearch").serialize();
                grid.pageTo(0);
            });
        });

        $("#btnAddDepartment").click(function () {
            mvcApp.notification.dialog("addDepartmentDialog", "新建部门", {
                width: 400,
                height: 200,
                resizable: false,
                draggable: false,
                buttons: {
                    "取消": function () {
                        $(this).dialog("close");
                    },
                    "确认": function () {
                        var name = $("#addDepartmentDialog .name").val();
                        if (name == null || name == '') {
                            mvcApp.notification.alert("提示", "部门名称不能为空");
                            return;
                        }
                        mvcApp.ajax.busyPost(urls.addDepartment, $("#addDepartmentForm").serialize(), function (data) {
                            if (data.success) {
                                $(this).dialog("close");
                                window.location = urls.index;
                            }
                        }, "提交中。。。")
                    }
                },
                open: function () {
                    var html = '<br><br><div class="verify">'
                        + '<form id="addDepartmentForm">'
                        + '<label><span class="required">*</span>部门名称：</label>'
                        + '<input type="text" id="name" name="name" class="name" placeholder="必填">'
                        + '</form>'
                        + '</div>';
                    $("#addDepartmentDialog").html(html);
                }
            });
        });
        initDepartmentEmployeeCount();
        

    });
    
    function initDepartmentEmployeeCount() {
        $(".department-list li a").each(function () {
            var $a = $(this);
            mvcApp.ajax.post(urls.getDepartmentEmployeeCount+"?departmentId="+$(this).attr("data-id"), null, function (result) {
                if (result.value > 0) {
                    $a.find("span").html('（' + result.value + '）');
                }else{
                    $a.find("span").html('（0）');
                }
            });
        });
    }

    function initSuperAdminCount() {
        mvcApp.ajax.post(urls.getSuperAdminCount, null, function (result) {
            if (result.value > 0) {
                $("#superAdminCount").text('（' + result.value + '）');
            }
        });
    }

    // 角色设置
    function loadPermission(id) {
        mvcApp.ajax.load("#roleSetting", urls.getPermissions(id), function () {
            mvcApp.utils.bindValidate('#formRoleEditor', null);
            $('.part-list>li a').click(function () {
                var $li = $(this).closest('li');
                var $parentPart = $(this).closest('.permission-part');
                var length1 = $parentPart.find('.part-list>li').length;
                if ($li.hasClass('selected') === true) {
                    $li.removeClass('selected');
                    $parentPart.find('.part-all').removeClass('selected');
                } else {
                    $li.addClass('selected');
                    var length2 = $parentPart.find('.part-list>li.selected').length;
                    if (length1 === length2) {
                        $parentPart.find('.part-all').addClass('selected');
                    }
                }
            });

            $('.part-all a').click(function () {
                var $parentAll = $(this).closest('.part-all');
                var $parentPart = $(this).closest('.permission-part');
                if ($parentAll.hasClass('selected') === true) {
                    $parentAll.removeClass('selected');
                    $parentPart.find('.part-list li').removeClass('selected');
                } else {
                    $parentAll.addClass('selected');
                    $parentPart.find('.part-list li').addClass('selected');
                }
            });

            $("#btnUpdateRole").click(function () {
                // var id = $("#hfRoleId").val();
                // var name = $("#hfName").val();
                // var description = $("#hfDescription").val();
                page.editRole();
            });
            $("#btnSavePermission").click(function () {
                page.savePermission();
            });
            $(".btn-permission").click(function () {
                location.href = urls.permissionDict;
            });
            $("#btnCancel").click(function () {
                location.reload();
            });
            // page.loadDictCount();
        });
    }

})();