
var createWindow = {
    alert:function (text) {
        var dom = '<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">\n' +
            '  <div class="am-modal-dialog">\n' +
            '    <div class="am-modal-hd">提示</div>\n' +
            '    <div class="am-modal-bd">\n' +
            text +
            '    </div>\n' +
            '    <div class="am-modal-footer">\n' +
            '      <span class="am-modal-btn">确定</span>\n' +
            '    </div>\n' +
            '  </div>\n' +
            '</div>';
        $('body').append(dom);
        $('#my-alert').modal(Option)
    },
    confirm:function (title,text,callback,[]) {
        var dom = "<div class=\"am-modal am-modal-confirm\" tabindex=\"-1\" id=\"my-confirm\">\n" +
            "  <div class=\"am-modal-dialog\">\n" +
            "    <div class=\"am-modal-hd\">"+title+"</div>\n" +
            "    <div class=\"am-modal-bd\">\n" +
            text +
            "    </div>\n" +
            "    <div class=\"am-modal-footer\">\n" +
            "      <span class=\"am-modal-btn\" data-am-modal-cancel>取消</span>\n" +
            "      <span class=\"am-modal-btn\" data-am-modal-confirm>确定</span>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>";
        $('body').append(dom);
        $('#my-confirm').modal({
            relatedTarget: this,
            onConfirm: function() {
                callback();
            },
            onCancel: function() {
                alert('算求，不弄了');
            }
        })
    },
    popup:function (title,content) {
        var dom = "<div class=\"am-popup\" id=\"my-popup\">\n" +
            "  <div class=\"am-popup-inner\">\n" +
            "    <div class=\"am-popup-hd\">\n" +
            "      <h4 class=\"am-popup-title\">"+title+"</h4>\n" +
            "      <span data-am-modal-close\n" +
            "            class=\"am-close\">&times;</span>\n" +
            "    </div>\n" +
            "    <div class=\"am-popup-bd\">\n" +
            content +
            "    </div>\n" +
            "  </div>\n" +
            "</div>";
        $('body').append(dom);
        $('#my-popup').modal(Option);
    }
};