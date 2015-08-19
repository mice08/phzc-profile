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
		<div class="user_warning">
			认证时会从卡内试扣1.00元，认证结束后即归还原卡。请保持身份信息与该银行卡开户信息一致，否则将认证失败</div>
		<div class="user_form">
			<input id="certStatus_hidden" type="hidden" value="${certStatus}" />
			<form id="form_bindCard" action="bind.do">
				<div class="input_row">
					<label><i>*</i>银行卡号 </label>
					<div class="input_part">
						<input type="text" name="cardId"> <!-- class="error"> -->
					</div>
					<!-- <em class="correct"></em> -->
				</div>
				<div class="input_row">
					<label>银行 </label>
					<span name="bankName"></span>
				</div>
				
				<#if certStatus == 0>
					<div class="input_row">
						<label><i>*</i>姓名 </label>
						<div class="input_part">
							<input type="text" name="realName">
						</div>
					</div>
					<div class="input_row">
						<label><i>*</i>身份证号码 </label>
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
					<label><i>*</i>手机号码 </label>
					<div class="input_part">
						<input type="text" name="mobile">
						<span> 银行预留手机号码 </span>
					</div>
					<!-- <em class="error">手机号格式错误</em> -->
				</div>
				<div class="input_row">
					<label><i>*</i>短信验证码 </label>
					<div class="input_part yzm_part">
						<input type="text" name="smsVerifyCode"> 
						<span></span>
					</div>
					<a href="javascript:void(0);" class="yzm"> 发送验证码 </a>
				</div>
				<hr>
					
				<#if certStatus != 2>
					<div class="input_row">
						<label><i>*</i>交易密码 </label>
						<div class="input_part">
							<input type="password" name="transPwd">
						</div>
						<span class="sp"> 设置网站交易密码用于支付，交易密码不能和登录密码相同 </span>
					</div>
					<div class="input_row">
						<label><i>*</i>确认交易密码 </label>
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
					<label> </label> <a class="submit" href="javascript:void(0);"> 同意协议并提交 </a>
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
$(function(){
	click_yzm(function() {
		var form_bindCard = $("#form_bindCard");
		var mobile_val = form_bindCard.find("[name='mobile']").val();
		
		var data = {
			"mobile" : mobile_val
		};

		$.ajax({
			type : 'POST',
			url : "/sms/sendForBind.do",
			data : data,
			success : function(data) {
			},
			error : function(error) {
			},
			dataType : "json"
		});
	}, function() {});

})

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
					$("[name=bankName]").text("暂不支持该银行，请更换其他银行卡");
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
	
	$("[name=transPwd]").blur(function(e) {
		remove_infornmation_alert("transPwd");
		var transPwd = $("[name='transPwd']").val();
		if (!checkPassWord(transPwd)) {
			infornmation_alert("error", "transPwd", warning);
			return false;
		}
	});
	
	$("[name=transPwdConfirm]").blur(function(e) {
		remove_infornmation_alert("transPwdConfirm");
		var transPwd = $("[name='transPwd']").val();
		var confirmTransPwd = $("[name='transPwdConfirm']").val();
		if (!checkRePassWord(confirmTransPwd, transPwd)) {
			infornmation_alert("error", "transPwdConfirm", warning);
			return false;
		}
	});
	
	var form_bind = $("#form_bindCard");
	form_bind.find("a.submit").click(function(e) {
		remove_infornmation_alert("cardId");
		remove_infornmation_alert("bankName");
		remove_infornmation_alert("realName");
		remove_infornmation_alert("certId");
		remove_infornmation_alert("realName_span");
		remove_infornmation_alert("certId_span");
		remove_infornmation_alert("mobile");
		remove_infornmation_alert("smsVerifyCode");
		remove_infornmation_alert("transPwd");
		remove_infornmation_alert("confirmTransPwd");
		
		var mainobj = $(this).parents("form");
		e.preventDefault();
		e.stopPropagation();
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
			$("a.yzm").addClass("disable");
			return false;
		}
		
		if($.trim(smsVerifyCode) === "") {
			infornmation_alert("error", "smsVerifyCode", "请输入短信验证码");
			return false;
		}
		
		var data = {
			"cardId" : cardId,
			"realName" : realName,
			"bankName" : bankName,
			"certId" : certId,
			"mobile" : mobile,
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

		$.ajax({
			type : 'POST',
			url : action,
			data : data,
			success : function(data) {
				if (data.success === true) {
					window.location.href = "submitSuccess.do";
				} else {
					alert(data.msg);
				}
			},
			error : function(error) {
			},
			dataType : "json"
		});

	});

});
</script>

</@page.pc>