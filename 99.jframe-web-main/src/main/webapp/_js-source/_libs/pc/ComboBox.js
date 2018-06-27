/**
 * Created by huxiaoli on 2018/1/22.
 */


(function () {
    $.widget( "ui.combobox", {
        _create: function() {
            var self = this;
            var select = this.element.hide(),
                selected = select.children( ":selected" ) || select.children(0),
                value = selected.text();
            var $div = $("<div class='combobox-box'></div>").insertAfter(select);
            var $input = $( "<input />" )
                .appendTo($div)
                .val( value )
                .autocomplete({
                    appendTo:'.main-w1',
                    delay: 0,
                    minLength: 0,
                    source: function(request, response) {
                        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
                        response( select.children("option" ).map(function() {
                            var text = $( this ).text();
                            if ( !request.term || matcher.test(text) ){
                                return {
                                    label: text.replace(
                                        new RegExp(
                                            "(?![^&;]+;)(?!<[^<>]*)(" +
                                            $.ui.autocomplete.escapeRegex(request.term) +
                                            ")(?![^<>]*>)(?![^&;]+;)", "gi"),
                                        "<strong>$1</strong>"),
                                    value: text,
                                    option: this
                                };
                            }
                        }) );
                    },
                    select: function( event, ui ) {
                        ui.item.option.selected = true;
                        self._trigger( "selected", event, {
                            item: ui.item.option
                        });
                        self.element.trigger('change');
                    },
                    change: function(event, ui) {
                        if ( !ui.item ) {
                            var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
                                valid = false;
                            select.children( "option" ).each(function() {
                                if ( this.value.match( matcher ) ) {
                                    this.selected = valid = true;
                                    return false;
                                }
                            });
                            if ( !valid ) {
                                $( this ).val(value);
                                select.val("");
                                return false;
                            }
                        }
                        if($(this).val() === ''){
                            $(this).val(self.element.children().eq(0).text());
                            select.val("");
                        }
                        self.element.trigger('change');
                    }
                })
                .addClass("ui-widget ui-widget-content ui-corner-left");

            $input.data( "autocomplete" )._renderItem = function( ul, item ) {
                return $( "<li></li>" )
                    .data( "item.autocomplete", item )
                    .append( "<a>" + item.label + "</a>" )
                    .appendTo( ul );
            };
            $( "<a></a>" )
                .attr( "tabIndex", -1 )
                .attr( "title", "" )
                .insertAfter( $input )
                .button({
                    icons: {
                        primary: "ui-icon-triangle-1-s"
                    },
                    text: false
                })
                .removeClass( "ui-corner-all" )
                .addClass( "ui-corner-right ui-button-icon ui-combobox-btn" )
                .click(function() {
                    if ($input.autocomplete("widget").is(":visible")) {
                        $input.autocomplete("close");
                        return;
                    }
                    $input.autocomplete("search", "");
                    $input.focus();
                });
        }
    });

})();