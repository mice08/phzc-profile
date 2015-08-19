<#import "../share/pc/iFrameLayout.ftl" as page> 
<@page.pc>

<input id="msg_hidden" type="hidden" value="${msg!''}" />

<div class="ext ext_iframe">
	<div id="bindForm" class="noframe ext_card_add">
		<p class="user_warning">请保持身份信息与该银行卡开户信息一致，否则将认证失败。只支持储蓄卡绑定。</p>
		<form id="form_bindCard" action="/yeepay/iFrameBindStepTwo.do">
			<div class="input_row">
				<label> 持卡人 </label> 
				<span name="realName"> ${userInfo.usrName} </span>
			</div>
			<div class="input_row">
				<label> 银行卡号 </label>
				<div class="input_part">
					<input type="text" name="cardId">
				</div>
			</div>
			<div class="input_row">
				<label> 银行 </label> 
				<span name="bankName">  </span>
			</div>
			<div class="input_row">
				<label> 手机号码 </label>
				<div class="input_part">
					<input type="text" name="mobile">
					<span> 银行预留手机号码 </span>
				</div>
			</div>
			<div class="input_row">
				<label> 短信验证码 </label>
				<div class="input_part yzm_part">
					<input type="text" name="smsVerifyCode"> 
					<span></span>
				</div>
				<a href="javascript:void(0);" class="yzm"> 发送验证码 </a>
			</div>
			<div class="input_row">
				<label> </label> <input type="checkbox" name="file3" value="1">
				<span> 我已阅读并同意<a href="javascript:void(0);" onclick="openProtocol(); return true;">《快捷支付协议》</a>
				</span>
			</div>
			<div class="input_row">
				<label> </label> <a class="submit disable" href="javascript:void(0);"> 同意协议并提交 </a>
			</div>
		</form>
		<div class="user_note">
			<span> 说明： </span><br> 1、仅支持绑定储蓄卡<br>
			2、支持的银行包括：平安银行、工商银行、建设银行、中国银行、交通银行、农业银行、招商银行、民生银行、广发银行、浦发银行、兴业银行、光大银行、华夏银行、中信银行、邮储银行。
		</div>
	</div>

	<div id="submitResult" class="noframe ext_state1" style="display:none;">
		<div class="ext_hint ext_hint_success">
			<div class="img"></div>
			<div class="content">
				<h2>
					恭喜您，添加成功
				</h2>
				<p>
					成功绑定银行卡后，您可以去<a href="javascript:void(0);" onclick="gotoAccount();">充值</a>、继续
					<a href="javascript:void(0);" onclick="gotoOrders();">支付</a>订单、或去首页逛逛<br>
					未成功绑定银行卡？<a href="javascript:void(0);" onclick="gotoBindForm();">重新绑定银行卡</a>
				</p>
				<a  href="javascript:void(0);" onclick="confirmSumbitSuccess();" class="confirm2">关闭</a>
			</div>
		</div>
	</div>

</div>

<script>

function gotoAccount() {
	parent.window.open("${config.getDomainProfile()}/account/account.do");
}

function gotoOrders() {
	parent.window.open("${config.getDomainProfile()}/orders.do");
}

function gotoBindForm() {
	parent.window.open("${config.getDomainProfile()}/card/bindForm.do");
}

function openProtocol() {
	parent.window.open("${config.getDomainWww()}/static/protocol3.html");
}

function alertMsg() {
	var msg = $("#msg_hidden").val();
	if (msg !== null && msg !== "" && typeof(msg) !== "undefined") {
		alert(msg);
	}
}

window.onload = alertMsg;

function prepareData() {
	remove_infornmation_alert("cardId");
	remove_infornmation_alert("bankName");
	remove_infornmation_alert("realName");
	remove_infornmation_alert("mobile");
	
	var mainobj = $("#form_bindCard");
	var action = mainobj.attr("action");
	
	var realName;
	
	var cardId = mainobj.find("[name='cardId']").val();
	var bankName = mainobj.find("[name='bankName']").text();
	var mobile = mainobj.find("[name='mobile']").val();
	
	if ($.trim(cardId) === "") {
		infornmation_alert("error", "cardId", "请输入银行卡号");
		return false;
	}

	realName = mainobj.find("[name='realName']").text();
	if ($.trim(realName) === "") {
		infornmation_alert("error", "realName", "请输入姓名");
		return false;
	}

	if(!checkUserName_phone(mobile)) {
		infornmation_alert("error", "mobile", warning);
		lock_yzm();
		return false;
	}

	var data = {
		"cardId" : cardId,
		"realName" : realName,
		"bankName" : bankName,
		"mobile" : mobile
	};
	
	return data;
}

$(function(){
	click_yzm(function() {
		var form_bindCard = $("#form_bindCard");
		var mobile_val = form_bindCard.find("[name='mobile']").val();
		
		var data = prepareData();

		$.ajax({
			type : 'POST',
			url : "/yeepay/iFrameBindStepOne.do",
			data : data,
			success : function(data) {
				if (data.success === true) {
					$('a.submit').removeClass('disable');
				} else {
					alert(data.msg);
				}
			},
			error : function(error) {
			},
			dataType : "json"
		});
	}, function() {});

});

function confirmSumbitSuccess() {
	parent.${successFunction}();
}

/*form_bind*/
$(function() {
	
	$("[name=cardId]").blur(function(e) {
		remove_infornmation_alert("cardId");
		var cardId = this.value;
		if($.trim(cardId) === "") {
			infornmation_alert("error", "cardId", "请输入银行卡号");
			return false;
		}
		
		$.ajax({
			type : 'GET',
			url : 'queryBankInfo.do',
			data : {
				'cardId' : cardId
			},
			success : function(data) {
				if (data.success === true) {
					$("[name=bankName]").text(data.bankInfo.bankNm);
				} else {
					$("[name=bankName]").text('');
				}
			},
			error : function(error) {
			},
			dataType : "json"
		});
		
	});
	
	$("[name=mobile]").blur(function(e) {
		remove_infornmation_alert("mobile");
		var mobile = this.value;
		if(!checkUserName_phone(mobile)) {
			infornmation_alert("error", "mobile", warning);
			lock_yzm();
			return false;
		} else {
			enable_yzm();
		}
	});
	
	var form_bind = $("#form_bindCard");
	form_bind.find("a.submit").click(function(e) {
		if ($('a.submit').hasClass('disable')) {
			return false;
		}

		remove_infornmation_alert("smsVerifyCode");
		
		var mainobj = $(this).parents("form");
		e.preventDefault();
		e.stopPropagation();
		var action = mainobj.attr("action");
		
		var smsVerifyCode = mainobj.find("[name='smsVerifyCode']").val();
		if($.trim(smsVerifyCode) === "") {
			infornmation_alert("error", "smsVerifyCode", "请输入短信验证码");
			return false;
		}

		if (mainobj.find("[name='file3']").attr("checked") === undefined) {
			alert("请先同意快捷支付协议");
			return false;
		}

		var data = {
			"smsVerifyCode" : smsVerifyCode
		};

		$('a.submit').addClass('disable');

		$.ajax({
			type : 'POST',
			url : action,
			data : data,
			success : function(data) {
				$('a.submit').removeClass('disable')
				if (data.success === true) {
					$("#bindForm")[0].style.display="none";
					$("#submitResult")[0].style.display="";
				} else {
					parent.${failureFunction}(data.msg);
				}
			},
			error : function(error) {
				$('a.submit').removeClass('disable')
			},
			dataType : "json"
		});

	});

});

</script>

</@page.pc>