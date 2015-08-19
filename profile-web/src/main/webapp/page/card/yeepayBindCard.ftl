<#import "../share/pc/layout.ftl" as page> 
<@page.pc>

<div class="user_page">
	<div class="main clearfix">
	
<#include "../leftNav.ftl">
	<@leftNav activeMenu="item3">
</@leftNav>

<div class="right">
	<div class="user_page_inner  user_page3">
		<h1>绑定银行卡</h1>
		<div class="user_form">
			<input id="certStatus_hidden" type="hidden" value="${certStatus}" />
			<form id="form_bindCard" action="/yeepay/bindStepTwo.do">
				<div class="input_row">
					<label> 银行卡号 </label>
					<div class="input_part">
						<input type="text" name="cardId">
					</div>
				</div>
				<div class="input_row">
					<label> 银行 </label>
					<span name="bankName"></span>
					<b id="notSupportInfo" style="display: none;">
						暂时不支持该银行，请绑定其他银行卡
					</b>
				</div>
				
				<#if certStatus == 0>
					<div class="input_row">
						<label> 姓名 </label>
						<div class="input_part">
							<input type="text" name="realName">
						</div>
					</div>
					<div class="input_row">
						<label> 身份证号码 </label>
						<div class="input_part">
							<input type="text" name="certId">
						</div>
					</div>
				<#else>
					<div class="input_row">
						<label> 姓名 </label>
						<span name="realName_span"> ${realName}</span>
					</div>
					<div class="input_row">
						<label> 身份证号码 </label>
						<span name="certId_span"> ${certId}</span>
					</div>
				</#if>
				
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
				<hr>
					
				<#if certStatus != 2>
					<div class="input_row">
						<label> 交易密码 </label>
						<div class="input_part">
							<input type="password" name="transPwd">
						</div>
						<span class="sp"> 设置网站交易密码用于支付，交易密码不能和登录密码相同 </span>
					</div>
					<div class="input_row">
						<label> 确认交易密码 </label>
						<div class="input_part">
							<input type="password" name="transPwdConfirm">
						</div>
					</div>
				</#if>
				
				<div class="input_row">
					<label> </label> <input type="checkbox" name="file" value="1">
					<span> 我已阅读并同意<a href="${config.getDomainWww()}/static/protocol3.html" target="_blank">《快捷支付协议》</a>
					</span>
				</div>
				<div class="input_row">
					<label> </label> <a class="submit disable" href="javascript:void(0);"> 同意协议并提交 </a>
				</div>
			</form>
		</div>
		<div class="user_note">
			<span> 说明： </span><br> 1、仅支持绑定储蓄卡<br> 2、支持的银行包括：<br>
			<img src="${config.getDomainPcstatic()}/skin/images/banks.jpg">
		</div>
	</div>
</div>
</div>

<script>

function prepareData() {
	remove_infornmation_alert("cardId");
	remove_infornmation_alert("bankName");
	remove_infornmation_alert("realName");
	remove_infornmation_alert("certId");
	remove_infornmation_alert("realName_span");
	remove_infornmation_alert("certId_span");
	remove_infornmation_alert("mobile");
	
	var mainobj = $("#form_bindCard");
	var action = mainobj.attr("action");
	
	var realName, certId;
	
	var cardId = mainobj.find("[name='cardId']").val();
	var bankName = mainobj.find("[name='bankName']").text();
	var mobile = mainobj.find("[name='mobile']").val();
	
	var certStatus = mainobj.find("[name='certStatus']").val();
	var smsVerifyCode = mainobj.find("[name='smsVerifyCode']").val();
	var certStatus = $("#certStatus_hidden").val();
	
	if ($.trim(cardId) === "") {
		infornmation_alert("error", "cardId", "请输入银行卡号");
		return false;
	}
	
	if (certStatus === "0") {
		realName = mainobj.find("[name='realName']").val();
		certId = mainobj.find("[name='certId']").val();
		if ($.trim(realName) === "") {
			infornmation_alert("error", "realName", "请输入姓名");
			return false;
		}
		
		if ($.trim(certId) === "") {
			infornmation_alert("error", "certId", "请输入身份证号");
			return false;
		}
	} else {
		realName = mainobj.find("[name='realName_span']").text();
		certId = mainobj.find("[name='certId_span']").text();
		if ($.trim(realName) === "") {
			infornmation_alert("error", "realName_span", "姓名不正确");
			return false;
		}
		
		if ($.trim(certId) === "") {
			infornmation_alert("error", "certId_span", "身份证号不正确");
			return false;
		}
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
		"certId" : certId,
		"mobile" : mobile,
		"certStatus" : certStatus
	};
	
	return data;
}

/*form_bind*/
$(function() {

	click_yzm(function() {
		var data = prepareData();

		$.ajax({
			type : 'POST',
			url : "/yeepay/bindStepOne.do",
			data : data,
			success : function(data) {
				console.log(data);
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

	$("[name=cardId]").blur(function(e) {
		remove_infornmation_alert("cardId");
		var cardId = this.value;
		if ($.trim(cardId) === "") {
			infornmation_alert("error", "cardId", "请输入银行卡号");
			return false;
		}/* else {
			infornmation_alert("correct", "cardId");
		}*/
		
		$.ajax({
			type : 'GET',
			url : 'queryBankInfo.do',
			data : {
				'cardId' : cardId
			},
			success : function(data) {
				if (data.success === true) {
					$("[name=bankName]").text(data.bankInfo.bankNm);
					$("#notSupportInfo")[0].style.display = 'none';
				} else {
					$("[name=bankName]").text('');
					$("#notSupportInfo")[0].style.display = '';
					// infornmation_alert("error", "cardId", "");
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
			infornmation_alert("correct", "mobile");
			enable_yzm();
		}
	});
	
	var password=$("[name=transPwd]");
	var password_check=$(".password_check");
	var resultBar=$(".register_page form .password_check ul li");
	var resultBar1=resultBar.eq(0);
	var resultBar2=resultBar.eq(1);
	var resultBar3=resultBar.eq(2);
	function check_keydown() {
		var result=password_detect(password.val());
		if (result==3) {
			remove_infornmation_alert("transPwd");
			password_check.show();
			resultBar.addClass("active");
		}
		if (result==2) {
			remove_infornmation_alert("transPwd");
			password_check.show();
			resultBar.removeClass("active");
			resultBar1.addClass("active");
			resultBar2.addClass("active");
		}
		if (result==1) {
			remove_infornmation_alert("transPwd");
			password_check.show();
			resultBar.removeClass("active");
			resultBar1.addClass("active");
		}
		if (result==0) {
			password_check.hide();
			resultBar.removeClass("active");
			remove_infornmation_alert("transPwd");
			if (!(checkPassWord_register(password.val()))) {
				infornmation_alert("error", "transPwd", warning);
				return false;
			}
		}
	};
	password.keyup(function() {
		check_keydown();
	});
	password.blur(function() {
		check_keydown()
	});

	var confirmPwd = $("[name=transPwdConfirm]");
	confirmPwd.blur(function() {
		remove_infornmation_alert("transPwdConfirm");
		if (!checkRePassWord(confirmPwd.val(), password.val())) {//重复密码
			infornmation_alert("error", "transPwdConfirm", warning);
			return false;
		}
	});
	
	var form_bind = $("#form_bindCard");
	form_bind.find("a.submit").click(function(e) {

		if ($('a.submit').hasClass('disable')) {
			return false;
		}

		remove_infornmation_alert("smsVerifyCode");
		remove_infornmation_alert("transPwd");
		remove_infornmation_alert("confirmTransPwd");
		
		var mainobj = $(this).parents("form");
		e.preventDefault();
		e.stopPropagation();
		var action = mainobj.attr("action");
		
		var smsVerifyCode = mainobj.find("[name='smsVerifyCode']").val();
		if ($.trim(smsVerifyCode) === "") {
			infornmation_alert("error", "smsVerifyCode", "请输入短信验证码");
			return false;
		}
		
		var certStatus = $("#certStatus_hidden").val();

		var data = {
			"smsVerifyCode" : smsVerifyCode,
			"certStatus" : certStatus
		};
		
		if (certStatus !== "2") {
			var transPwd = mainobj.find("[name='transPwd']").val();
			if ($.trim(transPwd) === "") {
				infornmation_alert("error", "transPwd", "请输入交易密码");
				return false;
			}
			data.transPwd = transPwd;
			
			var confirmTransPwd = mainobj.find("[name='transPwdConfirm']").val();
			if (!checkRePassWord(confirmTransPwd, transPwd)) {
				infornmation_alert("error", "transPwdConfirm", warning);
				return false;
			}
			data.confirmTransPwd = confirmTransPwd;
		}
		
		if (mainobj.find("[name='file']").attr("checked") === undefined) {
			alert("请先同意快捷支付协议");
			return false;
		}

		$('a.submit').addClass('disable');

		$.ajax({
			type : 'POST',
			url : action,
			data : data,
			success : function(data) {
				console.log(data);
				$('a.submit').removeClass('disable')
				if (data.success === true) {
					window.location.href = "/card/myCards.do";
				} else {
					alert(data.msg);
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