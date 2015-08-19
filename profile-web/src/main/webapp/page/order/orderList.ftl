<#if orderInfoList??>
    <#if orderInfoList?size != 0>
        <#list orderInfoList as order>
            <#if order_index % 2 == 0>
            <tr id="${order.orderId!}">
                <td class="right_tab_tbody">
                    <a href="${config.getDomainItem()}/item/detail.do?productId=${order.productId}" target="_blank"><img
                            src="${config.changeToSSL(order.imageUrl)!}" alt=""></a>

                    <div class="right_tab_td">
                        <p class='right_tab_p'>
                            <span class='right_tab_time'>${order.insertTime?datetime("yyyyMMddHHmmss")}</span><!-- <span>14:58:22</span> -->
                        </p>


                        <a href="${config.getDomainItem()}/item/detail.do?productId=${order.productId}"
                           class='right_tab_a1' target="_blank">${order.productName!}</a>

                        <div class="right_tab_per">
                            <p class="right_tab_bar">
                                <i class='right_tab_bar_co right_tab_bar1' style="width:${order.productRate!}%"></i>
                            </p>${order.productRate!}%
                        </div>

                        <p class='right_tab_order'>订单编号：${order.orderId!}</p>

                    </div>
                </td>
                <td id="right_money_num">${order.orderNum!}</td>
                <td class='right_money'>${order.orderAmt!}</td>
                <input id='refund_money_amount' type="hidden" value="${order.availableRefundAmount!}"/>
                <#if order.orderStatus == "01">
                    <td id="order_status_not_pay_value">未支付</td>
                <#elseif order.orderStatus == "02">
                    <td>订单取消</td>
                <#elseif order.orderStatus == "03">
                    <td id="order_status_success_pay_value">等待支付尾款</td>
                <#elseif order.orderStatus == "04">
                    <td id="order_status_success_pay_value">支付成功</td>
                <#elseif order.orderStatus == "05">
                    <td>支付失败</td>
                <#elseif order.orderStatus == "06">
                    <td>退款待审核</td>
                <#elseif order.orderStatus == "07">
                    <td>退款处理中</td>
                <#elseif order.orderStatus == "08">
                    <td>退款成功</td>
                <#elseif order.orderStatus == "09">
                    <td>退款失败</td>
                <#elseif order.orderStatus == "10">
                    <td>退款拒绝</td>
                <#elseif order.orderStatus == "11">
                    <td>订单关闭</td>
                <#elseif order.orderStatus == "12">
                    <td>支付处理中</td>
                </#if>

                <td>
                    <#if order.isBalance == "1" >
                        <a id="order_status_not_pay_target" class="pay_btn" href="#" onclick="submitPart('${order.productId}','${order.orderAmt?number + order.orderFee?number}','${order.howBuyId}','${order.orderNum}','${order.orderId}','${order.payBackInfoList[0].addressId!}','02');">
                            支付尾款
                        </a>
                     </#if>  
                       <#if order.isAllPay == "1" >
                       <a id="order_status_not_pay_target" class="pay_btn" href="#" onclick="checkOrderStatusBeforeSubmitPart('${order.productId}','${order.orderAmt?number + order.orderFee?number}','${order.howBuyId}','${order.orderNum}','${order.orderId}','${order.payBackInfoList[0].addressId!}','03');">
                            支付全款
                        </a>
                     </#if>   
                     <#if order.isAppoint == "1" >
                       	<a id="order_status_not_pay_target" class='right_look' href="#" onclick="checkOrderStatusBeforeSubmitPart('${order.productId}','${order.orderAmt?number + order.orderFee?number}','${order.howBuyId}','${order.orderNum}','${order.orderId}','${order.payBackInfoList[0].addressId!}','01');">
                            支付预约金
                        </a>
                     </#if>
                    <#if order.orderStatus == "01">
                        <a href="#" class='right_look cancel_link'>取消订单</a>
                    </#if>
	                 <a href="#" onclick="viewDetail('${order.custId!}','${order.howBuyId!}','${order.orderId!}','${order.productId!}','${order.isAllPay}','${order.isBalance}');"
	                    class='right_look'>查看详情</a>
	                 <#if order.refundFlg == "1">
	                    <a href="#" class='right_look refund_link'>申请退款</a>
	                 </#if>

                </td>
            </tr>
            <#else>
            <tr class="right_tbody" id="${order.orderId!}">
                <td class="right_tab_tbody">
                    <a href="${config.getDomainItem()}/item/detail.do?productId=${order.productId}" target="_blank"><img
                            src="${config.changeToSSL(order.imageUrl)!}" alt=""></a>

                    <div class="right_tab_td">
                        <p class='right_tab_p'>
                            <span class='right_tab_time'>${order.insertTime?datetime("yyyyMMddHHmmss")}</span><!-- <span>14:58:22</span> -->
                        </p>
                        <a href="${config.getDomainItem()}/item/detail.do?productId=${order.productId}"
                           class='right_tab_a1' target="_blank">${order.productName!}</a>

                        <div class="right_tab_per">
                            <p class="right_tab_bar">
                                <i class='right_tab_bar_co right_tab_bar2' style="width:${order.productRate!}%"></i>
                            </p>${order.productRate!}%
                        </div>
                        <p class='right_tab_order'>订单编号：${order.orderId!}</p>
                    </div>
                </td>
                <td id="right_money_num">${order.orderNum!}</td>
                <td class='right_money'>${order.orderAmt!}</td>
                <input id='refund_money_amount' type="hidden" value="${order.availableRefundAmount!}"/>
                <#if order.orderStatus == "01">
                    <td id="order_status_not_pay_value" >未支付</td>
                <#elseif order.orderStatus == "02">
                    <td>订单取消</td>
                <#elseif order.orderStatus == "03">
                    <td id="order_status_success_pay_value">等待支付尾款</td>
                <#elseif order.orderStatus == "04">
                    <td id="order_status_success_pay_value">支付成功</td>
                <#elseif order.orderStatus == "05">
                    <td>支付失败</td>
                <#elseif order.orderStatus == "06">
                    <td>退款待审核</td>
                <#elseif order.orderStatus == "07">
                    <td>退款处理中</td>
                <#elseif order.orderStatus == "08">
                    <td>退款成功</td>
                <#elseif order.orderStatus == "09">
                    <td>退款失败</td>
                <#elseif order.orderStatus == "10">
                    <td>退款拒绝</td>
                <#elseif order.orderStatus == "11">
                    <td>订单关闭</td>
                <#elseif order.orderStatus == "12">
                    <td>支付处理中</td>
                </#if>

                <td>
                    <#if order.isBalance == "1" >
                        <a id="order_status_not_pay_target" class="pay_btn" href="#" onclick="submitPart('${order.productId}','${order.orderAmt?number + order.orderFee?number}','${order.howBuyId}','${order.orderNum}','${order.orderId}','${order.payBackInfoList[0].addressId!}','02');">
                            支付尾款
                        </a>
                     </#if>  
                       <#if order.isAllPay == "1" >
                       <a id="order_status_not_pay_target" class="pay_btn" href="#" onclick="submitPart('${order.productId}','${order.orderAmt?number + order.orderFee?number}','${order.howBuyId}','${order.orderNum}','${order.orderId}','${order.payBackInfoList[0].addressId!}','03');">
                            支付全款
                        </a>
                     </#if>   
                     <#if order.isAppoint == "1" >
                       	<a id="order_status_not_pay_target" class="right_look" href="#" onclick="submitPart('${order.productId}','${order.orderAmt?number + order.orderFee?number}','${order.howBuyId}','${order.orderNum}','${order.orderId}','${order.payBackInfoList[0].addressId!}','01');">
                            支付预约金
                        </a>
                     </#if>
                    <#if order.orderStatus == "01">
                        <a href="#" class='right_look cancel_link'>取消订单</a>
                    </#if>
	                 <a href="#" onclick="viewDetail('${order.custId!}','${order.howBuyId!}','${order.orderId!}','${order.productId!}','${order.isAllPay}','${order.isBalance}');"
	                    class='right_look'>查看详情</a>
	                 <#if order.refundFlg == "1">
	                    <a href="#" class='right_look refund_link'>申请退款</a>
	                 </#if>
                        
                </td>
            </tr>
            </#if>
        </#list>
        <#else>
     	<div class="none">
		</div>
    </#if>
</#if>
<form id="sbForm" name="submit" action="${config.getDomainTrade()}/createOrder.do" method="POST">
                <input type="hidden" id="productId" name="productId" value="">
                <input type="hidden" id="amt" name="amt" value="">
                <input type="hidden" id="h" name="h" value="">
                <input type="hidden" id="orderNum" name="orderNum" value="">
                <input type="hidden" id="orderId" name="orderId" value="">
                <input type="hidden" id="addressId" name="addressId" value="">
                <input type="hidden" id="orderType" name="orderType" value="">
                
                
                <input type="hidden" id="custId" name="custId">
                <input type="hidden" id="isAllPay" name="isAllPay">
                <input type="hidden" id="isBalance" name="isBalance">
                <input type="hidden" id="howBuyId" name="howBuyId">
 </form>



