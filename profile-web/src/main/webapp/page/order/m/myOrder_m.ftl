<#import "../../share/m/layout.ftl" as page>
<@page.pc>

<div class="pagewrapper">
    <section class="temp_page user_assets no_banner">
        <div class="header_page custom_header_page">
            <span>
        我的资产    </span>
            <a class="back" href="${config.getDomainM()}" >
                back
            </a>
        </div>
        <#include "../../account/m/account.ftl">
        <div class="tabs">
            <a href="/m/orders.do" class="active">
                我支持的
            </a>
            <a href="/m/order/follows.do">
                我关注的
            </a>
        </div>
        <ul class="assest" id="pagechoose">
            <#if orderInfoList??>
                <#if orderInfoList?size != 0>
                    <#list orderInfoList as order>
                        <li>
                            <h3>
            <span>
                	订单号：${order.orderId!}
            </span>
                                <em>
                                    <#if order.orderStatus == "01">
                                        未支付
                                    <#elseif order.orderStatus == "02">
                                        订单取消
                                    <#elseif order.orderStatus == "03">
                                        等待支付尾款
                                    <#elseif order.orderStatus == "04">
                                        支付成功
                                    <#elseif order.orderStatus == "05">
                                        支付失败
                                    <#elseif order.orderStatus == "06">
                                        退款待审核
                                    <#elseif order.orderStatus == "07">
                                        退款处理中
                                    <#elseif order.orderStatus == "08">
                                        退款成功
                                    <#elseif order.orderStatus == "09">
                                        退款失败
                                    <#elseif order.orderStatus == "10">
                                        退款拒绝
                                    <#elseif order.orderStatus == "11">
                                        订单关闭
                                    <#elseif order.orderStatus == "12">
                                        支付处理中
                                    </#if>
                                </em>
                            </h3>

                            <div class="main_part">
                                <a href="${config.domainItem}/m/item/detail.do?productId=${order.productId}">
                                    <img src="${order.imageUrl!}">
                                </a>

                                <div class="content">
                                    <h4>
                                        <a href="${config.domainItem}/m/item/detail.do?productId=${order.productId}">
                                        <#if order.productName?length gt 10 > 
                                        	${order.productName?substring(0,10)}……
                                        </#if>
                                        <#if order.productName?length lt 11 > 
                                        	${order.productName!}
                                        </#if>
                                        </a>
                                    </h4>

                                    <p>
                                    ${order.insertTime?datetime("yyyyMMddHHmmss")}
                                    </p>
                                    <h5>
                                        订单金额：<span>￥${order.orderAmt!}</span>
                                    </h5>
                                </div>
                                <div class="link_part">
                                    <#if order.orderStatus == "01">
                                        <a href="#"
                                           onclick="submitPart('${order.productId}','${order.orderAmt}','${order.howBuyId}','${order.orderNum}','${order.orderId}')">
                                            去支付
                                        </a>
                                        <a href="#" onclick="javascript:cancelOrder('${order.orderId!}');">
                                            取消订单
                                        </a>
                                    <#elseif order.orderStatus == "03">
                                        <a href="#"
                                           onclick="submitPart('${order.productId}','${order.orderAmt}','${order.howBuyId}','${order.orderNum}','${order.orderId}')">
                                            去支付
                                        </a>
                                        <#if order.refundFlg == "1">
                                            <a href="#"
                                               onclick="javascript:refund('${order.orderId!}',${order.orderNum!},${order.availableRefundAmount!})">
                                                申请退款
                                            </a>
                                        </#if>
                                    <#elseif order.orderStatus == "02">

                                    <#elseif order.orderStatus == "08">

                                    <#else>
                                        <!-- <a href="javascript:void(0);"> 已支付</a> -->
                                        <#if order.refundFlg == "1">
                                            <a href="#"
                                               onclick="javascript:refund('${order.orderId!}',${order.orderNum!},${order.availableRefundAmount!})">
                                                申请退款
                                            </a>
                                        </#if>
                                    </#if>
                                </div>
                            </div>
                        </li>
                    </#list>
                </#if>
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
    </section>
</div>
<input type="hidden" id="refundOrderId" name="refundOrderId" value="">
<input type="hidden" id="refundOrderNum" name="refundOrderNum" value="">
<input type="hidden" id="refundOrderAmt" name="refundOrderAmt" value="">
<div class="ext ext_number">
    <div class="wrapper">
        <h2>
            确认申请退款吗？
        </h2>

        <div class="link_part">
            <a href="#" onclick="javascript:closeExt();">
                取消
            </a>
            <a href="#" onclick="javascript:refundOrder();">
                确定
            </a>
        </div>
    </div>
</div>


<div class="logo">
</div>

<script>
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

    //退款
    function refund(refundOrderId, refundOrderNum, refundOrderAmt) {
        $("#refundOrderId").val(refundOrderId);
        $("#refundOrderNum").val(refundOrderNum);
        $("#refundOrderAmt").val(refundOrderAmt);
        openExt($(".ext_number"));
    }

    function refundOrder(refundOrderId, refundOrderNum, refundOrderAmt) {
        refundOrderId = $("#refundOrderId").val();
        refundOrderNum = $("#refundOrderNum").val();
        refundOrderAmt = $("#refundOrderAmt").val();
        $.ajax({
            url: "/m/order/refundOrder.do",
            type: "POST",
            dataType: "json",
            data: {
                refundAmt: refundOrderAmt,
                refundNum: refundOrderNum,
                orderId: refundOrderId
            },
            success: function (data) {
                if (data.success) {
                    alert('退款成功');
                    window.location.reload();
                } else {
                    alert(data.message);
                }
            },
            error: function (data) {
                return;
            }
        });
        $(".ext_number").hide();
        $("#refundOrderId").val('');
        $("#refundOrderNum").val('');
        $("#refundOrderAmt").val('');
    }
    function closeExt() {
        $(".ext_number").hide();
    }

    //取消订单
    function cancelOrder(orderId) {
        if (confirm("确认取消订单吗？")) {
            $.ajax({
                url: "/m/order/cancelOrder.do",
                type: "POST",
                dataType: "json",
                data: {
                    orderId: orderId
                },
                success: function (data) {
                    if (data.success) {
                        alert('取消成功');
                        window.location.reload();
                    } else {
                        alert(data.message);
                    }
                },
                error: function (data) {
                    return;
                }
            });
        }
    }

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
        var pageUrl = "/m/order/paging.do?curPage=" + curPage;
        $.ajax({
            url: pageUrl,
            type: "GET",
            dataType: "text",
            success: function (data) {
                loading.hide();
                $("#pagechoose").append(data);
            },
            error: function (e) {
//                alert("接口异常！");
            }
        });
    }

    function submitPart(productId, amt, howBuyId, orderNum, orderId) {
        window.location.href = "${config.getDomainTrade()}/m/confirmOrderDetail.do?productId=" + productId + "&h=" + howBuyId + "&orderNum=" + orderNum + "&amt=" + amt + "&orderId=" + orderId;
    }
</script>
</@page.pc>