<#import "../share/pc/layout.ftl" as page>
<@page.pc>

<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item4"> </@leftNav>

		<!-- 	add right content -->
		<div class="right">
			<div class="user_page_inner  user_page4-2">
				<h1>充值</h1>
				<form class="cha_pass_f">
					<div class="input_row ">
						<label> <i>*</i> 充值金额
						</label>

						<div class="input_part">
							<input id="amount" name="amount" type="text" maxlength="7">
						</div>
						<span class='dollar'>元</span> 
					</div>

					<div class="input_row">
						<label> <i>*</i> 支付银行卡
						</label>
						<div class="bank_part">
							<a href="" class='bank_select bank_select_active'> <#if
								bankId??> <img
								src="${config.getDomainPcstatic()}/skin/images/banks/${bankId}.png"
								alt=""> <#else> <img
								src="${config.getDomainPcstatic()}/skin/images/banks/PINGAN.png"
								alt=""> </#if> <#if cardNoEnd??> <span>***
									${cardNoEnd! '***'} </span> </#if>
							</a> <input type="hidden" id="cardNoEnd" name="cardNoEnd"
								value="${cardNoEnd}">
							<a id="viewlimitation" href="#" class="normal"> 查看限额 </a>
						</div>
					</div>

					<div class="input_row">
						<label> <i>*</i> 短信验证码
						</label> <input type="hidden" id="mobileNo" name="mobileNo"
							value="${mobileNo}">
						<div class="input_part yzm_part">
							<input id="smsCode" name="smsCode" type="text" disabled="true">
							<span>验证码</span>
						</div>
						<a id="sendMessage" href="javascript:void(0)" class="yzm">
							发送验证码 </a>
					</div>
					<div class="input_row">
						<label> <i>*</i> 交易密码
						</label>
						<div class="input_part">
							<input id="transPwd" name="transPwd" type="password">
						</div>
						<a class="normal"
							href="${config.getDomainProfile()}/modifyPwd/resetTransPwdOne.do">忘记密码</a>

					</div>
					<div class="input_row">
						<label> </label> <a class="submit"
							href="javascript:submit_recharge();"> 提交 </a>
					</div>
				</form>
				
				<div class="page4_extop">
					<div class='page4_explain'>
						<h4>说明</h4>
						<p>1、不同银行充值额度不同，具体额度可以查看限额</p>
						<p>2、充值金额实时到账，如遇网站延迟，请耐心等待</p>
						<p>3、充值过程遇到问题，请联系客服邮箱 pub_zc@pingan.com.cn</p>
					</div>
				</div>

			</div>
		</div>
	</div>

	<!-- add box -->
	<div class="bgcover"></div>

	<div class="ext ext_limit2">
		<h1 align="center">充值支持银行列表</h1>
		<div class="table_head">
			<table>
				<tr>
					<td style="width: 30px;">序号</td>
					<td style="width: 80px;">银行</td>
					<td style="width: 60px;">账户类型</td>
					<td style="width: 60px;">交易类型</td>
					<td style="width: 60px;">单笔限额</td>
					<td style="width: 60px;">单日限额</td>
					<td style="width: 80px;">交易次数（月）</td>
				</tr>
			</table>
		</div>
		<div class="content">
			<table>
				<tbody>
					<tr>
						<td style="width: 30px;">1</td>
						<td style="width: 80px;">中国工商银行</td>
						<td style="width: 60px;">借记卡</td>
						<td style="width: 60px;">实时扣款</td>
						<td style="width: 60px;">5万</td>
						<td style="width: 60px;">5万</td>
						<td style="width: 80px;">10次</td>
					</tr>
					<tr class="sp">
						<td>2</td>
						<td>中国银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>1万</td>
						<td>1万</td>
						<td>10次</td>
					</tr>
					<tr>
						<td>3</td>
						<td>中国建设银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>50万</td>
						<td>50万</td>
						<td>10次</td>
					</tr>
					<tr class="sp">
						<td>4</td>
						<td>邮政储蓄银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>1万</td>
						<td>不限</td>
						<td>10次</td>
					</tr>
					<tr>
						<td>5</td>
						<td>招商银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>5万</td>
						<td>不限</td>
						<td>10次</td>
					</tr>
					<tr class="sp">
						<td>6</td>
						<td>兴业银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>2万</td>
						<td>5万</td>
						<td>不限</td>
					</tr>
					<tr>
						<td>7</td>
						<td>光大银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>50万</td>
						<td>50万</td>
						<td>10次</td>
					</tr>
					<tr class="sp">
						<td>8</td>
						<td>广发银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>50万</td>
						<td>50万</td>
						<td>10次</td>
					</tr>
					<tr>
						<td>9</td>
						<td>民生银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>50万</td>
						<td>50万</td>
						<td>10次</td>
					</tr>
					<tr class="sp">
						<td>10</td>
						<td>平安银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>50万</td>
						<td>50万</td>
						<td>不限</td>
					</tr>
					<tr class="sp">
						<td>11</td>
						<td>华夏银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
						<td>100万</td>
						<td>不限</td>
					</tr>
					<tr>
						<td>12</td>
						<td>中信银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
						<td>100万</td>
						<td>不限</td>
					</tr>
					<tr class="sp">
						<td>13</td>
						<td>浦东发展银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>49999元</td>
						<td>49999元</td>
						<td>10次</td>
					</tr>
					<tr>
						<td>14</td>
						<td>广州农商行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
						<td>100万</td>
						<td>不限</td>
					</tr>
					<tr class="sp">
						<td>15</td>
						<td>广州银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>50万</td>
						<td>50万</td>
						<td>10次</td>
					</tr>
				</tbody>
			</table>
		</div>
		<p class="table_infor">
			<span>备注：</span><br> 1、邮储银行晚上21点至24点不接受交易；<br>2、关于实时扣款退款问题，由于银行或银联系统故障导致长款交易，如果长款发生在易联端，易联T+1日对账后
			返回原卡，一般可于T+1日入账；如果长款发生在银行或银联端，银行或银联原则上T+2日对账后会返回原卡，一般不迟于T+3日入账。
		</p>
		<a href="#" class="close"> 关闭 </a>
		<script
			src="${config.getDomainPcstatic()}/skin/js/jquery.jscrollpane.min.js"></script>

	</div>


	<div class="ext ext_state1">
		<h1>提示</h1>
		<div class="ext_hint ext_hint_success">
			<div class="img"></div>
			<div class="content">
				<h2>恭喜您，充值成功!</h2>
				<p>
					您现在可以去查看<a href="javascript:goTo('account')">充值记录</a>、继续<a
						href="javascript:goTo('recharge');">充值</a> 、<a
						href="javascript:goTo('order');">去支付</a>
				</p>
				<a href="javascript:clickConfirm('.ext_state1');" class='confirm2'>确认</a>
			</div>
		</div>
		<a href="#" class="close"> 关闭 </a>
	</div>


	<div class="ext ext_state2" style="height: 220px">
		<h1>提示</h1>
		<div class="ext_hint ext_hint_wait">
			<div class="img"></div>
			<div class="content">
				<h2>充值处理中，请稍后查询!</h2>
				<p>
					您现在可以去查看<a href="javascript:goTo('account')">充值记录</a>、继续<a
						href="javascript:goTo('recharge');">充值</a> 、<a
						href="javascript:goTo('order');">去支付</a>
				</p>
				<a href="javascript:clickConfirm('.ext_state2');" class='confirm2'>确认</a>
			</div>
		</div>
		<a href="#" class="close"> 关闭 </a>
	</div>

	<div id="warnMess" class="ext ext_state3">
		<h1>提示</h1>
		<div class="ext_hint ext_hint_failed">
			<div class="content">
				<p></p>
				
				<a href="javascript:closeExt('#warnMess');" style="margin-top:30px;" class='confirm2'>确认</a>
			</div>
		</div>
		<a id="closeBtn" href="#" class="close"> 关闭 </a>
		
	</div>
</div>

<script src="${config.getDomainPcstatic()}/skin/js/custom.js"></script>
<script>
var amountNotDoubleMess = "充值金额必须为整数或小数，</br>小数点后不超过2位！";
var amountNotEmptyMess = "充值金额不能为空！"; 
var amoutMoreThanLimitRecharge = "充值金额超过限制！";

    var table_part=$(".ext_limit2 .content");
    api= table_part.jScrollPane({
        showArrows:false,
        maintainPosition: true,
        verticalGutter:-7
    }).data('jsp');
    $("#viewlimitation").click(function(e){
        e.preventDefault();
        openExt(".ext_limit2");
        api.reinitialise();
    })
    

click_yzm(function(){
	sendMessage();
},function(){
	var amount = $.trim($("#amount").val());
	if(!amount){
		showWarningMessage(amountNotEmptyMess);
		return;
	}else if(!validateNumber(amount)) {
		showWarningMessage(amountNotDoubleMess);
		return;
	} else if(isMoreThanLimitRecharge(amount)) {
		showWarningMessage(amoutMoreThanLimitRecharge);
		return;
	}
})
	
function clickConfirm(obj) {
	closeExt(obj);
	window.location.href = "initRecharge.do";
}
		
$("#amount").change(function(){
	var amount = $.trim($("#amount").val());
	if(amount && validateNumber(amount) && !isMoreThanLimitRecharge(amount)){
		enable_yzm();
	}else{
		lock_yzm();
	}
})
		
/**
 * 发送短信验证码
*/
function sendMessage(){
	var amount = $.trim($("#amount").val());
	if(!amount){
		showWarningMessage(amountNotEmptyMess);
		return;
	}else if(!validateNumber(amount)) {
		showWarningMessage(amountNotDoubleMess);
		return;
	} else if(isMoreThanLimitRecharge(amount)) {
		showWarningMessage(amoutMoreThanLimitRecharge);
		return;
	}
	
	var mobileNo = $("#mobileNo").val();//手机号
	$("#smsCode").removeAttr("disabled");
	$.ajax({
		url:"sendSmsForRecharge.do",
		type:"GET",
		dataType:'json',
		data:{"mobileNo": mobileNo,"transAmt":amount,"refresh" : getRandomNum(999)},
		success : function(data) {
		},
		error : function(error) {
		}
   });
}	

	function getRandomNum(n) {
		var random = Math.floor(Math.random()*n+1);
		var b = new Date();
		return b.getTime()+""+random;
	}

	/**
	 * 提交充值请求
	*/
	function submit_recharge(){	
		var cardNoEnd = $("#cardNoEnd").val();//银行卡号		
		var amount = $.trim($("#amount").val());//充值金额		
		var smsCode = $.trim($("#smsCode").val());//短信验证码
		var transPwd = $("#transPwd").val();//交易密码
		if(!cardNoEnd){
			showWarningMessage("请先绑定银行卡！");
			return;
		}else if(!amount){
			showWarningMessage(amountNotEmptyMess);
			return;
		} else if(!validateNumber(amount)) {
			showWarningMessage(amountNotDoubleMess);
			return;
		} else if(isMoreThanLimitRecharge(amount)) {
			showWarningMessage(amoutMoreThanLimitRecharge);
			return;
		}else if(!smsCode){
			showWarningMessage("短信验证码不能为空！");
			return;
		}else if(!transPwd){
			showWarningMessage("交易密码不能为空！");
			return;
		}
		var mobileNo = $("#mobileNo").val();

		$("a.submit").removeAttr("href");

		$.ajax({
			url:"recharge.do",
			type:"POST",
			dataType:'json',	
			
			data:{"mobileNo": mobileNo, 
				"transAmt":amount,
				"cardNoEnd":cardNoEnd,				
				"smsCode":smsCode,
				"transPwd":transPwd
			},
			success : function(data) {
				showRechargeResult(data);
				$("a.submit").attr("href","javascript:submit_recharge();");
			},
			error : function(error) {
				showWarningMessage("系统异常!"+"</br>请联系管理员!");
				$("a.submit").attr("href","javascript:submit_recharge();");
			}
	   });
	}
	function showRechargeResult(data) {
		if(data != null) {
			if(data['result'] == true || data['result'] == 'true') {
				if(data['responseDesc'] == 'processing') {
					showRechargeResultMessage(".ext_state2");
				} else if(data['responseDesc'] == 'success') {
					showRechargeResultMessage(".ext_state1");
				}
			} else {
				showWarningMessage(data.msg);
			}
		}
	}
	
	function showWarningMessage(message) {
		$(".ext_state3 p").html("<font size='4px'>"+message+"</font>");
		openExt(".ext_state3");
		$(".ext h1").css("height","10px").css("line-height", "10px");
	}
	
	function showRechargeResultMessage(obj) {
		openExt(obj);
		$(".ext h1").css("height","10px").css("line-height", "10px");
	}
	
	function isMoreThanLimitRecharge(value) {
		var bankId = $("#bankId").val();
		if(!value)
			return true;
		var amount = parseFloat(value);
		if(bankId == "ICBC")//工行
			return amount > 50000;
		else if(bankId == "BOC")//中国银行
			return amount > 10000;
		else if(bankId == "CCB")//建设
			return amount > 500000;
		else if(bankId == "PSBC")//邮政
			return amount > 10000;
		else if(bankId == "CMB")//招商
			return amount > 50000;
		else if(bankId == "CIB")//兴业
			return amount > 20000;
		else if(bankId == "CEB")//光大
			return amount > 500000;
		else if(bankId == "GDB")//广发
			return amount > 500000;
		else if(bankId == "CMBC")//民生
			return amount > 500000;
		else if(bankId == "PINGAN")//平安
			return amount > 500000;
		else if(bankId == "HXB")//华夏
			return amount > 1000000;
		else if(bankId == "SPDB")//浦东发展
			return amount > 49999;
		else
		    return amount > 500000;//默认50W
		//中信银行，广州农商，广州银行，目前没有bankId  TODO
		return false;
	}
	
	function validateNumber(value) {
		if(value != null)
			value = $.trim(value);
		var reg =/^[+]?\d+\.?\d{0,2}$/;
		var val = parseFloat(value);
		if(reg.test(value) && val > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 金额格式校验
	*/
	function isNotDouble(value){          
        if(value && value.length!=0){   
        	reg=/^[-\+]?\d+(\.\d+)?$/;        
	        if(!reg.test(value)){  
	        	return true;
	        }  
        }
        return false;
    }
	
	
    function goTo(action) {
    	if(action == 'account') {
    		window.location.href = "account.do?action=rechargeRecord&currentPage=1";
    	} else if(action == 'recharge') {
    		window.location.href = "initRecharge.do";
    	} else if(action == "order") {
    		window.location.href = $("#orderPage").val();
    	}
    }
    
    /**
    $('#closeBtn').bind('click', function(e) {
    	if(resultFlag) {
    		resultFlag = false;
     		window.location.href = "account.do";
    	}
    });
    */
</script> 

<input type="hidden" id="orderPage" value="${config.getDomainProfile()}/orders.do" />
<input type="hidden" id="bankId" name="bankId" value="${bankId}" />
</@page.pc>