<#import "../share/pc/layout.ftl" as page>
<@page.pc>

<div class="order_page">
    <div class="main clearfix">
        <#if orderInfo.orderStatus=="04">
            <div class="title">
                <p>
                    <em class="success">
                        支付成功：
                    </em>
                    <#include "orderStatus.ftl">
                </p>
                <label>
                    支付金额：
		        <span>
		            ${totalAmt!}元
		        </span>
                </label>
            </div>
        <#elseif orderInfo.orderStatus=="11">
            <div class="title">
                <p>
                    <em class="error">
                        交易关闭：
                    </em>
                    <#include "orderStatus.ftl">

            </div>
        <#elseif orderInfo.orderStatus=="08">
            <div class="title">
                <p>
                    <em class="wait">
                        退款成功：
                    </em>
                    <#include "orderStatus.ftl">
                </p>
                <label>
                    退款金额：
		        <span>
		            ${orderInfo.totalRefundAmt!}元
		        </span>
                </label>
            </div>
        <#elseif orderInfo.orderStatus=="01">
            <div class="title">
                <p>
                    <em class="question">
                        未支付：
                    </em>
                    <#include "orderStatus.ftl">
                </p>
                <label>
                    应付金额：
		        <span>
		            ${totalFee!}元
		        </span>
                </label>
            </div>
        </#if>

        <div class="content">
            <h3>
                订单信息
            </h3>
            <ul class="clearfix">
                <li>
                    <label>
                        订单号：
                    </label>
            <span>
            ${orderInfo.orderId!}
            </span>
                </li>
                <li>
                    <label>
                        创建时间：
                    </label>
            <span>
            ${orderInfo.insertTime!}
            </span>
                </li>
            </ul>
            <ul class="clearfix">
                <li>
                    <label>
                        姓名：
                    </label>
            <span>
            ${userInfoDto.usrName!}
            </span>
                </li>
                <li>
                    <label>
                        手机号码：
                    </label>
            <span>
            ${userInfoDto.usrMp!}
            </span>
                </li>
                <li>
                    <label>
                        邮箱：
                    </label>
            <span>
            ${userInfoDto.usrEmail!}
            </span>
                </li>
                <li>
                    <label>
                        备注：
                    </label>
            <span>

            </span>
                </li>
            </ul>
            <#if addressInfoDto??>
                <h3>
                    收货信息
                </h3>
                <ul class="clearfix">
                    <li>
                        <label>
                            所在地区：
                        </label>
            <span>
            ${addressInfoDto.area!}
            </span>
                    </li>
                    <li>
                        <label>
                            详细地址：
                        </label>
            <span>
            ${addressInfoDto.address!}
            </span>
                    </li>
                    <li>
                        <label>
                            邮政编码：
                        </label>
            <span>
            ${addressInfoDto.postCode!}
            </span>
                    </li>
                    <li>
                        <label>
                            收货人姓名：
                        </label>
            <span>
            ${addressInfoDto.name!}
            </span>
                    </li>
                    <li>
                        <label>
                            联系方式：
                        </label>
            <span>
            ${addressInfoDto.telephone!}
            </span>
                    </li>
                </ul>
            </#if>
            <div class="item">
                <h3>
                    项目信息
                </h3>
                <table>
                    <thead>
                    <tr>
                        <td class="long">
                            项目名称
                        </td>
                        <td>
                            发起人
                        </td>
                        <td>
                            回报
                        </td>
                        <td>
                            支持金额（元）
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <a href="${config.domainItem}/item/detail.do?productId=${projectDto.productId!}">
                            ${projectDto.productName!}
                            </a>
                        </td>
                        <td>
                        ${projectDto.productSponsor!}
                        </td>
                        <td>
                        ${projectDto.repay!}
                        </td>
                        <td>
                        ${totalAmt!}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="item">
                <h3>
                    交易信息
                </h3>
                <table>
                    <thead>
                    <tr>
                        <td>
                            订单金额（元）
                        </td>
                        <td>
                            支付金额（元）
                        </td>
                        <td>
                            退款金额（元）
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                        ${totalAmt!}
                        </td>
                        <td>
                        ${orderInfo.paymentAmt!}
                        </td>
                        <td>
                        ${orderInfo.totalRefundAmt!}
                        </td>
                    </tr>
                    </tbody>
                </table>
                <p class="item_infor">
                <#--查看<a href="#">《电子合同》</a>-->
            ${contractInfo!}
                </p>
            </div>
        </div>
    <div class="all">
        <#if orderInfo.orderStatus=="01"||orderInfo.orderStatus=="03">
            应付金额：<span>${totalFee!}</span>元
        </div>
            <div class="submit_part">
                <#if orderInfoVo.isBalance == "1" >
                <a class="submit" href="#" onclick="submitPart()">
                    支付尾款
                <#elseif orderInfoVo.isAllPay == "1">
                <a class="submit" href="#" onclick="submitPart()">
                    支付全款
                </#if>
            </a>
            </div>
        </#if>
    </div>
</div>

<div class="ext ext_refund_detail">
    <h1>
        退款金额
    </h1>

    <div class="table_head">
        <table>
            <tr>
                <td>
                    申请时间
                </td>
                <td>
                    退款状态
                </td>
                <td>
                    申请退款原因
                </td>
                <td>
                    退款金额（元）
                </td>
            </tr>
        </table>
    </div>
    <div class="content">
        <table>
            <tr>
                <td>
                    2015-05-01 12:31:12
                </td>
                <td>
                <span>
                    退款审核中
                </span>
                </td>
                <td>
                    对项目不感兴趣
                </td>
                <td>
                    1,000.00
                </td>
            </tr>
            <tr class="sp">
                <td>
                    2015-05-01 12:31:12
                </td>
                <td>
                <span>
                    退款审核中
                </span>
                </td>
                <td>
                    对项目不感兴趣
                </td>
                <td>
                    1,000.00
                </td>
            </tr>
        </table>
    </div>
    <form id="sbForm" name="submit" action="${config.getDomainTrade()}/createOrder.do" method="POST">
        <input type="hidden" id="productId" name="productId" value="${orderInfo.productId!}">
        <input type="hidden" id="amt" name="amt" value="${orderInfo.orderAmt?number + orderInfo.orderFee?number}">
        <input type="hidden" id="h" name="h" value="${orderInfo.howBuyId!}">
        <input type="hidden" id="orderNum" name="orderNum" value="${orderInfo.orderNum!0}">
        <input type="hidden" id="orderId" name="orderId" value="${orderInfo.orderId!}">
        <input type="hidden" id="addressId" name="addressId" value="${addressInfoDto.addressId!}">
        <input type="hidden" id="orderType" name="orderType">
        <input type="hidden" id="isBalance" name="isBalance" value="${orderInfoVo.isBalance!}">
        <input type="hidden" id="isAllPay" name="isAllPay" value="${orderInfoVo.isAllPay!}">
    </form>
    <a href="#" class="close">
        关闭
    </a>
</div>

<script type="text/javascript">
    function submitPart() {
        var isBalance = $("#isBalance").val();
        var isAllPay = $("#isAllPay").val();
        if (isBalance == "1") {
            $("#orderType").val("02");
        }
        if (isAllPay == "1") {
            $("#orderType").val("03");
        }
        $('#sbForm').submit();
    }
</script>
</@page.pc>