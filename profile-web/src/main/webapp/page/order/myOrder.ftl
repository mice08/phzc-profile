<#import "../share/pc/layout.ftl" as layout> <@layout.pc>
<div class="user_page">
    <div class="main clearfix">
        <#include "../leftNav.ftl"> <@leftNav activeMenu="item1"> </@leftNav>



        <div class="right">
            <div class="user_page_inner  user_page1-1">

                <ul class="user_top cha_pass">
                    <li class="first"><a href="/orders.do" class="active">
                        我支持的 </a></li>
                    <li><a href="/order/follows.do"> 我关注的 </a> <i>4</i></li>
                </ul>
    													<#if  orderInfoList?? && orderInfoList?size != 0>
                        						   				   <table class="right_item">
												                    <thead>
												                    <tr>
												                        <td class="right_item_thd">项目信息</td>
												                        <td>份额/份数</td>
												                        <td>支持金额</td>
												                        <td>订单状态</td>
												                        <td class="right_item_thead">状态</td>
												                    </tr>
												                    </thead>
												                    <tbody>
												                        <#include "orderList.ftl"/>
												                    </tbody>
												                </table>
												                <#include "orderPage.ftl" />
                        						   		 <#else>
     															<div class="none"></div>
                        						   		 </#if>



            </div>
        </div>
    </div>
</div>



<input type="hidden" id="refundOrderId" value=""></input>
<input type="hidden" id="refundOrderNum" value=""></input>
<input type="hidden" id="refundOrderAmt" value=""></input>


<!--[if IE 6]>
<script src="skin/js/png_fix.js"></script>
<script>
    DD_belatedPNG.fix('.img_part .cover');
</script>
<![endif]-->
<div class="bgcover"></div>
<div class="ext ext_refund">
    <h1>
        申请退款
    </h1>

    <form>
        <div class="input_row">
            <label>
                申请退款金额
            </label>
        <span class="span_num">
            ￥10
        </span>
        </div>
        <div class="input_row hidden">
            <label>
            </label>

            <div class="text_part">
                <textarea></textarea>
            <span>
                亲在这里输入具体退款原因
            </span>
            </div>
        </div>
        <div class="link_row">
            <a href="#" id="closeRefundOrder" class="cancel">
                取消
            </a>
            <a href="#" id="confirmRefundOrder" class="cofirm">
                确定
            </a>
        </div>
    </form>
    <p class="infor">
        退款说明：您的退款金额将全部退还到您的账户余额。
    </p>
    <a href="#" class="close">
        关闭
    </a>


</div>

<div class="ext ext_intro">
    <h1>
        取消订单
    </h1>

    <p>
        <span  style="margin-left:30px;font-size:16px">
            确认取消订单？
        </span>
        <br/>
        <span  style="margin-left:30px;font-size:16px">
            取消订单后不可恢复！
        </span>
    </p>
    <form>
        <div class="link_row">
            <a href="#" id="closeCancelOrder" class="cancel">
                取消
            </a>
            <a href="#" id="confirmCancelOrder" class="cofirm">
                确定
            </a>
        </div>
    </form>
    <a href="#" class="close">
        关闭
    </a>
</div>

<div class="ext ext_state1" style="display: hide;">
    <h1>提示</h1>
    <div class="ext_hint ext_hint_success">
        <div class="content" style="margin-left:0px;text-align: center;">
            <p></p><h2><i id="alertMessageInfo">订单取消成功</i></h2><p></p>
            <a href="javascript:clickConfirm('.ext_state1');" style="margin-top:40px;margin-left: auto;margin-right: auto;" class="confirm2">确认</a>
        </div>
    </div>
    <a href="javascript:clickConfirm('.ext_state1');" class="close"> 关闭 </a>
    <script>
        function clickConfirm(divName){
            closeExt(divName);
            $("#alertMessageInfo").html('');
        }
    </script>
</div>

<#--<div class="ext ext_state2" style="display: hide;">
    <h1 id="alertMessageTitle">提示</h1>
    <div class="ext_hint ext_hint_success">
        <div class="content" style="margin-left:0px;text-align: center;">
            <p></p><h2><i id="alertMessageInfo">订单退款成功</i></h2><p></p>
            <a href="javascript:clickConfirm('.ext_state2');" style="margin-top:40px;margin-left: auto;margin-right: auto;" class="confirm2">确认</a>
        </div>
    </div>
    <a href="javascript:clickConfirm('.ext_state2');" class="close"> 关闭 </a>
    <script>
        function clickConfirm(divName){
            closeExt(divName);
        }
    </script>
</div>-->

<script>

    $(document).on("click", "a.refund_link", function (e) {
        e.preventDefault();
        var refundAmt = $(e.currentTarget.parentElement.parentElement).find("#refund_money_amount").val();
        $('#refundOrderAmt').val(refundAmt);
        var refundNum = $(e.currentTarget.parentElement.parentElement).find("#right_money_num").html();
        $('#refundOrderNum').val(refundNum);
        var orderId = $(e.currentTarget.parentElement.parentElement).find(".right_tab_order").html();
        $('#refundOrderId').val(orderId.split('：')[1]);
        $(".span_num").html(refundAmt);
        openExt(".ext_refund")
    })

    $(document).on("click", "#closeRefundOrder", function (e) {
        e.preventDefault();
        closeExt(".ext_refund")
    })


    $(document).on("click", "#confirmRefundOrder", function (e) {
        e.preventDefault()
        var refundAmt = $('#refundOrderAmt').val();
        var refundNum = $('#refundOrderNum').val();
        var orderId = $('#refundOrderId').val();
        //清除数据
        $('#refundOrderAmt').val('');
        $('#refundOrderNum').val('');
        $('#refundOrderId').val('');
        $.ajax({
            url: "/order/refundOrder.do",
            type: "POST",
            dataType: "json",
            data: {
                refundAmt: refundAmt,
                refundNum: refundNum,
                orderId: orderId
            },
            success: function (data) {
                if (data.success) {
//                    alert('退款成功');
                    var element = $(document).find("#"+orderId);
                    var order_status_success_pay_value_Element = $(element).find("#order_status_success_pay_value");
                    order_status_success_pay_value_Element.html('退款成功');
                    var order_status_not_pay_target_Element = $(element).find("#order_status_not_pay_target");
                    order_status_not_pay_target_Element.remove();
                    var refund_link_Element = $(element).find(".refund_link");
                    refund_link_Element.remove();
                    $("#alertMessageInfo").html("订单退款成功");
                    openExt(".ext_state1");
                } else {
                    $("#alertMessageInfo").html(data.message);
                    openExt(".ext_state1");
                }
            },
            error: function (data) {
                $("#alertMessageInfo").html("服务器异常，请稍后再试！");
                openExt(".ext_state1");
            }
        });
        closeExt(".ext_refund")
    })

    $(document).on("click", "a.cancel_link", function (e) {
        e.preventDefault();
        var orderId = $(e.currentTarget.parentElement.parentElement).find(".right_tab_order").html();
        $('#refundOrderId').val(orderId.split('：')[1]);
        openExt(".ext_intro")
    })

    $(document).on("click", "#closeCancelOrder", function (e) {
        e.preventDefault();
        $('#refundOrderId').val('');
        closeExt(".ext_intro")
    })

    $(document).on("click", "#confirmCancelOrder", function (e) {
        e.preventDefault()
        var orderId = $('#refundOrderId').val();
        //清除数据
        $('#refundOrderId').val('');
        $.ajax({
            url: "/order/cancelOrder.do",
            type: "POST",
            dataType: "json",
            data: {
                orderId: orderId
            },
            success: function (data) {
                if (data.success) {
                    var element = $(document).find("#"+orderId);
                    var order_status_not_pay_value_Element = $(element).find("#order_status_not_pay_value");
                    order_status_not_pay_value_Element.html('订单取消');
                    var order_status_not_pay_target_Element = $(element).find("#order_status_not_pay_target");
                    order_status_not_pay_target_Element.remove();
                    var cancel_link_Element = $(element).find(".cancel_link");
                    cancel_link_Element.remove();
                    $("#alertMessageInfo").html('订单取消成功');
                    openExt(".ext_state1");
                } else {
                    $("#alertMessageInfo").html(data.message);
                    openExt(".ext_state1");
                }
            },
            error: function (data) {
                $("#alertMessageInfo").html("服务器异常，请稍后再试！");
                openExt(".ext_state1");
            }
        });
        closeExt(".ext_intro")
    })

    var hide_part = $(".ext form .hidden");
    var ext = $(".ext_refund");
    $(".ext .select_part li a").click(function () {
        if ($.trim($(this).attr("data-value")) == "other") {
            hide_part.show();
            ext.css({
                "height": 370,
                "margin-top": " -200px"
            })
        } else {
            hide_part.hide();
            ext.css({
                "height": 250,
                "margin-top": " -140px"
            })
        }
    })

    function submitPart(productId,amt,howBuyId,orderNum,orderId,addressId,orderType) {
        $("#productId").val(productId);
        $("#orderId").val(orderId);
        $("#amt").val(amt);
        $("#orderNum").val(orderNum);
        $("#h").val(howBuyId);
        $("#addressId").val(addressId);
        if(orderType == "02") {//支付尾款
            $("#orderType").val("02");
        } else if(orderType == "01") {//支付预约金
            $("#orderType").val("01");
        } else if(orderType == "03") {//支付全款
            $("#orderType").val("03");
        }
        $("#sbForm").attr("action", "${config.getDomainTrade()}/createOrder.do");
        $('#sbForm').submit();
    }

    function checkOrderStatusBeforeSubmitPart(productId,amt,howBuyId,orderNum,orderId,addressId,orderType) {
        $.ajax({
            url: "/order/judgeOrder.do",
            dataType : "json",
            type: "POST",
            data: {
                orderId : orderId
            },
            success: function (data) {
                if(data.success) {
                    if("02" == data.orderStatus || "11" == data.orderStatus) {
                        $("#alertMessageInfo").html('<span style="color:#4f5459"> 订单支付超时被关闭，如需认购此项 </span><br><span style="color:#4f5459;margin-left:-115px;text-align: center;">  目，您可以重新下单。</span>');
                        openExt(".ext_state1");
                    } else {
                        submitPart(productId,amt,howBuyId,orderNum,orderId,addressId,orderType);
                    }
                }else{
                    $("#alertMessageInfo").html(data.message);
                    openExt(".ext_state1");
                }
            },
            error: function (e) {
                $("#alertMessageInfo").html("服务器异常，请稍后再试！");
                openExt(".ext_state1");
            }
        });
    }

	function viewDetail(custId,howBuyId,orderId,productId,isAllPay,isBalance) {

	        //$("#sbForm").attr("action", "/order/detail");
			var f=document.createElement("form");
			var custIdInput=document.createElement("input");
			custIdInput.type = "hidden";
			f.appendChild(custIdInput);
			custIdInput.value = custId;
			custIdInput.name = "custId";

			var howBuyIdInput=document.createElement("input");
			howBuyIdInput.type = "hidden";
			f.appendChild(howBuyIdInput);
			howBuyIdInput.value = howBuyId;
			howBuyIdInput.name = "howBuyId";

			var orderIdInput=document.createElement("input");
			orderIdInput.type = "hidden";
			f.appendChild(orderIdInput);
			orderIdInput.value = orderId;
			orderIdInput.name = "orderId";

			var productIdInput=document.createElement("input");
			productIdInput.type = "hidden";
			f.appendChild(productIdInput);
			productIdInput.value = productId;
			productIdInput.name = "productId";

			var isAllPayInput=document.createElement("input");
			isAllPayInput.type = "hidden";
			f.appendChild(isAllPayInput);
			isAllPayInput.value = isAllPay;
			isAllPayInput.name = "isAllPay";

			var isBalanceInput=document.createElement("input");
			isBalanceInput.type = "hidden";
			f.appendChild(isBalanceInput);
			isBalanceInput.value = isBalance;
			isBalanceInput.name = "isBalance";

			f.action="/order/detail";
			f.target="_blank";
			f.method="post";
			document.body.appendChild(f);
			f.submit();

	        //$('#sbForm').submit();
    }

</script>
</@layout.pc>
