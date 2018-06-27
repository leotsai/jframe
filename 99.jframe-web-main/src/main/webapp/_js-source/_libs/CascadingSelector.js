/*
 创建于2017-07-19 12:18，来自裂果网
 */
(function () {
    if (window.CascadingSelector) {
        return;
    }
    var urls={
        natinals: "/api/region/nationals",
        saleRegions: "/api/region/sale-regions"
    };

    window.CascadingSelector = function () {
        this.$controls = [];
        this.optionalLabels = [];
        for (var i = 0; i < arguments.length; i++) {
            this.$controls.push(arguments[i]);
            this.optionalLabels.push("请选择");
        }

        this.items = [
            {
                value: '001000',
                text: '北京市',
                children: [
                    {
                        value: '001100',
                        text: '市辖区',
                        children: [
                            {value: '001101', text: '东城区'}
                        ]
                    }
                ]
            }
        ];
    };

    CascadingSelector.prototype = {
        init: function () {
            var me = this;
            for (var i = 0; i < me.$controls.length; i++) {
                (function (index, $ddl) {
                    $ddl.change(function () {
                        me._onSelectChanged(index, $(this));
                    });
                })(i, me.$controls[i]);
            }
        },
        loadNationalRegions: function(regionId){
            this._load(urls.natinals, regionId);
        },
        loadSaleRegions: function(regionId){
            this._load(urls.saleRegions, regionId);
        },
        render: function (items, value) {
            this.items = items;
            this._fixItems();
            var lastItem = this._findLastSelectedItem(value);
            if (lastItem == null) {
                this._renderSelect(0, this.items);
                this._onSelectChanged(0, this.$controls[0]);
            }
            else {
                var loopingItem = lastItem;
                while (loopingItem != null) {
                    var list = loopingItem.parent == null ? this.items : loopingItem.parent.children;
                    this._renderSelect(loopingItem.level, list, loopingItem.value);
                    loopingItem = loopingItem.parent;
                }
            }
        },
        select: function(value){
            this.render(this.items, value);
        },
        _load: function(url, selectedValue){
            var me = this;
            mvcApp.ajax.post(url, null, function(result){
                me.render(result.value, selectedValue + "");
            }, true);
        },
        _onSelectChanged: function (index, $ddl) {
            if (index >= this.$controls.length - 1) {
                return;
            }
            var me = this;
            var item = $ddl.children(":selected").data("item");
            if(item == null){
                item = {};
            }
            if (item.children == null) {
                item.children = [];
            }
            me._renderSelect(index + 1, item.children);
            me._onSelectChanged(index + 1, me.$controls[index + 1]);
        },
        _renderSelect: function (index, list, selectedValue) {
            var label = this.optionalLabels[index];
            var $ddl = this.$controls[index];
            $ddl.empty();
            if (label != null && label !== '') {
                var $option = $('<option value="">' + label + '</option>');
                $option.data("item", {children: []});
                $ddl.append($option);
            }
            for (var i = 0; i < list.length; i++) {
                var optionItem = list[i];
                var $option = $('<option value="' + optionItem.value + '">' + optionItem.text + '</option>');
                $option.data("item", optionItem);
                $ddl.append($option);
                if(optionItem.value === selectedValue){
                    $ddl.val(selectedValue);
                }
            }
        },
        _fixItems: function () {
            function fixItem(item) {
                if (item.children == null) {
                    item.children = [];
                }
                for (var j = 0; j < item.children.length; j++) {
                    var child = item.children[j];
                    child.level = item.level + 1;
                    child.parent = item;
                    fixItem(child);
                }
            }
            for (var i = 0; i < this.items.length; i++) {
                var item = this.items[i];
                item.level = 0;
                fixItem(item)
            }
        },
        _findLastSelectedItem: function (value) {
            var maxLevel = this.$controls.length - 1;

            function findItemByValue(item) {
                if (item.value === value && item.level === maxLevel) {
                    return item;
                }
                for (var i = 0; i < item.children.length; i++) {
                    var child = item.children[i];
                    var found = findItemByValue(child);
                    if (found != null) {
                        return found;
                    }
                }
                return null;
            }
            for (var i = 0; i < this.items.length; i++) {
                var item = this.items[i];
                var found = findItemByValue(item);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }
    };

})();