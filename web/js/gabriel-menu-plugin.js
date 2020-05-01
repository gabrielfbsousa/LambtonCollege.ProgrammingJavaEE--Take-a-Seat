/*
 *
 * Gabriel's created plugin for the Menu Bar on the Term Project of Web Tech II
 *
 */

(function($) {
    $.fn.gabrielmenubar = function(options) {

        var defaultConfigurations = $.extend({
            //------------------------------Default font settings
            "fontColor": "#f3f3f3",
            "preferredFont": "helvetica",
            "textWidth": "10px",

            //------------------------------On Hover-in font settings
            "fontOnHover": "helvetica",
            "fontColorOnHover": "#f1f1f1",

            //------------------------------Cell settings
            "background": "#9D9D9D",
            "backgroundOnHover": "#3D4D5D",
            "spaceBetweenElements": "5px",


            //------------------------------Menu settings
            "menuLength": "10px",
            "menuWidth": "4px",
            "menuBackgroundColor": "#9D9D9D",
            "distanceToTop": "10px",

            //------------------------------Current-page cell settings
            "currentSelectedElementFontColor": "#222222",
            "currentSelectedElementFont": "helvetica",
            "currentSelectedElementBackground": "#fff",
        }, options);

        return this.each(function() {
            var menuElements = $("li a");
            var o = defaultConfigurations;

            //Font Configurations
            menuElements.css("font-family", o.preferredFont)
                .css("color", o.fontColor)
                .css("background-color", o.background);


            //Setup current element configurations
            var currentElement = $("#current");
            currentElement.css("font-family", o.currentSelectedElementFont)
                .css("background-color", o.currentSelectedElementBackground)
                .css("color", o.currentSelectedElementFontColor);

            //Menu configurations
            $(this).css("background-color", o.menuBackgroundColor);
            $(this).css("padding", o.distanceToTop);
            $("li").css("list-style", "none") //Makes the MenuBar to be horizontal
                .css("display", "inline");
            $("#menu_plugin li a").css("padding", o.menuWidth);

            //Hover in e hover out
            menuElements.hover(
                function() { //Mouse in - Set Hover options
                    $(this).css("font-family", o.fontOnHover)
                        .css("color", o.fontColorOnHover)
                        .css("background-color", o.backgroundOnHover);
                },
                function() { //Mouse out - Set default options
                    if ($(this).attr("id") != "current") {
                        $(this).css("color", o.fontColor)
                            .css("background-color", o.background)
                            .css("font-family", o.preferredFont);
                    } else {
                        $(this).css("font-family", o.currentSelectedElementFont)
                            .css("background-color", o.currentSelectedElementBackground)
                            .css("color", o.currentSelectedElementFontColor);
                    }
                });


        });
    };
})(jQuery);
