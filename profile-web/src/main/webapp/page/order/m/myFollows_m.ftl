<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainM()}","parentName":"我的资产"} in page>
<@page.pc>

<#--<div class="pagewrapper">-->
    <section class="temp_page user_assets no_banner">
        <#--<div class="header_page custom_header_page">-->
    <#--<span>-->
        <#--我的资产    </span>-->
            <#--<a class="back" href="${config.getDomainM()}" onclick="${config.getDomainM()}">-->
                <#--back-->
            <#--</a>-->
        <#--</div>-->
        <#include "../../account/m/account.ftl">
        <div class="tabs">
            <a href="/m/orders.do">
                我支持的
            </a>
            <a href="/m/order/follows.do" class="active">
                我关注的
            </a>
        </div>
        <ul class="items" id="pagechoose">
            <#if userAttentionList?? && (userAttentionList?size > 0)>
                <#list userAttentionList as dto>
                    <li data-value="${dto.productRate?substring(0,dto.productRate?length-1)}">
                        <a href="${config.domainItem}/m/item/detail.do?productId=${dto.productId}">
                            <img src="${dto.imageUrl!}">

                            <div class="infor">
                                <h1>
                                ${dto.productName!}
                                </h1>

                                <div class="progress">
                                    <div class="progress_inner" style="width: ${dto.productRate!0};"></div>
                                </div>
                                <div class="num">
                                    <p>
                                        <strong><span>${dto.productRate!0}</span>%</strong><br>
                                        已达到
                                    </p>

                                    <p>
                                        <strong>￥${dto.productShare!0}</strong><br>
                                        已筹集
                                    </p>

                                    <p>
                                        <strong>${dto.sellCount!0}</strong><br>
                                        支持人数
                                    </p>

                                    <p>
                                        <strong>${dto.remainDays!0}天</strong><br>
                                        剩余天数
                                    </p>
                                </div>
                            </div>
                                <#if dto.productStatus == 9 ><!-- 众筹中 -->
                                <div class="states states1"></div>
                                <#elseif dto.productStatus == 7 ><!-- 预热中 -->
                                <div class="states states2"></div>
                                <#elseif dto.productStatus == 11 ><!-- 众筹成功 -->
                                <div class="states states3"></div>
                                <#elseif dto.productStatus == 10 ><!-- 众筹失败 -->
                                <div class="states states4"></div>
                                <#elseif dto.productStatus == 8 ><!-- 预约中 -->
                                <div class="states states5"></div>
                                <#elseif dto.productStatus == 12 ><!-- 项目成功 -->
                                <div class="states states6"></div>
                                <#elseif dto.productStatus == 13 ><!-- 项目失败 -->
                                <div class="states states7"></div>
                                <#elseif dto.productStatus == 15 ><!-- 成功结束 -->
                                <div class="states states8"></div>
                                <#elseif dto.productStatus == 16 ><!-- 失败结束 -->
                                <div class="states states9"></div>
                            </#if>
                        </a>
                    </li>
                </#list>
            <#else>
                <div class="none">
                    <p>
                        什么都没有？<br>
                        <a href="${config.getDomainM()}">
                            前往首页
                        </a>
                    </p>
                </div>
            </#if>
        </ul>
        <div class="loading">

        </div>
        <script>
            /*list*/
            (function ($) {
                var window_height = $(window).height();
                var home_lis = $(" .items li");
                window.home_lisfun = function () {
                    home_lis = $(" .items li");
                };
                function active_li(obj) {
                    var x = parseInt(obj.attr("data-value"));
                    obj.find(".progress_inner").width(x + "%");
                    var span = obj.find(".num strong span");
                    var time = 500 / x;
                    var y = 0;
                    var setTime = setInterval(function () {
                        if (x == y) {
                            clearInterval(setTime)
                        }
                        span.html(y);
                        y += 1;
                    }, time);
                }

                function analize(scroll) {
                    home_lis.each(function () {
                        var obj = $(this);
                        if (obj.hasClass("active")) {
                            return;
                        }
                        var obj_height = obj.offset().top + obj.height();
                        if ((scroll + window_height) > obj_height && scroll < obj_height) {
                            obj.addClass("active");
                            active_li(obj)
                        }
                    })
                }

                analize(0)
                $(window).scroll(
                        function () {
                            var scroll = $(this).scrollTop();
                            analize(scroll)
                        }
                );
            })(Zepto);

            /*下拉刷新*/
            (function () {
                var curPage = 1;

                function refreshDown() {
                    var window_obj = $(window);
                    var html_obj = $("html");
                    var scrollTop;

                    var w_h = window_obj.height();
                    var h_h = html_obj.height();

                    function changeConfig() {
                        w_h = window_obj.height();
                        h_h = html_obj.height();
                    }

                    window_obj.bind("resize", function () {
                        changeConfig()
                    });


                    window_obj.scroll(
                            function () {
                                scrollTop = $(this).scrollTop();
                                if (scrollTop / (h_h - w_h) >= 0.95) {
                                    do_paging(curPage);
                                    curPage++;
                                }
                            }
                    )
                }

                refreshDown()
            })(Zepto);

            function do_paging(curPage) {
                var loading = $(".loading")
                loading.show();
//                if (curPage==1){
//                    $("#pagechoose").html("");
//                }
                var pageUrl = "/m/order/followsPaging.do?curPage=" + curPage;
                $.ajax({
                    url: pageUrl,
                    type: "GET",
                    dataType: "text",
                    success: function (data) {
                        loading.hide();
                        $("#pagechoose").append(data);
//                        var divStr = "";
//                        if (pageSize==0&&curPage==1){
//                            divStr="<div class=\"error\">无数据</div>";
//                            $("#pagechoose").html(divStr);
//                            return;
//                        }
                    },
                    error: function (e) {
                        alert("接口异常！");
                    }
                });
            }
        </script>
    </section>
</div>
<div class="logo"></div>

</@page.pc>