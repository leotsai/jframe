(function() {
    if (window.Slider) {
        return;
    }
    window.Slider = function (itemsSelector, btnLeftSelector, btnRightSelector) {
        this.itemWidth = 80;
        this.itemsSelector = itemsSelector;
        this.btnLeftSelector = btnLeftSelector;
        this.btnRightSelector = btnRightSelector;
        this._isMoving = false;
        this.isInitialized = false;
        this.initWithAnimation = false;
    };

    Slider.prototype = {
        init: function() {
            var me = this;
            var $btnLeft = $(me.btnLeftSelector);
            var $btnRight = $(me.btnRightSelector);
            me.setItemAndWrapperWidth();
            $btnLeft.click(function() {
                if ($(this).hasClass("disabled")) {
                    return;
                }
                me.go(-1);
            });
            $btnRight.click(function () {
                if ($(this).hasClass("disabled")) {
                    return;
                }
                me.go(1);
            });
            me.slideToHiddenSelectedIfNecessary(function() {
                me.setNavButtonStatus(me.getCurrentLeft());
                me.isInitialized = true;
            });
        },
        setItemAndWrapperWidth: function() {
            var $wrapper = $(this.itemsSelector);
            var $childItems = this.getWrapperChildren();
            var itemsCount = $childItems.length;
            $childItems.width(this.itemWidth + "px");
            $wrapper.width(itemsCount * this.itemWidth + "px");
        },
        getWrapperChildren: function() {
            return $(this.itemsSelector).children();
        },
        getWrapperContainer: function() {
            return $(this.itemsSelector).parent();
        },
        go: function (step) {
            if (this._isMoving) {
                return;
            }
            var $wrapper = $(this.itemsSelector);
            var $container = this.getWrapperContainer();
            var containerWidth = $container.width();
            var movingItems = Math.floor(containerWidth / this.itemWidth);
            var movingWidth = movingItems * this.itemWidth;
            var left = this.getCurrentLeft();
            left = left - step * movingWidth;
            if (left > 0) {
                left = 0;
            }
            var min = containerWidth - $wrapper.width();
            if (left < min) {
                left = min;
            }
            this.move(left);
            this.setNavButtonStatus(left);
        },
        getCurrentLeft: function() {
            return $(this.itemsSelector).position().left;
        },
        move: function(left, callback) {
            if (this._isMoving) {
                return;
            }
            if (this.isInitialized == false && this.initWithAnimation == false) {
                $(this.itemsSelector).css("left", left + "px");
                callback && callback();
            } else {
                var me = this;
                this._isMoving = true;
                $(this.itemsSelector).animate({
                    left: left
                }, 500, function () {
                    me._isMoving = false;
                    callback && callback();
                });
            }
        },
        setNavButtonStatus: function (left) {
            var $btnLeft = $(this.btnLeftSelector);
            var $btnRight = $(this.btnRightSelector);
            var $wrapper = $(this.itemsSelector);
            var $container = this.getWrapperContainer();
            var min = $container.outerWidth() - $wrapper.width();
            if (left >= 0) {
                $btnLeft.cssDisable();
            } else {
                $btnLeft.cssEnable();
            }
            if (left <= min) {
                $btnRight.cssDisable();
            } else {
                $btnRight.cssEnable();
            }
        },
        slideToHiddenSelectedIfNecessary: function(callback) {
            if (this.isSelectedVisible()) {
                callback && callback();
                return;
            }
            this.slideToSelected(callback);
        },
        isSelectedVisible: function() {
            var $selectedItem = this.getWrapperChildren().find(".selected");
            if ($selectedItem.length === 0) {
                $selectedItem = $(this.itemsSelector).children(".selected");
            }
            if ($selectedItem.length === 0) {
                return false;
            }
            var $container = this.getWrapperContainer();
            var left = this.getCurrentLeft();
            var min = -$selectedItem.index() * this.itemWidth;
            var max = min + $container.width() - this.itemWidth;
            return left >= min && left <= max;
        },
        slideToSelected: function(callback) {
            var me = this;
            var $selectedItem = this.getWrapperChildren().find(".selected");
            if ($selectedItem.length === 0) {
                $selectedItem = $(this.itemsSelector).children(".selected");
            }
            if ($selectedItem.length === 0) {
                callback && callback();
                return;
            }
            var left = -$selectedItem.index() * this.itemWidth;
            var containerWidth = this.getWrapperContainer().width();
            var itemsWidth = $(this.itemsSelector).width();

            var min = containerWidth - itemsWidth;
            if (left < min) {
                left = min;
            }

            me.move(left, function() {
                me.setNavButtonStatus(left);
                callback && callback();
            });
        }
    };
})();
(function() {
    if (window.ImageZoomer) {
        return;
    }
    window.ImageZoomer = function(wrapperSelector, zoomSelector) {
        this.$wrap = $(wrapperSelector);
        this.$zoom = $(zoomSelector);

        this.$img = null;
        this.fullWidth = 0;
        this.fullHeight = 0;
        this.rate = 0;
        this.bestRate = 0;
    };

    ImageZoomer.prototype = {
        init: function() {
            var me = this;
            me.$zoom.click(function() {
                if (me.rate == me.bestRate) {
                    me.zoomToRate(1);
                } else {
                    me.zoomToRate(me.bestRate);
                }
                me.centerImage();
            });
            me.enableWheelZoom();
        },
        loadImage: function(url) {
            var me = this;
            me.$img = null;
            me.$wrap.html("<div class='loading'>加载中...</div>");
            $("<img/>").attr("src", url).load(function () {
                me.$img = $(this);
                me.fullWidth = this.width;
                me.fullHeight = this.height;
                me.$wrap.empty().append(me.$img);
                me.zoomToBest();
                me.enableDrag();
            });
        },
        zoomToBest: function() {
            var rate = Math.max(this.fullWidth / this.$wrap.width(), this.fullHeight / this.$wrap.height());
            if (rate < 1) {
                rate = 1;
            }
            rate = 1 / rate;
            this.bestRate = rate;
            this.zoomToRate(rate);
            this.centerImage();
        },
        zoomToRate: function(rate) {
            if (this.$img == null) {
                return;
            }
            this.$img.width(this.fullWidth * rate + "px");
            this.$img.height(this.fullHeight * rate + "px");
            this.rate = rate;
            if (rate == this.bestRate) {
                this.$zoom.addClass("best");
            } else {
                this.$zoom.removeClass("best");
            }
        },
        centerImage: function() {
            if (this.$img == null) {
                return;
            }
            this.$img.css("top", (this.$wrap.height() - this.$img.height()) / 2 + "px");
            this.$img.css("left", (this.$wrap.width() - this.$img.width()) / 2 + "px");
        },
        enableDrag: function() {
            var me = this;
            var isMoving = false;
            var lastX = null;
            var lastY = null;
            me.$img.removeClass("moving");
            me.$img.mousedown(function (e) {
                isMoving = true;
                lastX = e.clientX;
                lastY = e.clientY;
                me.$img.addClass("moving");
            });
            $(document).mouseup(function () {
                isMoving = false;
                me.$img.removeClass("moving");
            });
            $(document).mousemove(function (e) {
                if (!isMoving) {
                    return;
                }
                e.preventDefault();
                var position = me.$img.position();
                var newLeft = position.left + (e.clientX - lastX);
                var newTop = position.top + (e.clientY - lastY);

                var containerWidth = me.$wrap.width();
                var containerHeight = me.$wrap.height();
                var width = me.$img.width();
                var height = me.$img.height();

                var minLeft = 0;
                var maxLeft = 0;
                var minTop = 0;
                var maxTop = 0;

                if (containerWidth >= width) {
                    minLeft = 0;
                    maxLeft = containerWidth - width;
                } else {
                    minLeft = containerWidth - width;
                    maxLeft = 0;
                }
                newLeft = Math.max(newLeft, minLeft);
                newLeft = Math.min(newLeft, maxLeft);
                
                if (containerHeight >= height) {
                    minTop = 0;
                    maxTop = containerHeight - height;
                } else {
                    minTop = containerHeight - height;
                    maxTop = 0;
                }
                newTop = Math.max(newTop, minTop);
                newTop = Math.min(newTop, maxTop);

                me.$img.css("left", newLeft + "px");
                me.$img.css("top", newTop + "px");
                lastX = e.clientX;
                lastY = e.clientY;
            });
        },
        enableWheelZoom: function() {
            var me = this;
            me.$wrap.bind("mousewheel", function (e) {
                e.preventDefault();
                if (me.$img == null) {
                    return;
                }
                var step = (-e.originalEvent.deltaY || e.originalEvent.wheelDelta) / 20;
                var newRate = me.rate + me.rate * step / 100;
                if (newRate < me.bestRate) {
                    newRate = me.bestRate;
                    if (me.rate == me.bestRate) {
                        return;
                    }
                }
                me.zoomToRate(newRate);
                me.centerImage();
            });
        },
        dispose: function() {
            $(document).unbind("mousemove").unbind("mouseup");
        }
    };

})();
(function() {
    if (window.ImageViewer) {
        return;
    }
    
    window.ImageViewer = function() {
        this.margins = {
            left: 20,
            right: 40,
            top: 20,
            bottom: 20
        };
        this.thumbnails = [];

        this.imageZoomer = null;
        this.slider = null;
    };

    ImageViewer.prototype = {
        show: function (thumbnails, index) {
            var me = this;
            me.thumbnails = thumbnails;

            me._renderFrame();
            this._resize();
            this.renderThumbnails(index);
            this.showImage();

            $("#imageViewer a.close").click(function() {
                me.close();
            });
            $("#imageViewer a.btn-left").click(function () {
                var $li = $("#imageViewer .thumbnails li.selected").prev();
                if ($li.length == 0) {
                    $li = $("#imageViewer .thumbnails li:last");
                }
                $("#imageViewer .thumbnails li").removeClass("selected");
                $li.addClass("selected");
                me.showImage();
            });
            $("#imageViewer a.btn-right").click(function () {
                var $li = $("#imageViewer .thumbnails li.selected").next();
                if ($li.length == 0) {
                    $li = $("#imageViewer .thumbnails li:first");
                }
                $("#imageViewer .thumbnails li").removeClass("selected");
                $li.addClass("selected");
                me.showImage();
            });
            $(document).keyup(function (e) {
                if (e.keyCode == 27) {
                    me.close();
                }
            });
        },
        close: function() {
            $("#imageViewer").remove();
            if (this.imageZoomer != null) {
                this.imageZoomer.dispose();
            }
            $(document).unbind("keyup");
        },
        showImage: function() {
            var me = this;
            var index = $("#imageViewer .thumbnails li.selected").index();

            if (this.imageZoomer != null) {
                this.imageZoomer.dispose();
            }
            this.imageZoomer = new ImageZoomer("#imageViewer .image", "#imageViewer .zoom");
            this.imageZoomer.init();
            this.imageZoomer.loadImage(this.getFullUrl(me.thumbnails[index]));
            this.slider.slideToHiddenSelectedIfNecessary();
        },
        getFullUrl: function(src) {
            return src.replace(/s50x50/g, 'full');
        },
        renderThumbnails: function(selectedIndex) {
            var me = this;
            var $ul = $("#imageViewer .thumbnails > ul").empty();
            for (var i = 0; i < this.thumbnails.length; i++) {
                var src = this.thumbnails[i];
                var $li = $('<li><a href="javascript:void(0);"><img src="' + src + '"/></a></li>');
                $ul.append($li);
                if (selectedIndex == i) {
                    $li.addClass("selected");
                }
            }
            if ($ul.find("li.selected").length == 0) {
                $ul.children().first().addClass("selected");
            }

            me.slider = new Slider($ul, "#imageViewer .nav-left", "#imageViewer .nav-right");
            me.slider.itemWidth = 100;
            me.slider.init();

            $("#imageViewer .thumbnails li").click(function () {
                $(this).siblings().removeClass("selected");
                $(this).addClass("selected");
                me.showImage();
            });
        },
        _renderFrame: function() {
            var html = '\
<div id="imageViewer">\
    <div class="main-content">\
        <a class="close"></a>\
        <div class="viewer-main">\
            <div class="img-wrapper">\
                <a class="btn-left"></a>\
                <a class="btn-right"></a>\
                <div class="image"></div>\
                <div class="info"></div>\
                <a class="zoom" href="javascript:void(0);"></a>\
            </div>\
            <div class="bottom-nav">\
                <a class="nav-left"></a>\
                <a class="nav-right"></a>\
                <div class="thumbnails">\
                    <ul></ul>    \
                </div>\
            </div>\
        </div>    \
    </div>\
</div>';
            $("body").append(html);
        },
        _resize: function() {
            var $content = $("#imageViewer .main-content");
            var height = $(window).height() - this.margins.top - this.margins.bottom;
            $content.css("left", "20px").css("top", "20px")
                .width($(window).width() - this.margins.left - this.margins.right + "px")
                .height(height + "px");
            var $bottonNav = $("#imageViewer .bottom-nav");
            var $imgWrapper = $("#imageViewer .img-wrapper");
            $bottonNav.height("80px");
            $imgWrapper.height(height - 80 + "px");
        }
    };
})();