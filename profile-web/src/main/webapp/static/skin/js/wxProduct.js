
(function ($) {
    var html_obj = $("html");
    var window_obj = $(window);
    //size
    function sizeConfirm() {
        var window_width = window_obj.width();
        if (window_width >= 640) {
            html_obj.css({"font-size": 20 + "px"});
        } else {
            html_obj.css({"font-size": 20 * window_width / 640 + "px"});
        }
    }

    sizeConfirm();
    window_obj.bind("resize", function () {
        sizeConfirm();
    });
    $(function () {
        window_obj.trigger("resize");
    })
})(Zepto);

(function ($) {
    var button = $("a.open");
    button.each(function () {
    $(this).attr("active", "false");
    })
button.click(function (e) {
    e.preventDefault();
    e.stopPropagation();
    var obj = $(this);
    var origin = obj.parents(".content").find(".origin");
    var fake = obj.parents(".content").find(".fake");
    if (obj.attr("active") == "false") {
    fake.hide();
    origin.show();
    origin.css({"opacity": 1});
obj.text("收起");
obj.addClass("active");
origin.addClass("active");
obj.attr("active", "true");
} else {
    origin.css({"opacity": 0});
fake.show();
origin.hide();
obj.text("展开");
obj.removeClass("active");
origin.removeClass("active");
obj.attr("active", "false");
}
})
})(Zepto);

(function ($) {
    var top = $(".detail .fix_part .top");
    var top_y = 0
    $(window).scroll(
    function () {
    var scroll = $(this).scrollTop();
    if (scroll > top_y) {
    top.css({bottom: "-4rem"});
top_y = scroll;
} else {
    top.css({bottom: "4rem"});
top_y = scroll;
}
}
);
})(Zepto);

(function ($) {
    var button = $(".detail .ext_part ul li a");
    var ext = $(".ext_infor");
    button.click(function (e) {
    e.preventDefault();
    e.stopPropagation();
    var obj = $(this);
    if (!obj.hasClass("active")) {
    obj.addClass("active");
    ext.show();
    } else {
    obj.removeClass("active");
    ext.hide();
    }
}
);
})(Zepto);

(function ($) {
    var tab_link = $("ul.tabs li a");
    var tab_link_li = $("ul.tabs li");
    var tab_content = $(".tabs_content li");
    tab_link.click(function (e) {
    e.preventDefault();
    var obj = $(this);
    var index = obj.parents("li").index();
    tab_link_li.removeClass("active");
    obj.parents("li").addClass("active");
    tab_content.hide();
    tab_content.eq(index).show();
    });
tab_link.eq(0).trigger("click")
})(Zepto);
