<#import "../share/pc/layout.ftl" as page>
<@page.pc>

<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item4"> </@leftNav>
		<!-- 	add right content -->
		<div class="right">
			<div class="user_page_inner  user_page4-2">
				<h1>提现</h1>
				<form class="cha_pass_f">
					<div class="input_row ">
						<label> <i>*</i> 提现金额
						</label>

						<div class="input_part">
							<input id="amount" name="amount" type="text" maxlength="7"> 
						</div>
						<lable>&nbsp;&nbsp; 
						<font color="red">
						当前可提现金额
								${acctInfo.acctBal! '0.00'}元
						</font>
						</lable>

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
						 
						 <a id="view_limit" href="#" class="normal"> 查看限额 </a>
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
							href="javascript:submit_cash();"> 提交 </a>
					</div>
				</form>
				<input type="button" name="test" value"test1"/>

				<div class="page4_extop">
					<div class='page4_explain'>
						<h4>说明</h4>
						<p>1、不同银行提现额度请查看限额</p> 
						<p>2、工作日17点前提现预计当日到账，17点后提现预计次日到账，节假日顺延</p>
						<p>3、提现过程遇到问题，请联系客服邮箱pub_zc@pingan.com.cn</P>
					</div>
				</div>
			</div>
		</div>
	</div>


	<div class="bgcover"></div>
	<!--提现限额 -->
	<div class="ext ext_limit3">
		<h1 align="center">提现支持银行列表</h1>
		<div class="table_head">
			<table>
				<tr>
					<td>序号</td>
					<td>银行</td>
					<td>账户类型</td>
					<td>交易类型</td>
					<td>单笔限额</td>
				</tr>
			</table>
		</div>
		<div class="content">
			<table>
				<tbody>
					<tr>
						<td>1</td>
						<td>中国工商银行</td>
						<td>借记卡</td>
						<td>实时付款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>2</td>
						<td>中国银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr>
						<td>3</td>
						<td>中国建设银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>4</td>
						<td>邮政储蓄银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr>
						<td>5</td>
						<td>招商银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>6</td>
						<td>兴业银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr>
						<td>7</td>
						<td>光大银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>8</td>
						<td>广发银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr>
						<td>9</td>
						<td>民生银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>10</td>
						<td>平安银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>11</td>
						<td>华夏银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr>
						<td>12</td>
						<td>中信银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>13</td>
						<td>浦东发展银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr>
						<td>14</td>
						<td>广州农商行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
					<tr class="sp">
						<td>15</td>
						<td>广州银行</td>
						<td>借记卡</td>
						<td>实时扣款</td>
						<td>100万</td>
					</tr>
				</tbody>
			</table>
		</div>
		<p class="table_infor">
			<span>备注：</span><br>
			1、商户提交代付银行账户信息准确有效，如因代付账户状态或信息有误导致代付有误，商户需核实清楚后重新提交；<br>2、如因银联或银行系统故障导致代付失败，需经易联对账完成或核实具体情况后，方可重新提交；<br>3、代付交易数据和资金需在工作日下午14点前提交，资金才能在当天付出。
		</p>
		<a href="#" class="close"> 关闭 </a>
	</div>


	<div class="ext ext_state1">
		<h1>提示</h1>
		<div class="ext_hint ext_hint_success">
			<div class="img"></div>
			<div class="content">
				<h2>恭喜您，提现成功!</h2>
				<p>
					您现在可以去查看<a href='account.do?action=cashRecord&currentPage=1'>提现记录</a>、 继续<a href='initCash.do'>提现</a>
				</p>
				<a href="javascript:clickConfirm('.ext_state1');" class='confirm2'>确认</a>
			</div>
		</div>
		<a href="#" class="close"> 关闭 </a>

		<script
			src="${config.getDomainPcstatic()}/skin/js/jquery.jscrollpane.min.js"></script>
	</div>


	<div class="ext ext_state2" style="height: 260px">
		<h1>提示</h1>
		<div class="ext_hint ext_hint_wait">
			<div class="img"></div>
			<div class="content">
				<h2>提现处理中，请稍后查询!</h2>
				<p>
					工作日17点前提现预计当日到账，</br>17点后提现次日到账 </br> 您现在可以去查看<a href='account.do?action=cashRecord&currentPage=1'>提现记录</a>、
					继续<a href='initCash.do'>提现</a>
				</p>
				<a href="javascript:clickConfirm('.ext_state2');" class='confirm2'>确认</a>
			</div>
		</div>
		<a href="#" class="close"> 关闭 </a>
	</div>

	<div class="ext ext_state3">
		<h1>提示</h1>
		<div class="ext_hint ext_hint_failed">
			<div class="content">
				<p></p>
				<a href="javascript:closeExt('.ext_state3');" style="margin-top:30px;" class='confirm2'>确认</a>
			</div>
		</div>
		<a id="closeBtn" href="#" class="close"> 关闭 </a>
	</div>
</div>
       

		
<script src="${config.getDomainPcstatic()}/skin/js/custom.js"></script>
		
<script>
var amountNotDoubleMess = "提现金额必须为整数或小数，</br>小数点后不超过2位！";
var amountNotEmptyMess = "提现金额不能为空！"; 
var amoutMoreThanLimit = "提现金额超过限制！";

    var table_part=$(".ext_limit3 .content");
    api= table_part.jScrollPane({
        showArrows:false,
        maintainPosition: true,
        verticalGutter:-7
    }).data('jsp');
    $("#view_limit").click(function(e){
        e.preventDefault();
        openExt(".ext_limit3");
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
	} else if(!isNotMoreThanLimit(amount)) {
		showWarningMessage(amoutMoreThanLimit);
		return;
	}
})
		
$("#amount").change(function(){
	var amount = $.trim($("#amount").val());
	if(amount && validateNumber(amount) && isNotMoreThanLimit(amount)){
		enable_yzm();
	}else{
		lock_yzm();
	}
})

function clickConfirm(obj) {
	closeExt(obj);
	window.location.href = "initCash.do";
}
		
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
	}else if(!isNotMoreThanLimit(amount)) {
		showWarningMessage(amoutMoreThanLimit);
		return;
	}
	
	
	var mobileNo = $("#mobileNo").val();//手机号
	$("#smsCode").removeAttr("disabled");
	var amount = $.trim($("#amount").val());
	$.ajax({
		url:"sendSmsForCashing.do",
		type:"GET",
		dataType:'json',	
		data:{"mobileNo":mobileNo,"transAmt" : amount, "refresh": getRandomNum(999)},
		success : function(data) {
			console.log(data);
		},
		error : function(error) {
			console.log(error);
		}
   });
}	

	function getRandomNum(n) {
		var random = Math.floor(Math.random()*n+1);
		var b = new Date();
		return b.getTime()+""+random;
	}

	/**
	 * 提交提现请求
	*/
	function submit_cash(){	
		var cardNoEnd = $("#cardNoEnd").val();//银行卡号		
		var amount = $.trim($("#amount").val());//提现金额		
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
		} else if(!isNotMoreThanLimit(amount)){
			showWarningMessage(amoutMoreThanLimit);
			return;
		} else if(!smsCode){
			showWarningMessage("短信验证码不能为空！");
			return;
		}else if(!transPwd){
			showWarningMessage("交易密码不能为空！");
			return;
		}
		var mobileNo = $("#mobileNo").val();
		
		$("a.submit").removeAttr("href");
		
		$.ajax({
			url:"cashing.do",
			type:"POST",
			dataType:'json',	
			data:{"mobileNo":mobileNo,
				"amount":amount,
				"cardNoEnd":cardNoEnd,				
				"smsCode":smsCode,
				"transPwd":transPwd	
			},
			success : function(data) {
				showCashResult(data);
				$("a.submit").attr("href","javascript:submit_cash();");
			},
			error : function(error) {
				$("a.submit").attr("href","javascript:submit_cash();");
			}
	   });
	}
	function showCashResult(data) {
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
	
	function showRechargeResultMessage(obj) {
		openExt(obj);
		$(".ext h1").css("height","10px").css("line-height", "10px");
	}
	
	function showWarningMessage(message) {
		$(".ext_state3 p").html("<font size='4px'>"+message+"</font>");
		openExt(".ext_state3");
		$(".ext h1").css("height","10px").css("line-height", "10px");
	}
	
	function validateNumber(value) {
		if(value != null)
			value = $.trim(value);
		var reg =/^[+]?\d+\.?\d{0,2}$/;
		if(reg.test(value) && parseFloat(value) > 0) {
			return true;
		}
		return false;
	}
	
	function isNotMoreThanLimit(value) {
		if(!value)
			return false;
		if(parseFloat(value)>1000000)
			return false;
		return true;
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
	
</script> 
</@page.pc>
