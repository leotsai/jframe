(function() {
    if (window.MultiAjaxCaller) {
        return;
    }
    window.MultiAjaxCaller = function(isOnPc) {
        this._tasks = [];
        this._totalTasks = 0;
        this.singleBusyText = "处理中...";
        this.progressTitle = "正在处理";
        this._currentRequest = null;
        this._isStopped = false;
        this.isOnPc = isOnPc == undefined ? false : isOnPc;
    };

    MultiAjaxCaller.prototype = {
        run: function(tasks) {
            var me = this;
            this._isStopped = false;
            if (tasks.length === 1) {
                this._runSingleTask(tasks[0]);
            } else {
                this._tasks = tasks;
                this._totalTasks = tasks.length;
                var htmlProgresser = '<div class="progress-bar"></div>';
                if (me.isOnPc) {
                    mvcApp.notification.dialog("dialogMultiAjaxCaller", this.progressTitle, {
                        width: 400,
                        height: 'auto',
                        canClose: false,
                        resizable: false,
                        buttons: {
                            "停止": function () {
                                $(this).dialog("close");
                            }
                        },
                        open: function () {
                            $("#dialogMultiAjaxCaller").html(htmlProgresser);
                        },
                        close: function () {
                            me._stop();
                        }
                    });
                } else {
                    mvcApp.notification.dialog("dialogMultiAjaxCaller", this.progressTitle, htmlProgresser, {
                        "停止": function () {
                            me._stop();
                        }
                    });
                }
                me._updateProgressBar(0);
                this._getFirstTaskToRun();
            }
        },
        _stop: function() {
            this._isStopped = true;
            $("#dialogMultiAjaxCaller").remove();
            this._tasks = [];
            if (this._currentRequest != null) {
                this._currentRequest.abort();
                this._currentRequest = null;
            }
            this.onAllCompletedFunc();
        },
        _runSingleTask: function(task) {
            var me = this;
            mvcApp.ajax.busyPost(task.url, task.getPostData(), function () {
                (function (t) {
                    me.onTaskCompleted(t);
                })(task);
                me.onAllCompletedFunc();
            }, this.singleBusyText, true);
        },
        _getFirstTaskToRun: function() {
            var me = this;
            if (me._isStopped) {
                return;
            }
            if (me._tasks.length === 0) {
                me._updateButtonText("完成");
                return;
            }
            var task = me._tasks.splice(0, 1)[0];
            me._currentRequest = mvcApp.ajax.post(task.url, task.getPostData(), function(result) {
                if (result.success) {
                    me._updateProgressBar(me._totalTasks - me._tasks.length);
                    (function(t) {
                        me.onTaskCompleted(t);
                    })(task);
                    me._getFirstTaskToRun();
                } else {
                    mvcApp.notification.alert("出错了", result.Message);
                }
            }, false);
        },
        _updateProgressBar: function(completed) {
            var $bar = $("#dialogMultiAjaxCaller .progress-bar");
            var progress = completed / this._totalTasks;
            var width = progress * 100 + "%";
            var value = (Math.round(progress * 1000) / 10).toFixed(1) + "%";
            var htmlInner = '<div class="progress-covered" style="width:' + width + ';">';
            if (progress > 0.5) {
                htmlInner += '<span>' + value + '</span></div>';
            } else {
                htmlInner += '</div><span>' + value + '</span>';
            }
            $bar.html(htmlInner);
        },
        _updateButtonText: function (text) {
            if (this.isOnPc) {
                $("#dialogMultiAjaxCaller").closest(".ui-dialog").find("span.ui-button-text").html(text);
            } else {
                $("#dialogMultiAjaxCaller .dialog-buttons a").html(text);
            }
        },
        onAllCompletedFunc: function() {

        },
        onTaskCompleted: function(task) {

        }
    };

})();