/**
 * Created by Leo on 2017/9/26.
 */
(function () {
    var SITE;
    if (window.LocationPicker) {
        return;
    }
    function G(id) {
        return document.getElementById(id);
    }

    window.LocationPicker = function () {
        this.map = null;
        this.address = '';
        this.isEverShown = false;
        this.isLoaded = false;
        this.zoom = 16;
    };
    LocationPicker.prototype = {
        init: function () {
            var me = this;
            var html = '<div class="picker-content">\
                                <div class="picker-map">\
                                    <div id="pickerMap" class="map-wrap"></div>\
                                    <label class="marker"></label>\
                                </div>\
                                <div class="picker-address"><ul class="list list-addresses"></ul></div>\
                            </div>\
                            <div class="picker-footer">\
                                <a class="btn btn-cancel" href="javascript:;">取消</a>\
                        </div>';
            $("#locationPicker").append(html);
            $("#locationPicker .btn-cancel").click(function () {
                $("#locationPicker").hide();
            });
            var mapHeight = $("#body").width();
            if (mapHeight > $(window).height() * 0.66) {
                mapHeight = $(window).height() * 0.66;
            }
            $("#pickerMap").height($("#body").height() * 0.4);
            $(".picker-address").height($("#body").height() * 0.6 - 48);
            $("#locationPicker").hide();
            me.map = new BMap.Map("pickerMap");
            me.map.addEventListener("moveend", function () {
                me._searchLocal();
            });
            me.map.addEventListener("zoomend", function () {
                me._searchLocal();
            });
            me.map.addEventListener("load", function () {
                this.isLoaded = true;
            });
            me.map.enableScrollWheelZoom();
            var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
                {
                    "input": "suggestId"
                    , "location": me.map
                });
            ac.addEventListener("onhighlight", function (e) {  //鼠标放在下拉列表上的事件
                var str = "";
                var _value = e.fromitem.value;
                var value = "";
                if (e.fromitem.index > -1) {
                    value = _value.province + _value.city + _value.district + _value.street + _value.business;
                }
                str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

                value = "";
                if (e.toitem.index > -1) {
                    _value = e.toitem.value;
                    value = _value.province + _value.city + _value.district + _value.street + _value.business;
                }
                str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
                G("searchResultPanel").innerHTML = str;
            });

            var myValue;
            ac.addEventListener("onconfirm", function (e) {    //鼠标点击下拉列表后的事件
                var _value = e.item.value;
                myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
                G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

                setPlace();
            });

            function setPlace() {
                me.map.clearOverlays();    //清除地图上所有覆盖物
                function myFun() {
                    var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
                    me.map.centerAndZoom(pp, 18);
                    me.map.addOverlay(new BMap.Marker(pp));    //添加标注
                }

                var local = new BMap.LocalSearch(me.map, { //智能搜索
                    onSearchComplete: myFun
                });
                local.search(myValue);
                SITE = myValue;
            }
        },
        _centerToCurrentLocation: function () {
            var me = this;
            var geolocation = new BMap.Geolocation();
            geolocation.getCurrentPosition(function (result) {
                if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                    me.map.centerAndZoom(result.point, me.zoom);
                }
            }, {enableHighAccuracy: true});
        },
        show: function (lng, lat, address) {
            $("#locationPicker").show();
            var me = this;
            this.address = address;
            if (this.isEverShown === false) {
                var isLatLngProvided = lat != null && !isNaN(lat) && lat != 0 && lng != 0;
                if (isLatLngProvided) {
                    me.map.centerAndZoom(new BMap.Point(lng, lat), me.zoom);
                }
                else {
                    me._centerToCurrentLocation();
                }
                me._searchLocal();
                this.isEverShown = true;
            }
        },
        onLocated: function (lng, lat, address) {

        },
        _searchLocal: function () {
            var me = this;
            var options = {
                onSearchComplete: function (results) {
                    if (local.getStatus() == BMAP_STATUS_SUCCESS) {
                        var addresses = me._getAddresses(results, me.map.getCenter());
                        addresses = me._removeDuplicates(addresses);
                        me._sort(addresses);
                        me._renderAddresses(addresses);
                    }
                }
            };
            var local = new BMap.LocalSearch(me.map, options);
            local.searchNearby(["小区", "学校", "写字楼", "宾馆", "酒店", "住宅", SITE], me.map.getCenter(), 1000);
        },
        _getAddresses: function (localResults, center) {
            var list = [];
            for (var i = 0; i < localResults.length; i++) {
                var localResult = localResults[i];
                var poiCount = localResult.getCurrentNumPois();
                for (var pi = 0; pi < poiCount; pi++) {
                    var poi = localResult.getPoi(pi);
                    if(poi != undefined){
                        list.push({
                            title: poi.title,
                            address: poi.address,
                            point: poi.point,
                            distance: this.map.getDistance(center, poi.point)
                        });
                    }
                }
            }
            return list;
        },
        _renderAddresses: function (addresses) {
            var me = this;
            $(".list-addresses").empty();
            for (var i = 0; i < addresses.length; i++) {
                var item = addresses[i];
                var pt = item.point;
                me._getLocation(pt, item, function (addr, nowItem) {
                    nowItem.address = addr;
                    var $li = $('<li><div>' + nowItem.title + '</div><div>' + nowItem.address + '</div></li>');
                    if (me.address === nowItem.address + nowItem.title) {
                        $li.addClass("selected");
                    }
                    $li.data("data", nowItem);
                    $li.click(function () {
                        var dataItem = $(this).data("data");
                        $("#locationPicker").hide();
                        $(this).closest("ul").children().removeClass("selected");
                        $(this).addClass("selected");
                        me.onLocated(dataItem.point.lng, dataItem.point.lat, dataItem.address + dataItem.title);
                    });
                    $(".list-addresses").append($li);
                });

            }
        },
        _sort: function (addresses) {
            for (var i = 0; i < addresses.length; i++) {
                var item1 = addresses[i];
                for (var j = i + 1; j < addresses.length; j++) {
                    var item2 = addresses[j];
                    if (item2.distance < item1.distance) {
                        var swapping = item1;
                        addresses[i] = item2;
                        addresses[j] = swapping;
                        item1 = item2;
                    }
                }
            }
        },
        _removeDuplicates: function (addresses) {
            var list = [];

            function listExists(item) {
                for (var li = 0; li < list.length; li++) {
                    var liItem = list[li];
                    if (liItem.address === item.address && liItem.title === item.title) {
                        return true;
                    }
                }
                return false;
            }
            for (var i = 0; i < addresses.length; i++) {
                var item = addresses[i];
                if (listExists(item) === false) {
                    list.push(item);
                }
            }
            return list;
        },
        _getLocation: function (pt, item, callback) {
            var address = '';
            var geoc = new BMap.Geocoder();
            geoc.getLocation(pt, function (rs) {
                var addComp = rs.addressComponents;
                if (rs !== null) {
                    if (addComp.province === addComp.city) {
                        address = addComp.province + addComp.district + addComp.street + addComp.streetNumber;
                    } else {
                        address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
                    }
                }
                callback(address, item);
            });

        }
    }
})();