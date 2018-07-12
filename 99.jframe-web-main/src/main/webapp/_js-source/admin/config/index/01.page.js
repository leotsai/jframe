(function () {
    var urls = {
        save: "/admin/config/save"
    };
    $(document).ready(function () {
        $(".switch").click(function () {
            if ($(this).hasClass("switch-off")) {
                $(this).removeClass("switch-off");
                $(this).closest("tr").find(".value").text("true");
            } else {
                $(this).addClass("switch-off");
                $(this).closest("tr").find(".value").text("false");
            }
        });

        $(".select").change(function () {
            $(this).closest("tr").find(".value").text($(this).val());
        });

        $(".btn-edit").click(function () {
            var $value = $(this).closest("tr").find(".value");
            if ($(this).hasClass("checkbox")) {
                var csvSource = $(this).closest("tr").attr("data-source");
                showCheckboxEditor($value, csvSource);
                return;
            }
            showEditor($value, $(this).closest("tr").attr("data-type"));
        });

        $("#btnSave").click(function () {
            var $tr = $("tr.data");
            if ($tr.length === 0) {
                return;
            }
            var data = "";
            for (var i = 0; i < $tr.length; i++) {
                data += "&configs[" + i + "].key=" + $($tr[i]).attr("data-key") + "&configs[" + i + "].value=" + $($tr[i]).find(".value").text();
            }

            mvcApp.ajax.busyPost(urls.save, data, function () {
                mvcApp.notification.toastSuccess("保存成功")
            }, "保存中...", true);
        });
    });

    function showCheckboxEditor($value, csvSource) {
        var sources = csvSource.split(",");
        var content = '';
        var values = $($value).text();
        for (var i = 0; i < sources.length; i++) {
            var checked = '';
            var selected = '';
            if (values.indexOf(sources[i]) > -1) {
                checked = 'checked';
                selected = 'selected'
            }
            content += '<div class="edit-checkbox"><input id="cb' + i + '" class="hide" type="checkbox" name="checkbox" ' +
                'value="' + sources[i] + '" ' + checked + ' />' +
                '<label class="checkbox" for="cb' + i + '">' +
                '<span class="cb ' + selected + '">' + sources[i] + '</span></label></div>'
        }
        mvcApp.notification.dialog("editCheckbox", "编辑", {
            width: 400,
            height: 300,
            open: function () {
                $("#editCheckbox").html(content);
                $("#editCheckbox .cb").unbind("click");
                $("#editCheckbox .cb").click(function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        $(this).addClass('selected');
                    }
                })
            },
            buttons: {
                "确定": function () {
                    var $checked = $("#editCheckbox input:checked");
                    if ($checked.length == 0) {
                        mvcApp.notification.alertInfo("至少勾选一个");
                        return;
                    }
                    var newValues = [];
                    for (var i = 0; i < $checked.length; i++) {
                        newValues.push($($checked[i]).val())
                    }
                    $($value).text(JSON.stringify(newValues));
                    $(this).dialog("close");
                },
                "取消": function () {
                    $(this).dialog("close");
                }
            }
        })
    }

    function showEditor($value, type) {
        var values = $($value).text();
        var content = '<textarea style="width: 100%;height: 100%;resize:none">' + values + '</textarea>';
        mvcApp.notification.dialog("editDialog", "编辑", {
            width: 400,
            height: 300,
            open: function () {
                $("#editDialog").html(content)
            },
            buttons: {
                "确定": function () {
                    var newValue = $("#editDialog textarea").val();
                    if (type == 'JSON') {
                        try {
                            JSON.parse(newValue);
                        } catch (e) {
                            mvcApp.notification.alertError("JSON格式不对");
                            return;
                        }
                    }
                    $($value).text(newValue);
                    $(this).dialog("close");
                },
                "取消": function () {
                    $(this).dialog("close");
                }
            }
        })
    }

})();